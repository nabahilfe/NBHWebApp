package eu.nabahilfe.webapp.media.images;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {

	List<Gallery> findAllByOrderByCreatedAtDescIdDesc();

	@Query("""
			SELECT g
			FROM Gallery g
			WHERE (g.showGalleryFrom IS NULL OR g.showGalleryFrom <= CURRENT_DATE)
			  AND (g.showGalleryTo IS NULL OR g.showGalleryTo >= CURRENT_DATE)
			ORDER BY
			  CASE WHEN g.galleryDate IS NULL THEN 0 ELSE 1 END ASC,
			  g.galleryDate DESC,
			  g.id DESC
			""")
	List<Gallery> findAllVisibleForCurrentDateOrderByGalleryDateDescNullFirst();

	@Query("""
			SELECT g
			FROM Gallery g
			WHERE g.isPublic = true
			  AND (g.showGalleryFrom IS NULL OR g.showGalleryFrom <= CURRENT_DATE)
			  AND (g.showGalleryTo IS NULL OR g.showGalleryTo >= CURRENT_DATE)
			ORDER BY
			  CASE WHEN g.galleryDate IS NULL THEN 0 ELSE 1 END ASC,
			  g.galleryDate DESC,
			  g.id DESC
			""")
	List<Gallery> findPublicVisibleForCurrentDateOrderByGalleryDateDescNullFirst();

	Optional<Gallery> findByIdAndIsPublicTrue(Long id);
}