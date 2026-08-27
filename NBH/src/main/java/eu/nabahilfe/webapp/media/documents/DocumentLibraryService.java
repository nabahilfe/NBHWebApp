package eu.nabahilfe.webapp.media.documents;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentLibraryService {

    private static final int MAX_DOCUMENTS_PER_UPLOAD = 10;

    private final LibraryRepository libraryRepository;
    private final DocumentRepository documentRepository;

    public DocumentLibraryService(LibraryRepository libraryRepository, DocumentRepository documentRepository) {
        this.libraryRepository = libraryRepository;
        this.documentRepository = documentRepository;
    }

    @Transactional
    public void uploadDocuments(Long libraryId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bitte mindestens ein PDF auswählen.");
        }

        if (files.size() > MAX_DOCUMENTS_PER_UPLOAD) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Es können maximal " + MAX_DOCUMENTS_PER_UPLOAD + " Dokumente gleichzeitig hochgeladen werden.");
        }

        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bibliothek nicht gefunden."));

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leere Dateien können nicht hochgeladen werden.");
            }

            String fileName = resolveFileName(file);
            if (!fileName.toLowerCase().endsWith(".pdf")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Es sind nur PDF-Dokumente erlaubt (Dateiendung .pdf).");
            }

            String contentType = file.getContentType();
            if (contentType != null && !contentType.isBlank() && !"application/pdf".equalsIgnoreCase(contentType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Es sind nur PDF-Dokumente erlaubt (Content-Type application/pdf).");
            }

            byte[] fileData;
            try {
                fileData = file.getBytes();
            }
            catch (IOException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dokument konnte nicht gelesen werden.");
            }

            Document document = new Document();
            document.setLibrary(library);
            document.setFileName(fileName);
            document.setContentType("application/pdf");
            document.setDocumentData(fileData);
            document.setDocumentSize(fileData.length);
            document.setDescription(null);

            documentRepository.save(document);
        }
    }

    private String resolveFileName(MultipartFile file) {
        String original = file.getOriginalFilename();
        if (original == null || original.isBlank()) {
            return "upload.pdf";
        }

        String trimmed = original.trim();
        int slash = Math.max(trimmed.lastIndexOf('/'), trimmed.lastIndexOf('\\'));
        String baseName = slash >= 0 ? trimmed.substring(slash + 1) : trimmed;

        if (baseName.isBlank()) {
            return "upload.pdf";
        }

        return baseName;
    }

    @Transactional(readOnly = true)
    public List<LibraryDocumentInfo> findDocuments(Long libraryId) {
        return documentRepository.findDocumentInfos(libraryId);
    }

    @Transactional(readOnly = true)
    public Document findDocumentForDownload(Long libraryId, Long documentId) {
        return documentRepository.findByIdAndLibrary_Id(documentId, libraryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dokument nicht gefunden."));
    }

    @Transactional
    public void deleteDocument(Long libraryId, Long documentId) {
        Document document = documentRepository.findByIdAndLibrary_Id(documentId, libraryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dokument nicht gefunden."));
        documentRepository.delete(document);
    }
}
