package eu.nabahilfe.webapp.media.images;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
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
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
public class ImageProcessingService {

    private static final int MAX_IMAGE_SIZE = 1920;
    private static final int MAX_THUMBNAIL_SIZE = 400;

    /**
     * WebP Qualität:
     *
     * 0.0 = maximale Kompression / schlechte Qualität
     * 1.0 = minimale Kompression / hohe Qualität
     */
    private static final float WEBP_QUALITY = 0.90f;

    public ProcessedImage process(MultipartFile file) {

        validate(file);

        try {
            byte[] originalData = file.getBytes();
            BufferedImage original = readImage(originalData);
            int orientation = readOrientation(originalData);
            BufferedImage oriented = applyOrientation(original, orientation);
            BufferedImage image = resize(oriented, MAX_IMAGE_SIZE);
            BufferedImage thumbnail = resize(oriented, MAX_THUMBNAIL_SIZE);
            byte[] imageData = toWebP(image);
            byte[] thumbnailData = toWebP(thumbnail);

            return new ProcessedImage(
                    imageData,
                    thumbnailData,
                    image.getWidth(),
                    image.getHeight());

        } catch (UnsupportedImageFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dateiformat wird nicht unterstuetzt. Bitte JPG, PNG oder WebP verwenden.", e);
        } catch (MissingWebPWriterException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Server-Konfiguration unvollstaendig: WebP-Writer ist nicht verfuegbar.", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Bilddatei ist beschaedigt oder konnte nicht gelesen werden.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Bild konnte nicht verarbeitet werden. Bitte anderes Bild versuchen.", e);
        }
    }


    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Leere Bilddatei");
        }

        /*
         * Wir verlassen uns absichtlich nicht auf
         * MultipartFile.getContentType().
         *
         * Das tatsächliche Bild wird später mit ImageIO
         * dekodiert.
         */
    }


    private BufferedImage readImage(byte[] data) throws IOException {

        ImageIO.scanForPlugins();

        try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {

            BufferedImage image = ImageIO.read(input);

            if (image == null) {
                throw new UnsupportedImageFormatException("Datei ist kein unterstuetztes Bild");
            }

            return image;
        }
    }


    private int readOrientation(byte[] data) {

        try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {

            Metadata metadata = ImageMetadataReader.readMetadata(input);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType( ExifIFD0Directory.class);

            if (directory != null&& directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                return directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }

        } catch (Exception ignored) {
            /*
             * Kein EXIF bzw. keine Orientation.
             * Das ist völlig in Ordnung.
             */
        }

        return 1;
    }


    private BufferedImage applyOrientation(BufferedImage image, int orientation) {

        int width = image.getWidth();
        int height = image.getHeight();

        AffineTransform transform;

        int newWidth;
        int newHeight;

        switch (orientation) {

            case 2: // Horizontal spiegeln
                transform = new AffineTransform(-1, 0, 0, 1, width, 0);
                newWidth = width;
                newHeight = height;
                break;

            case 3: // 180°
                transform = new AffineTransform(-1, 0, 0, -1, width, height);
                newWidth = width;
                newHeight = height;
                break;

            case 4: // Vertikal spiegeln
                transform = new AffineTransform(1, 0, 0, -1, 0, height);
                newWidth = width;
                newHeight = height;
                break;

            case 5: // Transpose
                transform = new AffineTransform(0, 1, 1, 0, 0, 0);
                newWidth = height;
                newHeight = width;
                break;

            case 6: // 90° clockwise
                transform = new AffineTransform(0, 1, -1, 0, height, 0);
                newWidth = height;
                newHeight = width;
                break;

            case 7: // Transverse
                transform = new AffineTransform(0, -1, -1, 0,height, width);
                newWidth = height;
                newHeight = width;
                break;

            case 8: // 270° clockwise
                transform = new AffineTransform(0, -1, 1, 0, 0, width);
                newWidth = height;
                newHeight = width;
                break;

            default:
                return image;
        }

        BufferedImage result =
                new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        AffineTransformOp operation =
                new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

        operation.filter(image, result);

        return result;
    }


    private BufferedImage resize(BufferedImage image, int maxSize) throws IOException {

        int width = image.getWidth();
        int height = image.getHeight();

        /*
         * Niemals hochskalieren.
         */
        if (width <= maxSize && height <= maxSize) {
            return image;
        }

        return Thumbnails.of(image)
                .size(maxSize, maxSize)
                .keepAspectRatio(true)
                .asBufferedImage();
    }


    private byte[] toWebP(BufferedImage image) throws IOException {

        ImageIO.scanForPlugins();

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");

        if (!writers.hasNext()) {
            throw new MissingWebPWriterException("Kein WebP ImageWriter verfuegbar");
        }

        ImageWriter writer = writers.next();

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            try (ImageOutputStream imageOutput = ImageIO.createImageOutputStream(output)) {

                writer.setOutput(imageOutput);

                ImageWriteParam param = writer.getDefaultWriteParam();

                if (param.canWriteCompressed()) {

                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

                    /*
                     * TwelveMonkeys stellt bei WebP
                     * entsprechende Compression Types bereit.
                     *
                     * Wir verwenden Lossy, wenn verfügbar.
                     */
                    String[] types = param.getCompressionTypes();

                    if (types != null) {

                        for (String type : types) {
                            if ("Lossy".equalsIgnoreCase(type)) {
                                param.setCompressionType(type);
                                break;
                            }
                        }
                    }

                    param.setCompressionQuality(WEBP_QUALITY);
                }

                writer.write(null, new IIOImage(image, null, null), param);

                imageOutput.flush();
            }

            return output.toByteArray();

        }
        finally {
            writer.dispose();
        }
    }


    private static class UnsupportedImageFormatException extends IOException {
        private static final long serialVersionUID = 1L;

        UnsupportedImageFormatException(String message) {
            super(message);
        }
    }


    private static class MissingWebPWriterException extends IOException {
        private static final long serialVersionUID = 1L;

        MissingWebPWriterException(String message) {
            super(message);
        }
    }
}