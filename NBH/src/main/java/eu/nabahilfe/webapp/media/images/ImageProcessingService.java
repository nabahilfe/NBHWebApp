package eu.nabahilfe.webapp.media.images;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service
public class ImageProcessingService {

    private static final int MAX_IMAGE_SIZE = 1920;
    private static final int MAX_THUMBNAIL_SIZE = 400;

    /**
     * Maximale Anzahl Pixel eines hochgeladenen Bildes.
     *
     * Die Prüfung erfolgt VOR der eigentlichen Bilddekodierung.
     *
     * 25 MP entsprechen z.B. ungefähr:
     *
     * 6000 x 4166 Pixel
     */
    private static final long MAX_PIXEL_COUNT = 25_000_000L;

    /**
     * WebP Qualität:
     *
     * 0.0 = maximale Kompression / schlechte Qualität
     * 1.0 = minimale Kompression / hohe Qualität
     */
    private static final float WEBP_QUALITY = 0.90f;


    /**
     * Verarbeitet ein hochgeladenes Bild.
     *
     * Es werden zwei WebP-Versionen erzeugt:
     *
     * - maximal 1920 px für die Darstellung
     * - maximal 400 px als Thumbnail
     */
    public ProcessedImage process(MultipartFile file) {

        validate(file);

        try {

            /*
             * Zuerst nur die Dimensionen des Bildes bestimmen.
             *
             * Das vollständige Bild wird hierbei noch NICHT
             * als BufferedImage in den Heap geladen.
             */
            ImageDimensions dimensions = readDimensions(file);

            /*
             * Schutz gegen extrem große Bilder.
             *
             * Diese Prüfung passiert vor der eigentlichen
             * Bildverarbeitung.
             */
            validateDimensions(dimensions, file.getOriginalFilename());

            /*
             * 1920er WebP erzeugen.
             */
            byte[] imageData =
                    createWebP(file, MAX_IMAGE_SIZE);

            /*
             * Thumbnail erzeugen.
             *
             * Das Original wird hierfür erneut vom InputStream
             * gelesen, aber nicht vorher als BufferedImage
             * vollständig in unserem Code gehalten.
             */
            byte[] thumbnailData =
                    createWebP(file, MAX_THUMBNAIL_SIZE);

            /*
             * Die tatsächlichen Dimensionen der erzeugten
             * 1920er WebP-Version bestimmen.
             */
            ImageDimensions resultDimensions =
                    readDimensions(imageData);

            return new ProcessedImage(
                    imageData,
                    thumbnailData,
                    resultDimensions.width(),
                    resultDimensions.height());

        } catch (ImageTooLargeException e) {

            /*
             * Das ist ein erwarteter Fehler:
             * Das hochgeladene Bild ist zu groß.
             */
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    e);

        } catch (UnsupportedImageFormatException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dateiformat wird nicht unterstützt. " +
                            "Bitte JPG, PNG oder WebP verwenden.",
                    e);

        } catch (MissingWebPWriterException e) {

            /*
             * Das ist kein Fehler des Benutzers,
             * sondern eine Server-Konfiguration.
             */
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Server-Konfiguration unvollständig: " +
                            "WebP-Writer ist nicht verfügbar.",
                    e);

        } catch (IOException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Bilddatei ist beschädigt oder konnte " +
                            "nicht gelesen werden.",
                    e);

        } catch (Exception e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Bild konnte nicht verarbeitet werden. " +
                            "Bitte ein anderes Bild versuchen.",
                    e);
        }
    }


    /**
     * Grundlegende Prüfung der Multipart-Datei.
     */
    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Leere Bilddatei");
        }
    }


    /**
     * Erzeugt eine WebP-Version direkt aus dem Upload-Stream.
     *
     * Es wird bewusst NICHT zuerst ImageIO.read() aufgerufen.
     *
     * Thumbnailator übernimmt:
     *
     * - Decodierung
     * - Skalierung
     * - Seitenverhältnis
     * - EXIF-Orientation
     */
    private byte[] createWebP(
            MultipartFile file,
            int maxSize) throws IOException {

        try (InputStream input = file.getInputStream()) {

            BufferedImage image =
                    Thumbnails.of(input)
                            .size(maxSize, maxSize)
                            .keepAspectRatio(true)
                            .useExifOrientation(true)
                            .asBufferedImage();

            try {

                return toWebP(image);

            } finally {

                /*
                 * BufferedImage kann relativ viel Heap belegen.
                 *
                 * Explizites flush() gibt die von der
                 * BufferedImage-Struktur verwendeten Ressourcen frei.
                 */
                image.flush();
            }
        }
    }


    /**
     * Ermittelt die Dimensionen eines Bildes, ohne das Bild
     * als vollständiges BufferedImage zu dekodieren.
     */
    private ImageDimensions readDimensions(MultipartFile file) throws IOException {

        try (InputStream input = file.getInputStream()) {

            var imageInput = ImageIO.createImageInputStream(input);

            if (imageInput == null) {
                throw new UnsupportedImageFormatException("Bild konnte nicht gelesen werden");
            }

            try (imageInput) {

                Iterator<javax.imageio.ImageReader> readers =
                        ImageIO.getImageReaders(imageInput);

                if (!readers.hasNext()) {

                    throw new UnsupportedImageFormatException(
                            "Datei ist kein unterstütztes Bild");
                }

                javax.imageio.ImageReader reader =
                        readers.next();

                try {

                    reader.setInput(
                            imageInput,
                            true,
                            true);

                    int width = reader.getWidth(0);
                    int height = reader.getHeight(0);

                    return new ImageDimensions(
                            width,
                            height);

                } finally {

                    reader.dispose();
                }
            }
        }
    }


    /**
     * Ermittelt die Dimensionen eines bereits erzeugten
     * Bild-Byte-Arrays.
     */
    private ImageDimensions readDimensions(
            byte[] data) throws IOException {

        try (InputStream input =
                     new ByteArrayInputStream(data)) {

            var imageInput =
                    ImageIO.createImageInputStream(input);

            if (imageInput == null) {

                throw new UnsupportedImageFormatException(
                        "Bild konnte nicht gelesen werden");
            }

            try (imageInput) {

                Iterator<javax.imageio.ImageReader> readers =
                        ImageIO.getImageReaders(imageInput);

                if (!readers.hasNext()) {

                    throw new UnsupportedImageFormatException(
                            "Datei ist kein unterstütztes Bild");
                }

                javax.imageio.ImageReader reader =
                        readers.next();

                try {

                    reader.setInput(
                            imageInput,
                            true,
                            true);

                    int width = reader.getWidth(0);
                    int height = reader.getHeight(0);

                    return new ImageDimensions(
                            width,
                            height);

                } finally {

                    reader.dispose();
                }
            }
        }
    }


    /**
     * Prüft die maximale Anzahl Pixel.
     *
     * Wichtig:
     * Die Multiplikation erfolgt als long, damit es bei sehr
     * großen Dimensionen keinen Integer Overflow gibt.
     */
    private void validateDimensions(ImageDimensions dimensions, String imgName) {

        long pixelCount = (long) dimensions.width() * dimensions.height();

        if (pixelCount > MAX_PIXEL_COUNT) {
            throw new ImageTooLargeException(
                    "Das Bild -> " + imgName + " <- ist zu groß. " + "Maximal erlaubt sind " + (MAX_PIXEL_COUNT / 1_000_000) + " Megapixel. " +
                            "Das hochgeladene Bild hat " + pixelCount / 1_000_000 + " Megapixel.");
        }
    }


    /**
     * Kodiert ein BufferedImage als WebP.
     */
    private byte[] toWebP(
            BufferedImage image) throws IOException {

        Iterator<ImageWriter> writers =
                ImageIO.getImageWritersByFormatName("webp");

        if (!writers.hasNext()) {

            throw new MissingWebPWriterException(
                    "Kein WebP ImageWriter verfügbar");
        }

        ImageWriter writer = writers.next();

        try {

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();

            try (ImageOutputStream imageOutput =
                         ImageIO.createImageOutputStream(output)) {

                writer.setOutput(imageOutput);

                ImageWriteParam param =
                        writer.getDefaultWriteParam();

                if (param.canWriteCompressed()) {

                    param.setCompressionMode(
                            ImageWriteParam.MODE_EXPLICIT);

                    String[] types =
                            param.getCompressionTypes();

                    if (types != null) {

                        for (String type : types) {

                            if ("Lossy".equalsIgnoreCase(type)) {

                                param.setCompressionType(type);
                                break;
                            }
                        }
                    }

                    param.setCompressionQuality(
                            WEBP_QUALITY);
                }

                writer.write(
                        null,
                        new IIOImage(
                                image,
                                null,
                                null),
                        param);

                imageOutput.flush();
            }

            return output.toByteArray();

        } finally {

            writer.dispose();
        }
    }


    private record ImageDimensions(
            int width,
            int height) {
    }


    private static class ImageTooLargeException
            extends RuntimeException {

        private static final long serialVersionUID = 1L;

        ImageTooLargeException(String message) {
            super(message);
        }
    }


    private static class UnsupportedImageFormatException
            extends IOException {

        private static final long serialVersionUID = 1L;

        UnsupportedImageFormatException(String message) {
            super(message);
        }
    }


    private static class MissingWebPWriterException
            extends IOException {

        private static final long serialVersionUID = 1L;

        MissingWebPWriterException(String message) {
            super(message);
        }
    }
}