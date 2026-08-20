package eu.nabahilfe.webapp.media.images;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final GalleryImageRepository galleryImageRepository;

    public GalleryService(GalleryRepository galleryRepository, GalleryImageRepository galleryImageRepository) {
        this.galleryRepository = galleryRepository;
        this.galleryImageRepository = galleryImageRepository;
    }

    @Transactional(readOnly = true)
    public Gallery findById(Long galleryId) {
        return galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Galerie nicht gefunden: " + galleryId));
    }


    @Transactional(readOnly = true)
    public List<Gallery> findAll() {
        return galleryRepository.findAllByOrderByCreatedAtDescIdDesc();
    }


    public Gallery create(GalleryForm form) {

        validateForm(form);

        Gallery gallery = new Gallery();
        applyForm(gallery, form);

        return galleryRepository.save(gallery);
    }


    public Gallery update(Long galleryId, GalleryForm form) {

        validateForm(form);

        Gallery gallery = findById(galleryId);
        applyForm(gallery, form);

        return galleryRepository.save(gallery);
    }


    private void validateForm(GalleryForm form) {

        if (form.getDescription() == null || form.getDescription().isBlank()) {
            throw new IllegalArgumentException("Die Beschreibung der Galerie darf nicht leer sein.");
        }

        if (form.getShowGalleryFrom() != null && form.getShowGalleryTo() != null
                && form.getShowGalleryTo().isBefore(form.getShowGalleryFrom())) {
            throw new IllegalArgumentException("'Anzeigen bis' darf nicht vor 'Anzeigen von' liegen.");
        }
    }


    private void applyForm(Gallery gallery, GalleryForm form) {
        gallery.setDescription(form.getDescription().trim());
        gallery.setRemark(form.getRemark());
        gallery.setGalleryDate(form.getGalleryDate());
        gallery.setShowGalleryFrom(form.getShowGalleryFrom());
        gallery.setShowGalleryTo(form.getShowGalleryTo());
        gallery.setIsPublic(Boolean.TRUE.equals(form.getIsPublic()));
    }


    public Gallery create(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Der Galeriename darf nicht leer sein.");
        }

        GalleryForm form = new GalleryForm();
        form.setDescription(name);
        form.setIsPublic(Boolean.FALSE);
        return create(form);
    }


    public void deleteGallery(Long galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Galerie nicht gefunden: " + galleryId));

        galleryImageRepository.deleteByGallery_Id(galleryId);
        galleryRepository.delete(gallery);
    }
}