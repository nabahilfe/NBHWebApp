package eu.nabahilfe.webapp.media.images;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {

	List<Gallery> findAllByOrderByCreatedAtDescIdDesc();

	List<Gallery> findAllByIsPublicTrueOrderByCreatedAtDescIdDesc();

	Optional<Gallery> findByIdAndIsPublicTrue(Long id);
}