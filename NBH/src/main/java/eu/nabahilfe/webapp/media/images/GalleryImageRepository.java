package eu.nabahilfe.webapp.media.images;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GalleryImageRepository
        extends JpaRepository<Image, Long> {

    long countByGallery_Id(Long galleryId);

    Optional<Image> findByIdAndGallery_Id(Long imageId, Long galleryId);

    Optional<Image> findFirstByGallery_IdOrderByIdAsc(Long galleryId);

    @Query("""
        select new eu.nabahilfe.webapp.media.images.GalleryImageInfo(
            i.id,
            i.imageWidth,
            i.imageHeight
        )
        from Image i
        where i.gallery.id = :galleryId
        order by i.id
        """)
    List<GalleryImageInfo> findImageInfos(
            @Param("galleryId") Long galleryId);


    @Query("""
        select i.thumbnail
        from Image i
        where i.id = :imageId
          and i.gallery.id = :galleryId
        """)
    Optional<byte[]> findThumbnailData(
            @Param("galleryId") Long galleryId,
            @Param("imageId") Long imageId);


    @Query("""
        select i.image
        from Image i
        where i.id = :imageId
          and i.gallery.id = :galleryId
        """)
    Optional<byte[]> findImageData(
            @Param("galleryId") Long galleryId,
            @Param("imageId") Long imageId);
}