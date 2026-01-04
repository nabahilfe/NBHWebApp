package eu.nabahilfe.webapp.org;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

public interface OfferRepository extends ListCrudRepository<Offer, Long> {

    List<Offer> findAll();
}
