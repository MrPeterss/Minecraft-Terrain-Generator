package me.mrpeterss.terrainGen.generation;

import me.mrpeterss.terrainGen.generation.populators.TreePopulator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes) {

        ChunkData chunkData = createChunkData(world);

        SimplexOctaveGenerator caveGen = new SimplexOctaveGenerator(new Random(world.getSeed()), 16);
        caveGen.setScale(0.03);

        // Set the height of the position relative to the heightmap
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int rawX = x + 16 * chunkX;
                int rawZ = z + 16 * chunkZ;
                int maxHeight = getBlockHeight(heights, rawX, rawZ, min_height, max_height);
                for (int y = -64; y <= maxHeight; y++) {
                    if (y == maxHeight) {
                        chunkData.setBlock(x, y, z, Material.GRASS_BLOCK);
                    } else if (caveGen.noise(rawX, y, rawZ, 0.5, 0.25) > 0.5) {
                        chunkData.setBlock(x, y, z, Material.AIR);
                    } else {
                        // generate diamonds
                        if (y < 16 && Math.random() < 0.001) {
                            generateOreVein(caveGen, world, x, y, z, Material.DIAMOND_ORE, 2, 9,
                                0.7, chunkData, chunkX, chunkZ);
                        }
                        chunkData.setBlock(x, y, z, Material.STONE);
                    }
                }
            }
        }

        /*
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (getBlockHeight(heights, x + 16 * chunkX, z + 16 * chunkZ, min_height, max_height) >
                    getBlockHeight(heights, (x-1) + 16 * chunkX, z + 16 * chunkZ, min_height, max_height)) {
                    for (int y = getBlockHeight(heights, x + 16 * chunkX, z + 16 * chunkZ, min_height, max_height) - 1;
                         y > getBlockHeight(heights, (x-1) + 16 * chunkX, z + 16 * chunkZ, min_height, max_height); y--)
                        chunkData.setBlock(x, y, z, Material.DIRT);
                }
                if (getBlockHeight(heights, x + 16 * chunkX, z + 16 * chunkZ, min_height, max_height) >
                    getBlockHeight(heights, (x+1) + 16 * chunkX, z + 16 * chunkZ, min_height, max_height)) {
                    for (int y = getBlockHeight(heights, x + 16 * chunkX, z + 16 * chunkZ, min_height, max_height) - 1;
                         y > getBlockHeight(heights, (x+1) + 16 * chunkX, z + 16 * chunkZ, min_height, max_height); y--)
                        chunkData.setBlock(x, y, z, Material.DIRT);
                }
                if (getBlockHeight(heights, x + 16 * chunkX, z + 16 * chunkZ, min_height, max_height) >
                    getBlockHeight(heights, x + 16 * chunkX, (z-1) + 16 * chunkZ, min_height, max_height)) {
                    for (int y = getBlockHeight(heights, x + 16 * chunkX, z + 16 * chunkZ, min_height, max_height) - 1;
                         y > getBlockHeight(heights, x + 16 * chunkX, (z-1) + 16 * chunkZ, min_height, max_height); y--)
                        chunkData.setBlock(x, y, z, Material.DIRT);
                }
                if (getBlockHeight(heights, x + 16 * chunkX, z + 16 * chunkZ, min_height, max_height) >
                    getBlockHeight(heights, x + 16 * chunkX, (z+1) + 16 * chunkZ, min_height, max_height)) {
                    for (int y = getBlockHeight(heights, x + 16 * chunkX, z + 16 * chunkZ, min_height, max_height) - 1;
                         y > getBlockHeight(heights, x + 16 * chunkX, (z+1) + 16 * chunkZ, min_height, max_height); y--)
                        chunkData.setBlock(x, y, z, Material.DIRT);
                }
            }
        }
        */

        return chunkData;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new TreePopulator());
    }

    private void generateOreVein(SimplexOctaveGenerator noise, World w, int x, int y, int z, Material oreMaterial,
                                 int minOre, int maxOre, double continuancePercent, ChunkData chunkData,
                                 int chunkX, int chunkZ) {
        int oreCount = 0;
        while (oreCount < maxOre) {
            // if the ore is above the minimum we want to use the percent to see if it continues
            if (oreCount >= minOre && Math.random() > continuancePercent) {
                // get out of here
                break;
            }

            int rawX = x + 16 * chunkX;
            int rawZ = z + 16 * chunkZ;

            // get the block at the current position
            chunkData.setBlock(x, y, z, oreMaterial);

            // get 1 of 6 random directions using BlockFace
            BlockFace direction = BlockFace.values()[new Random().nextInt(6)];
            Block toCheck = w.getBlockAt(rawX, y, rawZ).getRelative(direction);

            // update the x, y, and z
            x = toCheck.getX() - 16 * chunkX;
            x = Math.max(0, Math.min(15, x));
            y = toCheck.getY();
            z = toCheck.getZ() - 16 * chunkZ;
            z = Math.max(0, Math.min(15, z));

            // increment ore count
            oreCount++;
        }
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
