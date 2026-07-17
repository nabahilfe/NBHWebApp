/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import eu.nabahilfe.webapp.osm.NominatimService;

/**
 * Runs geo-validation of Member addresses (via {@link NominatimService}) for all
 * active, non-system members that do not yet have latitude/longitude set.
 * Progress is streamed to the browser via Server-Sent-Events, and a random delay
 * of 3-6 seconds is inserted between calls to respect the OSM Nominatim usage policy.
 */
@Service
public class AddressValidationService {

    private static final Logger log = LoggerFactory.getLogger(AddressValidationService.class);

    private static final long MIN_DELAY_MS = 1200;
    private static final long MAX_DELAY_MS = 3000;

    private final MemberRepository memberRepository;
    private final NominatimService nominatimService;

    public AddressValidationService(MemberRepository memberRepository, NominatimService nominatimService) {
        this.memberRepository = memberRepository;
        this.nominatimService = nominatimService;
    }

    /**
     * Starts the validation of all currently unvalidated member addresses in a background
     * thread and returns an {@link SseEmitter} that streams progress events to the client.
     * Event types sent: "progress" (after each member) and "done" (final summary).
     */
    public SseEmitter validateAllUnvalidatedAddresses() {

        List<Long> memberIds = memberRepository.findActiveUnvalidatedMemberIds();
        int total = memberIds.size();

        SseEmitter emitter = new SseEmitter(0L); // no timeout, we control lifecycle ourselves

        Thread worker = new Thread(() -> runValidation(memberIds, total, emitter), "address-validation-worker");
        worker.setDaemon(true);
        worker.start();

        return emitter;
    }

    private void runValidation(List<Long> memberIds, int total, SseEmitter emitter) {

        int processed = 0;
        int successCount = 0;
        int failedCount = 0;

        try {
            for (Long id : memberIds) {

                Member member = memberRepository.findById(id).orElse(null);
                String memberName = (member != null) ? member.getFirstName() + " " + member.getLastName() : "Unbekannt";

                boolean success = false;
                if (member != null) {
                    try {
                        nominatimService.validateAndUpdateMemberAddress(member);
                        memberRepository.save(member);
                        success = member.getLatitude() != null && member.getLongitude() != null;
                    }
                    catch (Exception e) {
                        log.warn("Error validating address for member {}: {}", id, e.getMessage());
                        success = false;
                    }
                }

                processed++;
                if (success) {
                    successCount++;
                } else {
                    failedCount++;
                }

                AddressValidationProgress progress = new AddressValidationProgress(
                        total, processed, successCount, failedCount, memberName, success);

                emitter.send(SseEmitter.event().name("progress").data(progress));

                // Respect OSM Nominatim usage policy: random pause between 3 and 6 seconds,
                // but not after the very last request.
                if (processed < total) {
                    long delayMs = ThreadLocalRandom.current().nextLong(MIN_DELAY_MS, MAX_DELAY_MS + 1);
                    Thread.sleep(delayMs);
                }
            }

            AddressValidationProgress finalSummary = new AddressValidationProgress(
                    total, processed, successCount, failedCount, null, null);
            emitter.send(SseEmitter.event().name("done").data(finalSummary));
            emitter.complete();
        }
        catch (Exception e) {
            log.error("Address validation run aborted due to error", e);
            try {
                emitter.completeWithError(e);
            }
            catch (Exception ignore) {
                // emitter may already be completed/closed by the client
            }
        }
    }


    /** Simple DTO sent as SSE event payload (serialized to JSON). */
    public record AddressValidationProgress(
            int total,
            int processed,
            int successCount,
            int failedCount,
            String currentMemberName,
            Boolean currentSuccess) {
    }

}
