package eu.nabahilfe.webapp.media.documents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    List<Library> findAllByOrderByCreatedAtDescIdDesc();

    @Query("""
            SELECT l
            FROM Library l
            WHERE l.showLibraryFrom <= CURRENT_DATE
              AND (l.showLibraryTo IS NULL OR l.showLibraryTo >= CURRENT_DATE)
            ORDER BY l.id DESC
            """)
    List<Library> findAllVisibleForCurrentDateOrderByIdDesc();

    @Query("""
            SELECT l
            FROM Library l
            WHERE l.isPublic = true
              AND l.showLibraryFrom <= CURRENT_DATE
              AND (l.showLibraryTo IS NULL OR l.showLibraryTo >= CURRENT_DATE)
            ORDER BY l.id DESC
            """)
    List<Library> findPublicVisibleForCurrentDateOrderByIdDesc();

    Optional<Library> findByIdAndIsPublicTrue(Long id);
}
