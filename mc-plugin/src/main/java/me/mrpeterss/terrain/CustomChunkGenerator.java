package me.mrpeterss.terrain;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

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

        System.out.println("Max X and Y: " + maxXY[0]+", "+maxXY[1]);

        heights = new int[(maxXY[1]+1)*512][(maxXY[1]+1)*512];

        max_height = 0;
        min_height = 0;

        for(int[] pos: heights_map.keySet()) {

            int[][] current = heights_map.get(pos);

            for (int x = 0; x < current.length; x++) {
                for (int y = 0; y < current[0].length; y++) {
                    int height = current[y][x];
                    heights[512*pos[1]+y][512*pos[0]+x] = height;
                    if (height > max_height) max_height = height;
                    if (height < min_height) min_height = height;
                }
            }
        }
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int height = getBlockHeight(heights,chunkX*16+x,chunkZ*16+z,min_height,max_height);
                for (int y = 0; y < height; y++) {

                    chunkData.setBlock(x, y, z, Material.STONE);
                }
            }
        }

        return chunkData;
    }

    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) {
        super.generateCaves(worldInfo, random, chunkX, chunkZ, chunkData);
    }

    private int getBlockHeight(int[][] array, int x, int z, int min, int max) {
        if (z >= array.length || z < 0 || x >= array[z].length || x < 0) return 0;

        int real_height = array[z][x];
        return scale(real_height, min, max, 64, 256);
    }

    private int scale(int originalNumber, int minOriginal, int maxOriginal, int minScaled, int maxScaled ) {
        return (originalNumber - minOriginal) * (maxScaled - minScaled) / (maxOriginal - minOriginal) + minScaled;
    }
}
