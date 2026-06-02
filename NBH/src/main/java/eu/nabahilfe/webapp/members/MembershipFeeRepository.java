package eu.nabahilfe.webapp.members;

import java.time.Year;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface MembershipFeeRepository extends ListCrudRepository<MembershipFee, Long> {

    List<MembershipFee> findByAccountedByIsNullAndDoNotChargeFalse();

    @Query("SELECT new eu.nabahilfe.webapp.members.MembershipFeeOpenForm(m.id, m.firstName, m.lastName, m.street, m.number, m.zip, m.city) " +
           "FROM Member m " +
           "WHERE NOT (m.isImportedMember = true AND YEAR(m.createdAt) = :currentYear) " +
           "AND NOT EXISTS (SELECT r FROM Role r WHERE r = m.role AND r.roleName = 'System-Administrator') " +
           "AND NOT EXISTS (SELECT f FROM MembershipFee f WHERE f.member = m AND f.forYear = :currentYear) " +
           "ORDER BY m.lastName ASC")
    List<MembershipFeeOpenForm> findMembersWithoutFeeForYear(@Param("currentYear") Year currentYear);

}
