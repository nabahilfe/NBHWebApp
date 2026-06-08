/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.registration;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Per-IP rate limiter for the "request registration code" endpoint.
 *
 * Rules:
 *   - 3 failed attempts (unknown e-mail) within 3 minutes → block for 5 minutes
 *   - The block is per client IP, independent of other users
 */
@Component
public class EmailRateLimiter {

    private static final int    MAX_FAILURES    = 3;
    private static final Duration WINDOW        = Duration.ofMinutes(3);
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(5);

    private record Entry(Instant windowStart, int failures, Instant blockedUntil) {}

    private final ConcurrentHashMap<String, Entry> table = new ConcurrentHashMap<>();

    /** Returns true when the IP is currently blocked. */
    public boolean isBlocked(String ip) {
        Entry e = table.get(ip);
        if (e == null || e.blockedUntil() == null) return false;
        return Instant.now().isBefore(e.blockedUntil());
    }

    /** Remaining block time (rounded up to full minutes), or 0 if not blocked. */
    public long blockedMinutesRemaining(String ip) {
        Entry e = table.get(ip);
        if (e == null || e.blockedUntil() == null) return 0;
        Duration remaining = Duration.between(Instant.now(), e.blockedUntil());
        if (remaining.isNegative()) return 0;
        return remaining.toMinutes() + 1; // round up so we never show "0 minutes"
    }

    /** Call this every time a non-existing e-mail was submitted for the given IP. */
    public void recordFailure(String ip) {
        table.compute(ip, (_, existing) -> {
            Instant now = Instant.now();

            // If there's an active block, just keep it
            if (existing != null && existing.blockedUntil() != null && now.isBefore(existing.blockedUntil())) {
                return existing;
            }

            // If the previous window has expired, start a fresh one
            if (existing == null || existing.windowStart().isBefore(now.minus(WINDOW))) {
                return new Entry(now, 1, null);
            }

            int newCount = existing.failures() + 1;
            if (newCount >= MAX_FAILURES) {
                return new Entry(existing.windowStart(), newCount, now.plus(BLOCK_DURATION));
            }
            return new Entry(existing.windowStart(), newCount, null);
        });
    }

    /** Remove stale entries every 10 minutes to prevent unbounded memory growth. */
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void evictExpired() {
        Instant cutoff = Instant.now().minus(BLOCK_DURATION).minus(WINDOW);
        table.entrySet().removeIf(e -> {
            Entry v = e.getValue();
            Instant latest = v.blockedUntil() != null ? v.blockedUntil() : v.windowStart();
            return latest.isBefore(cutoff);
        });
    }
}
