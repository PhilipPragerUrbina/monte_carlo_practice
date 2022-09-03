package monte.carlo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//class for outputting visualizations
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
        image.setRGB(x,image.getHeight()-y-1, color.getRGB());//y needs to be reversed for correct orientation
    }
    public void plot(double x, double y){
        image.setRGB((int)Math.round(x),(int)Math.round(image.getHeight()-y)-1, color.getRGB());//y needs to be reversed for correct orientation
    }

    //save graph
    public void savePNG(String filename){
        File path = new File(filename);
        try{

            ImageIO.write(image, "PNG", path);
        }
        catch (IOException x){
            x.printStackTrace();
        }
    }
}
