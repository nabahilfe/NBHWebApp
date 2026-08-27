package eu.nabahilfe.webapp.media.documents;

import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@Controller
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;
    private final DocumentLibraryService documentLibraryService;

    public LibraryController(LibraryService libraryService, DocumentLibraryService documentLibraryService) {
        this.libraryService = libraryService;
        this.documentLibraryService = documentLibraryService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @GetMapping("/list")
    public String listLibraries(Model model) {
        model.addAttribute("libraries", libraryService.findAll());
        return "media/documents/list-libraries";
    }

    @GetMapping("/user-list")
    public String listLibrariesForUsers(Model model, Authentication authentication) {
        model.addAttribute("libraries", libraryService.findVisibleForUsers(isAuthenticated(authentication)));
        return "media/documents/user-list-libraries";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @GetMapping("/new")
    public String newLibrary(Model model) {
        if (!model.containsAttribute("libraryForm")) {
            model.addAttribute("libraryForm", new LibraryForm());
        }
        model.addAttribute("libraryId", null);
        return "media/documents/detail-library";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @GetMapping("/{libraryId}/edit")
    public String editLibrary(@PathVariable Long libraryId, Model model) {
        Library library = libraryService.findById(libraryId);

        LibraryForm form = new LibraryForm();
        form.setDescription(library.getDescription());
        form.setRemark(library.getRemark());
        form.setShowLibraryFrom(library.getShowLibraryFrom());
        form.setShowLibraryTo(library.getShowLibraryTo());
        form.setIsPublic(library.getIsPublic());

        model.addAttribute("libraryForm", form);
        model.addAttribute("libraryId", libraryId);
        return "media/documents/detail-library";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    public String createLibrary(
            @ModelAttribute @Valid LibraryForm libraryForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Bitte die Pflichtfelder korrekt ausfüllen.");
            return "media/documents/detail-library";
        }

        try {
            Library library = libraryService.create(libraryForm);
            redirectAttributes.addFlashAttribute("successMessage", "Bibliothek '" + library.getDescription() + "' wurde erstellt.");
            return "redirect:/library/" + library.getId();
        }
        catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "media/documents/detail-library";
        }
    }

    @PostMapping("/{libraryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    public String updateLibrary(
            @PathVariable Long libraryId,
            @ModelAttribute @Valid LibraryForm libraryForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Bitte die Pflichtfelder korrekt ausfüllen.");
            model.addAttribute("libraryId", libraryId);
            return "media/documents/detail-library";
        }

        try {
            Library library = libraryService.update(libraryId, libraryForm);
            redirectAttributes.addFlashAttribute("successMessage", "Bibliothek '" + library.getDescription() + "' wurde gespeichert.");
            return "redirect:/library/list";
        }
        catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("libraryId", libraryId);
            return "media/documents/detail-library";
        }
    }

    @PostMapping("/{libraryId}/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER')")
    public String deleteLibrary(@PathVariable Long libraryId, RedirectAttributes redirectAttributes) {
        try {
            libraryService.deleteLibrary(libraryId);
            redirectAttributes.addFlashAttribute("successMessage", "Bibliothek wurde gelöscht.");
        }
        catch (ResponseStatusException ex) {
            String message = ex.getReason() != null ? ex.getReason() : "Bibliothek konnte nicht gelöscht werden.";
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Beim Löschen ist ein unerwarteter Fehler aufgetreten.");
        }

        return "redirect:/library/list";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @GetMapping("/{libraryId}")
    public String showLibrary(@PathVariable Long libraryId, Model model) {
        Library library = libraryService.findById(libraryId);
        List<LibraryDocumentInfo> documents = documentLibraryService.findDocuments(libraryId);

        model.addAttribute("library", library);
        model.addAttribute("documents", documents);

        return "media/documents/library";
    }

    @GetMapping("/user/{libraryId}")
    public String showLibraryForUsers(@PathVariable Long libraryId, Model model, Authentication authentication) {
        Library library = libraryService.findVisibleByIdForUsers(libraryId, isAuthenticated(authentication));
        List<LibraryDocumentInfo> documents = documentLibraryService.findDocuments(libraryId);

        model.addAttribute("library", library);
        model.addAttribute("documents", documents);
        model.addAttribute("readOnly", Boolean.TRUE);

        return "media/documents/library";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @PostMapping("/{libraryId}/documents")
    public String uploadDocuments(
            @PathVariable Long libraryId,
            @RequestParam List<MultipartFile> documents,
            RedirectAttributes redirectAttributes) {

        try {
            documentLibraryService.uploadDocuments(libraryId, documents);
            redirectAttributes.addFlashAttribute("successMessage", "Dokumente wurden erfolgreich hochgeladen.");
        }
        catch (ResponseStatusException ex) {
            String message = ex.getReason() != null ? ex.getReason() : "Dokumente konnten nicht hochgeladen werden.";
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Beim Hochladen ist ein unerwarteter Fehler aufgetreten.");
        }

        return "redirect:/library/" + libraryId;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EXECUTIVE_MEMBER', 'BOARD_MEMBER')")
    @PostMapping("/{libraryId}/document/{documentId}/delete")
    public String deleteDocument(
            @PathVariable Long libraryId,
            @PathVariable Long documentId,
            RedirectAttributes redirectAttributes) {

        try {
            documentLibraryService.deleteDocument(libraryId, documentId);
            redirectAttributes.addFlashAttribute("successMessage", "Dokument wurde gelöscht.");
        }
        catch (ResponseStatusException ex) {
            String message = ex.getReason() != null ? ex.getReason() : "Dokument konnte nicht gelöscht werden.";
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Beim Löschen ist ein unerwarteter Fehler aufgetreten.");
        }

        return "redirect:/library/" + libraryId;
    }

    @GetMapping("/{libraryId}/document/{documentId}")
    public ResponseEntity<byte[]> getDocument(
            @PathVariable Long libraryId,
            @PathVariable Long documentId,
            Authentication authentication) {
        // Enforce visibility for anonymous users to prevent URL guessing on private libraries.
        libraryService.findVisibleByIdForUsers(libraryId, isAuthenticated(authentication));

        Document document = documentLibraryService.findDocumentForDownload(libraryId, documentId);

        ContentDisposition disposition = ContentDisposition.inline()
                .filename(document.getFileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .cacheControl(CacheControl.maxAge(Duration.ofHours(4)))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(document.getDocumentData());
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
