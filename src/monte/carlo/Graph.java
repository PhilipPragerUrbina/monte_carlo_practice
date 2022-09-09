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
    private int width;
    private int height;
    private double offset_x;
    private double offset_y;
    private double resolution_x;
    private double resolution_y;

    //resolution is multiplier
    public Graph(double min_x, double min_y, double max_x, double max_y, double resolution_x, double resolution_y){
        this.resolution_x =resolution_x;
        this.resolution_y = resolution_y;
        this.offset_x = min_x;
        this.offset_y= min_y;
        width = (int)((max_x - min_x) * resolution_x)+1;
        height = (int)((max_y - min_y) * resolution_y)+1;

        image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        color =  new Color(255, 0, 0);
    }
    public Graph(Vector2 min, Vector2 max, Vector2 resolution){
      this(min.x,min.y, max.x, max.y,resolution.x, resolution.y);
    }

    public void setColor(int r, int g, int b){
        color = new Color(r,g,b);
    }

    //plot a point on image(screen space)
    public void pixel(int x, int y){
        if(x >= 0 && x < width && y >=0 && y < height){
            image.setRGB(x,height-y-1, color.getRGB());//y needs to be reversed for correct orientation
        }
    }
    public void pixel(double x, double y){
        pixel((int)(Math.round(x)),(int)(Math.round(y)));
    }
    public void pixel(Vector2 v){
        pixel(v.x,v.y);
    }

    Vector2 transformVector(Vector2 v){
        return new Vector2(transformX(v.x), transformY(v.y));
    }

    //get transformations, world space to screen space
    public double transformX(double x){
        return ((Math.round(x)-offset_x)*resolution_x);
    }
    public double transformY(double y){
        return ((Math.round(y)-offset_y)*resolution_y);
    }


    //plot a world space point
    //automatically transforms it and rounds it
    public void plot(double x, double y){
        pixel((int)((Math.round(x)-offset_x)*resolution_x),(int)((Math.round(y)-offset_y)*resolution_y));//y needs to be reversed for correct orientation
    }
    public void plot(Vector2 v){
       plot(v.x,v.y);
    }

    public void drawAxis(){
        for (int x = 0; x < width ; x++) {
            pixel(x ,(int)((0 - offset_y)*resolution_y) );
        }
        for (int y = 0; y < height ; y++) {
            pixel((int)((0 - offset_x) * resolution_x),y );
        }
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
