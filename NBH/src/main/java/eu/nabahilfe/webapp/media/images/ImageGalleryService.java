package eu.nabahilfe.webapp.media.images;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ImageGalleryService {

    private final GalleryRepository galleryRepository;
    private final GalleryImageRepository galleryImageRepository;
    private final ImageProcessingService imageProcessingService;

    public ImageGalleryService(
            GalleryRepository galleryRepository,
            GalleryImageRepository galleryImageRepository,
            ImageProcessingService imageProcessingService) {

        this.galleryRepository = galleryRepository;
        this.galleryImageRepository = galleryImageRepository;
        this.imageProcessingService = imageProcessingService;
    }


    @Transactional
    public void uploadImages(
            Long galleryId,
            List<MultipartFile> files) {

        Gallery gallery = galleryRepository.findById(galleryId).orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND, "Galerie nicht gefunden"));

        boolean needsCoverImage = galleryImageRepository.countByGallery_Id(galleryId) == 0;

        for (MultipartFile file : files) {

            ProcessedImage processed = imageProcessingService.process(file);

            Image image = new Image();

            image.setGallery(gallery);
            image.setFileName(resolveFileName(file));
            image.setContentType("image/webp");
            image.setImage(processed.imageData());
            image.setThumbnail(processed.thumbnailData());
            image.setImageWidth(processed.width());
            image.setImageHeight(processed.height());
            image.setImageSize(processed.imageData().length);
            image.setIsGalleryCover(needsCoverImage);
            needsCoverImage = false;

            galleryImageRepository.save(image);
        }
    }


    private String resolveFileName(MultipartFile file) {
        String original = file.getOriginalFilename();

        if (original == null || original.isBlank()) {
            return "upload.webp";
        }

        String trimmed = original.trim();
        int slash = Math.max(trimmed.lastIndexOf('/'), trimmed.lastIndexOf('\\'));
        String baseName = slash >= 0 ? trimmed.substring(slash + 1) : trimmed;

        if (baseName.isBlank()) {
            return "upload.webp";
        }

        if (baseName.toLowerCase().endsWith(".webp")) {
            return baseName;
        }

        int dot = baseName.lastIndexOf('.');
        if (dot > 0) {
            baseName = baseName.substring(0, dot);
        }

        return baseName + ".webp";
    }


    @Transactional(readOnly = true)
    public List<GalleryImageInfo> findImages(Long galleryId) {
        return galleryImageRepository.findImageInfos(galleryId);
    }


    @Transactional(readOnly = true)
    public byte[] getThumbnail(Long galleryId,Long imageId) {
        return galleryImageRepository.findThumbnailData(galleryId, imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public byte[] getImage(Long galleryId, Long imageId) {

        return galleryImageRepository.findImageData(galleryId, imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
