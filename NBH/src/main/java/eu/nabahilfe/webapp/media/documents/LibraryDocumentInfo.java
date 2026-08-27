package eu.nabahilfe.webapp.media.documents;

public record LibraryDocumentInfo(
        Long id,
        String fileName,
        int documentSize,
        String contentType,
        String description) {
}
