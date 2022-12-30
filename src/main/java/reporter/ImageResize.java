package reporter;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;




public class ImageResize {
    public ImageResize() {
    }

    public static String FixTamano(File file_in) {
        String pathToFile = "";
        pathToFile = AjustarTamano(file_in);
        pathToFile = resizeImage(pathToFile);
        return pathToFile;
    }

    public static String AjustarTamano(File file_in) {
        String nuevaImagen = file_in.getAbsolutePath();

        try {
            BufferedImage image = ImageIO.read(file_in);
            nuevaImagen = nuevaImagen.replace(".png", "_2.png");
            File output = new File(nuevaImagen);
            OutputStream out = new FileOutputStream(output);
            ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("png").next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(2);
                param.setCompressionQuality(0.0F);
            }

            writer.write((IIOMetadata)null, new IIOImage(image, (List)null, (IIOMetadata)null), param);
            out.close();
            ios.close();
            writer.dispose();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return nuevaImagen;
    }

    private static String resizeImage(String pathToFile) {
        int IMG_WIDTH = 884;
        int IMG_HEIGHT = 1462;
        String location = pathToFile;

        try {
            BufferedImage originalImage = ImageIO.read(new File(pathToFile));
            int type = originalImage.getType() == 0 ? 2 : originalImage.getType();
            BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, (ImageObserver)null);
            g.dispose();
            location = location.replace("_2.png", "_3");
            ImageIO.write(resizedImage, "png", new File(location));
        } catch (Exception var9) {
        }

        return location;
    }
}
