package me.mrpeterss.terrainGen.generation.populators;

import org.bukkit.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class TreePopulator extends BlockPopulator {
  @Override
  public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion) {
    if (random.nextInt(100) < 10) {
      putTree(worldInfo, random, chunkX, chunkZ, limitedRegion);
    }
    if (random.nextInt(100) < 10) {
      putTree(worldInfo, random, chunkX, chunkZ, limitedRegion);
    }
    if (random.nextInt(100) < 10) {
      putTree(worldInfo, random, chunkX, chunkZ, limitedRegion);
    }
  }

  public void putTree(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion lr) {
    int x = random.nextInt(16) + chunkX * 16;
    int z = random.nextInt(16) + chunkZ * 16;
    int y;
    for (y = worldInfo.getMaxHeight()-1; lr.getType(x, y, z) == Material.AIR; y--);
    lr.generateTree(new Location(lr.getWorld(), x, y, z), random, TreeType.TREE);
  }
}
