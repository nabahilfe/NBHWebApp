package eu.nabahilfe.webapp.registration;

import org.springframework.data.repository.ListCrudRepository;

public interface RegistrationCodeRepository extends ListCrudRepository<RegistrationCode, Long> {

    RegistrationCode findFirstByEmailOrderByIdDesc(String email);

    long deleteByEmail(String email);

}
