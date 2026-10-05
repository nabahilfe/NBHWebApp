/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.charts;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import eu.nabahilfe.webapp.members.Member;

public interface PopulationPyramidRepository extends Repository<Member, Long> {

        /** Birthdates and salutations of active, non-system members that have a birthdate. */
        @Query("SELECT new eu.nabahilfe.webapp.charts.MemberBirthdateSalutation(m.birthdate, m.salutation) FROM Member m " +
            "WHERE m.birthdate IS NOT NULL " +
           "AND m.birthdate <= CURRENT_DATE " +
                   "AND m.joiningDate IS NOT NULL " +
                   "AND YEAR(m.joiningDate) <= :joiningYear " +
           "AND (m.resignationDate IS NULL OR m.resignationDate > CURRENT_DATE) " +
           "AND (m.isSystemAccount = false OR m.isSystemAccount IS NULL)")
                List<MemberBirthdateSalutation> findActiveMemberBirthdates(@Param("joiningYear") int joiningYear);

                @Query("SELECT DISTINCT YEAR(m.joiningDate) FROM Member m " +
                        "WHERE m.joiningDate IS NOT NULL " +
                        "AND (m.isSystemAccount = false OR m.isSystemAccount IS NULL) " +
                        "ORDER BY YEAR(m.joiningDate) DESC")
                List<Integer> findDistinctJoiningYears();
}
