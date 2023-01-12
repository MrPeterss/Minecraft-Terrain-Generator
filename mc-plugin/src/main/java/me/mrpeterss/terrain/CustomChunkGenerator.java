package me.mrpeterss.terrain;

import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator {

    public int[][] heights;
    private int max_height;
    private int min_height;

    public CustomChunkGenerator(HashMap<int[], int[][]> heights_map) throws IOException {

        int[][] tiles = new int[heights_map.keySet().size()][];
        tiles = heights_map.keySet().toArray(tiles);

        int[] maxXY = {0, 0};
        for (int[] tile : tiles) {
            if (tile[0] > maxXY[0]) maxXY[0] = tile[0];
            if (tile[1] > maxXY[1]) maxXY[1] = tile[1];
        }

        int[] minXY = tiles[0];
        for (int[] tile : tiles) {
            if (tile[0] < minXY[0]) minXY[0] = tile[0];
            if (tile[1] < minXY[1]) minXY[1] = tile[1];
        }

        System.out.println("Max X and Y: " + maxXY[0]+", "+maxXY[1]);
        System.out.println("Min X and Y: " + minXY[0]+", "+minXY[1]);

        heights = new int[(maxXY[1]-minXY[1]+1)*512][(maxXY[0]-minXY[0]+1)*512];

        max_height = 0;
        min_height = 0;

        for(int[] pos: heights_map.keySet()) {
            System.out.println("Current Pos " + pos[0]+", " + pos[1]);
            int[][] current = heights_map.get(pos);

            int correctx = pos[0]-minXY[0];
            int correcty = pos[1]-minXY[1];


            for (int x = 0; x < current.length; x++) {
                for (int y = 0; y < current[0].length; y++) {
                    int height = current[y][x];
                    heights[512*correcty+y][512*correctx+x] = height;
                    if (height > max_height) max_height = height;
                    if (height < min_height) min_height = height;
                }
            }
        }
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }


    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int rawX = x * chunkX;
                int rawZ = z * chunkZ;
                chunkData.setBlock(x, getBlockHeight(heights, rawX, rawZ, min_height, max_height), z, Material.GRASS_BLOCK);
            }
        }
    }

    public boolean shouldGenerateBedrock() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateNoise() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }


    private int getBlockHeight(int[][] array, int x, int z, int min, int max) {
        if (z >= array.length || z < 0 || x >= array[z].length || x < 0) return 40;

        int real_height = array[z][x];
        return scale(real_height, min, max, 64, 256);
    }

    private int scale(int originalNumber, int minOriginal, int maxOriginal, int minScaled, int maxScaled ) {
        return (originalNumber - minOriginal) * (maxScaled - minScaled) / (maxOriginal - minOriginal) + minScaled;
    }
}
