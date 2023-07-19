package me.mrpeterss.terrainGen.utils;

import org.bukkit.Bukkit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeightHandler {

    private BufferedImage image;
    private int max_height;
    private int min_height;

    private int[][] heights;

    public HeightHandler(BufferedImage image) {
        this.image = image;
    }

    //add a vertical adjustment variable here
    public int[][] getHeight() {
        heights = new int[image.getHeight()][image.getWidth()];

        max_height = 0;
        min_height = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int height = getHeight(x,y);
                heights[y][x] = height;
                if (height>max_height) max_height = height;
                if (height<min_height) min_height = height;
            }
        }
        return heights;
    }

    private int getBlockHeight(int[][] array, int x, int z, int min, int max) {
        if (z >= array.length || z < 0 || x >= array[z].length || x < 0) return 0;

        int real_height = array[z][x];
        return scale(real_height, min, max, 64, 128);
    }

    private int scale(int originalNumber, int minOriginal, int maxOriginal, int minScaled, int maxScaled ) {
        return (originalNumber - minOriginal) * (maxScaled - minScaled) / (maxOriginal - minOriginal) + minScaled;
    }

    private int getHeight(int x, int z) {
        //if any of the coords are off
        if (x<0||x>=image.getWidth()||z<0||z>=image.getHeight()) return 0;

        int pixelColor = image.getRGB(x, z);
        int red = (pixelColor >> 16) & 0xff;
        int green = (pixelColor >> 8) & 0xff;
        int blue = pixelColor & 0xff;

        return (red * 256 + green + blue / 256) - 32768 ;
    }

    public void createImg() throws IOException {
        // Create a BufferedImage with the same size as the array
        int width = heights.length;
        int height = heights[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Get the graphics context for the image
        Graphics2D g2d = image.createGraphics();

        // Loop through the array and draw each element as a pixel on the image
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int value = getBlockHeight(heights,x,y,min_height,max_height);
                g2d.setColor(new Color(value, value, value));

                g2d.fillRect(x, y, 1, 1);
            }
        }

        // Save the image to a file
        File outputFile = new File(Bukkit.getServer().getWorldContainer()+"/greyscale_image.png");
        ImageIO.write(image, "png", outputFile);
    }
}
