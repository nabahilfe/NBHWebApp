package eu.nabahilfe.webapp.media.documents;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final DocumentRepository documentRepository;

    public LibraryService(LibraryRepository libraryRepository, DocumentRepository documentRepository) {
        this.libraryRepository = libraryRepository;
        this.documentRepository = documentRepository;
    }

    @Transactional(readOnly = true)
    public Library findById(Long libraryId) {
        return libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bibliothek nicht gefunden: " + libraryId));
    }

    @Transactional(readOnly = true)
    public List<Library> findAll() {
        return libraryRepository.findAllByOrderByCreatedAtDescIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Library> findVisibleForUsers(boolean isAuthenticated) {
        if (isAuthenticated) {
            return libraryRepository.findAllVisibleForCurrentDateOrderByIdDesc();
        }
        return libraryRepository.findPublicVisibleForCurrentDateOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public Library findVisibleByIdForUsers(Long libraryId, boolean isAuthenticated) {
        if (isAuthenticated) {
            return findById(libraryId);
        }

        return libraryRepository.findByIdAndIsPublicTrue(libraryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bibliothek nicht gefunden: " + libraryId));
    }

    public Library create(LibraryForm form) {
        validateForm(form);

        Library library = new Library();
        applyForm(library, form);

        return libraryRepository.save(library);
    }

    public Library update(Long libraryId, LibraryForm form) {
        validateForm(form);

        Library library = findById(libraryId);
        applyForm(library, form);

        return libraryRepository.save(library);
    }

    private void validateForm(LibraryForm form) {
        if (form.getDescription() == null || form.getDescription().isBlank()) {
            throw new IllegalArgumentException("Der Name der Bibliothek darf nicht leer sein.");
        }

        if (form.getShowLibraryFrom() != null && form.getShowLibraryTo() != null
                && form.getShowLibraryTo().isBefore(form.getShowLibraryFrom())) {
            throw new IllegalArgumentException("'Anzeigen bis' darf nicht vor 'Anzeigen von' liegen.");
        }
    }

    private void applyForm(Library library, LibraryForm form) {
        library.setDescription(form.getDescription().trim());
        library.setRemark(form.getRemark());
        library.setShowLibraryFrom(form.getShowLibraryFrom());
        library.setShowLibraryTo(form.getShowLibraryTo());
        library.setIsPublic(Boolean.TRUE.equals(form.getIsPublic()));
    }

    public void deleteLibrary(Long libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bibliothek nicht gefunden: " + libraryId));

        documentRepository.deleteByLibrary_Id(libraryId);
        libraryRepository.delete(library);
    }
}
