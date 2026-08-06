/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.timetransfers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface TimeTransferRepository extends ListCrudRepository<TimeTransfer, Long> {

    List<TimeTransfer> findAllByOrderByDateOfServiceDesc();

    /** Returns the (up to) N most recently created booking-report rows for TimeTransfers that were booked by someone
     *  other than the service provider (fromMember), pre-formatted as a DTO ready for display. */
    @Query("""
            SELECT new eu.nabahilfe.webapp.timetransfers.TimeTransferBookingReportRow(
                t.createdAt,
                CONCAT(cb.lastName, ' ', cb.firstName),
                CONCAT(fm.lastName, ' ', fm.firstName),
                CONCAT(tm.lastName, ' ', tm.firstName),
                t.dateOfService,
                t.hours,
                TRIM(CONCAT(COALESCE(o.code, ''), ' ', COALESCE(o.description, ''))),
                t.note
            )
            FROM TimeTransfer t
            JOIN t.createdBy cb
            JOIN t.fromMember fm
            JOIN t.toMember tm
            LEFT JOIN t.offer o
            WHERE cb.id <> fm.id
            ORDER BY t.createdAt DESC
            """)
    List<TimeTransferBookingReportRow> findForeignBookingsOrderByCreatedAtDesc(Pageable pageable);

    /** Returns the (up to) N most recently created booking-report rows for TimeTransfers that were self-booked
     *  by the service provider (fromMember == createdBy), pre-formatted as a DTO ready for display. */
    @Query("""
            SELECT new eu.nabahilfe.webapp.timetransfers.TimeTransferBookingReportRow(
                t.createdAt,
                CONCAT(cb.lastName, ' ', cb.firstName),
                CONCAT(fm.lastName, ' ', fm.firstName),
                CONCAT(tm.lastName, ' ', tm.firstName),
                t.dateOfService,
                t.hours,
                TRIM(CONCAT(COALESCE(o.code, ''), ' ', COALESCE(o.description, ''))),
                t.note
            )
            FROM TimeTransfer t
            JOIN t.createdBy cb
            JOIN t.fromMember fm
            JOIN t.toMember tm
            LEFT JOIN t.offer o
            WHERE cb.id = fm.id
            ORDER BY t.createdAt DESC
            """)
    List<TimeTransferBookingReportRow> findSelfBookingsOrderByCreatedAtDesc(Pageable pageable);

    List<TimeTransfer> findTop10ByFromMember_IdOrToMember_IdOrderByDateOfServiceDesc(Long fromMemberId, Long toMemberId);


    List<TimeTransfer> findAllByToMember_IdOrderByDateOfServiceDesc(Long memberId);

    @Query("SELECT t FROM TimeTransfer t WHERE t.toMember.id = :memberId AND YEAR(t.dateOfService) = :year ORDER BY t.dateOfService DESC")
    List<TimeTransfer> findAllByToMember_IdAndYearOrderByDateOfServiceDesc(Long memberId, int year);

    List<TimeTransfer> findAllByFromMember_IdOrderByDateOfServiceDesc(Long memberId);

    @Query("SELECT t FROM TimeTransfer t WHERE t.fromMember.id = :memberId AND YEAR(t.dateOfService) = :year ORDER BY t.dateOfService DESC")
    List<TimeTransfer> findAllByFromMember_IdAndYearOrderByDateOfServiceDesc(Long memberId, int year);

    Optional<TimeTransfer> findByIdAndFromMember_Id(Long id, Long fromMemberId);

    /** Returns [offerCode, offerDescription, totalHours, transferCount] grouped by offer category for the given year, sorted by total hours desc */
    @Query("""
            SELECT COALESCE(o.code, '---'), COALESCE(o.description, '(keine Kategorie)'), SUM(t.hours), COUNT(t)
            FROM TimeTransfer t LEFT JOIN t.offer o
            WHERE YEAR(t.dateOfService) = :year
            GROUP BY o.id, o.code, o.description
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findStatsByOfferAndYear(int year);

    /** Returns [offerCode, offerDescription, totalHours, transferCount] grouped by offer category for all years, sorted by total hours desc */
    @Query("""
            SELECT COALESCE(o.code, '---'), COALESCE(o.description, '(keine Kategorie)'), SUM(t.hours), COUNT(t)
            FROM TimeTransfer t LEFT JOIN t.offer o
            GROUP BY o.id, o.code, o.description
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findStatsByOfferAllYears(org.springframework.data.domain.Pageable pageable);

    /** Returns [memberFullName, totalHours, transferCount] for members who received hours in the given year, sorted by total hours desc */
    @Query("""
            SELECT CONCAT(m.lastName, ' ', m.firstName), SUM(t.hours), COUNT(t)
            FROM TimeTransfer t JOIN t.toMember m
            WHERE YEAR(t.dateOfService) = :year
            GROUP BY m.id, m.firstName, m.lastName
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findEarnedHoursStatsByYear(int year);

    /** Returns [memberFullName, totalHours, transferCount] for members who received hours (all years), sorted by total hours desc */
    @Query("""
            SELECT CONCAT(m.lastName, ' ', m.firstName), SUM(t.hours), COUNT(t)
            FROM TimeTransfer t JOIN t.toMember m
            GROUP BY m.id, m.firstName, m.lastName
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findEarnedHoursStatsAllYears(org.springframework.data.domain.Pageable pageable);

    /** Returns [memberFullName, totalHours, transferCount] for members who gave hours in the given year, sorted by total hours desc */
    @Query("""
            SELECT CONCAT(m.lastName, ' ', m.firstName), SUM(t.hours), COUNT(t)
            FROM TimeTransfer t JOIN t.fromMember m
            WHERE YEAR(t.dateOfService) = :year
            GROUP BY m.id, m.firstName, m.lastName
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findGivenHoursStatsByYear(int year);

    /** Returns [memberFullName, totalHours, transferCount] for members who gave hours (all years), sorted by total hours desc */
    @Query("""
            SELECT CONCAT(m.lastName, ' ', m.firstName), SUM(t.hours), COUNT(t)
            FROM TimeTransfer t JOIN t.fromMember m
            GROUP BY m.id, m.firstName, m.lastName
            ORDER BY SUM(t.hours) DESC
            """)
    List<Object[]> findGivenHoursStatsAllYears(org.springframework.data.domain.Pageable pageable);

}


