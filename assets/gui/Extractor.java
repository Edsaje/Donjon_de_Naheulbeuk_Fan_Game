import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Extractor {
    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File("main/GUI/GUI_4x.png"));
        // Top-left dark window seems to be a square. Let's find its size by looking at non-transparent pixels.
        int width = 0;
        for (int x = 0; x < img.getWidth(); x++) {
            if ((img.getRGB(x, 0) >> 24) == 0) { // transparent
                width = x;
                break;
            }
        }
        int height = 0;
        for (int y = 0; y < img.getHeight(); y++) {
            if ((img.getRGB(0, y) >> 24) == 0) {
                height = y;
                break;
            }
        }
        System.out.println("Dark window size: " + width + "x" + height);
        
        BufferedImage window = img.getSubimage(0, 0, width, height);
        ImageIO.write(window, "png", new File("window_4x.png"));
        
        // Let's also get the selection cursor. It's an arrow somewhere.
        // Instead, let's just use the dark window for now!
    }
}
