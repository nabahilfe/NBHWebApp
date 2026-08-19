package eu.nabahilfe.webapp.media.images;

public record ProcessedImage(
        byte[] imageData,
        byte[] thumbnailData,
        int width,
        int height) {
}