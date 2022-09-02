package monte.carlo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Graph {
    private BufferedImage image;
    private Color color;
    public Graph(int width, int height){
        image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        color =  new Color(255, 0, 0);
    }

    public void setColor(int r, int g, int b){
        color = new Color(r,g,b);
    }
    public void plot(int x, int y){
        image.setRGB(x,y, color.getRGB());
    }

    public void saveJPG(String filename){
        File path = new File(filename);
        try{
            ImageIO.write(image, "JPG", path);
        }
        catch (IOException x){
            x.printStackTrace();
        }
    }
}
