package eu.nabahilfe.webapp.media.documents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    long countByLibrary_Id(Long libraryId);

    void deleteByLibrary_Id(Long libraryId);

    Optional<Document> findByIdAndLibrary_Id(Long documentId, Long libraryId);

    @Query("""
        select new eu.nabahilfe.webapp.media.documents.LibraryDocumentInfo(
            d.id,
            d.fileName,
            d.documentSize,
            d.contentType,
            d.description
        )
        from Document d
        where d.library.id = :libraryId
        order by d.fileName asc, d.id asc
        """)
    List<LibraryDocumentInfo> findDocumentInfos(@Param("libraryId") Long libraryId);
}
