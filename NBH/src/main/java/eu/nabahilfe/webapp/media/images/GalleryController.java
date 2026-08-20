package eu.nabahilfe.webapp.media.images;


import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.Duration;
import java.util.List;

@Controller
@RequestMapping("/gallery")
public class GalleryController {

    private final GalleryService galleryService;
    private final ImageGalleryService galleryImageService;

    public GalleryController(GalleryService galleryService, ImageGalleryService galleryImageService) {
        this.galleryService = galleryService;
        this.galleryImageService = galleryImageService;
    }


    @GetMapping("/list")
    public String listGalleries(Model model) {
        model.addAttribute("galleries", galleryService.findAll());
        return "media/images/list-galleries";
    }


    @GetMapping("/user-list")
    public String listGalleriesForUsers(Model model, Authentication authentication) {
        model.addAttribute("galleries", galleryService.findVisibleForUsers(isAuthenticated(authentication)));
        return "media/images/user-list-galleries";
    }


    @GetMapping("/new")
    public String newGallery(Model model) {
        if (!model.containsAttribute("galleryForm")) {
            model.addAttribute("galleryForm", new GalleryForm());
        }
        model.addAttribute("galleryId", null);
        return "media/images/detail-gallery";
    }


    @GetMapping("/{galleryId}/edit")
    public String editGallery(@PathVariable Long galleryId, Model model) {
        Gallery gallery = galleryService.findById(galleryId);

        GalleryForm form = new GalleryForm();
        form.setDescription(gallery.getDescription());
        form.setRemark(gallery.getRemark());
        form.setGalleryDate(gallery.getGalleryDate());
        form.setShowGalleryFrom(gallery.getShowGalleryFrom());
        form.setShowGalleryTo(gallery.getShowGalleryTo());
        form.setIsPublic(gallery.getIsPublic());

        model.addAttribute("galleryForm", form);
        model.addAttribute("galleryId", galleryId);
        return "media/images/detail-gallery";
    }


    @PostMapping
    public String createGallery(
            @ModelAttribute("galleryForm") @Valid GalleryForm galleryForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Bitte die Pflichtfelder korrekt ausfuellen.");
            return "media/images/detail-gallery";
        }

        try {
            Gallery gallery = galleryService.create(galleryForm);
            redirectAttributes.addFlashAttribute("successMessage", "Galerie '" + gallery.getDescription() + "' wurde erstellt.");
            return "redirect:/gallery/" + gallery.getId();
        }
        catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "media/images/detail-gallery";
        }
    }


    @PostMapping("/{galleryId}")
    public String updateGallery(
            @PathVariable Long galleryId,
            @ModelAttribute("galleryForm") @Valid GalleryForm galleryForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Bitte die Pflichtfelder korrekt ausfuellen.");
            model.addAttribute("galleryId", galleryId);
            return "media/images/detail-gallery";
        }

        try {
            Gallery gallery = galleryService.update(galleryId, galleryForm);
            redirectAttributes.addFlashAttribute("successMessage", "Galerie '" + gallery.getDescription() + "' wurde gespeichert.");
            return "redirect:/gallery/" + gallery.getId();
        }
        catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("galleryId", galleryId);
            return "media/images/detail-gallery";
        }
    }


    @PostMapping("/{galleryId}/delete")
    public String deleteGallery(@PathVariable Long galleryId, RedirectAttributes redirectAttributes) {
        try {
            galleryService.deleteGallery(galleryId);
            redirectAttributes.addFlashAttribute("successMessage", "Galerie wurde geloescht.");
        }
        catch (ResponseStatusException ex) {
            String message = ex.getReason() != null ? ex.getReason() : "Galerie konnte nicht geloescht werden.";
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Beim Loeschen ist ein unerwarteter Fehler aufgetreten.");
        }

        return "redirect:/gallery/list";
    }


    /**
     * Galerie anzeigen
     */
    @GetMapping("/{galleryId}")
    public String showGallery(@PathVariable Long galleryId, Model model) {

        Gallery gallery = galleryService.findById(galleryId);

        List<GalleryImageInfo> images = galleryImageService.findImages(galleryId);

        model.addAttribute("gallery", gallery);
        model.addAttribute("images",images);

        return "media/images/gallery";
    }


    @GetMapping("/user/{galleryId}")
    public String showGalleryForUsers(@PathVariable Long galleryId, Model model, Authentication authentication) {

        Gallery gallery = galleryService.findVisibleByIdForUsers(galleryId, isAuthenticated(authentication));
        List<GalleryImageInfo> images = galleryImageService.findImages(galleryId);

        model.addAttribute("gallery", gallery);
        model.addAttribute("images", images);
        model.addAttribute("readOnly", Boolean.TRUE);

        return "media/images/gallery";
    }


    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }


    /**
     * Bilder hochladen
     */
    @PostMapping("/{galleryId}/images")
    public String uploadImages(
            @PathVariable Long galleryId,
            @RequestParam List<MultipartFile> images,
            RedirectAttributes redirectAttributes) {

        try {
            galleryImageService.uploadImages(galleryId, images);
            redirectAttributes.addFlashAttribute("successMessage", "Bilder wurden erfolgreich hochgeladen.");
        }
        catch (ResponseStatusException ex) {
            String message = ex.getReason() != null ? ex.getReason() : "Bilder konnten nicht hochgeladen werden.";
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Beim Hochladen ist ein unerwarteter Fehler aufgetreten.");
        }

        return "redirect:/gallery/" + galleryId;
    }


    @PostMapping("/{galleryId}/image/{imageId}/delete")
    public String deleteImage(
            @PathVariable Long galleryId,
            @PathVariable Long imageId,
            RedirectAttributes redirectAttributes) {

        try {
            galleryImageService.deleteImage(galleryId, imageId);
            redirectAttributes.addFlashAttribute("successMessage", "Bild wurde gelöscht.");
        }
        catch (ResponseStatusException ex) {
            String message = ex.getReason() != null ? ex.getReason() : "Bild konnte nicht gelöscht werden.";
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Beim Löschen ist ein unerwarteter Fehler aufgetreten.");
        }

        return "redirect:/gallery/" + galleryId;
    }


    /**
     * Thumbnail ausliefern
     */
    @GetMapping("/{galleryId}/image/{imageId}/thumbnail")
    @ResponseBody
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long galleryId, @PathVariable Long imageId) {
        byte[] data = galleryImageService.getThumbnail(galleryId, imageId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/webp"))
                .cacheControl(CacheControl.maxAge(Duration.ofDays(30)))
                .body(data);
    }


    /**
     * Großes Bild ausliefern
     */
    @GetMapping("/{galleryId}/image/{imageId}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long galleryId,@PathVariable Long imageId) {
        byte[] data =galleryImageService.getImage(galleryId, imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/webp"))
                .cacheControl(CacheControl.maxAge(Duration.ofDays(30)))
                .body(data);
    }
}