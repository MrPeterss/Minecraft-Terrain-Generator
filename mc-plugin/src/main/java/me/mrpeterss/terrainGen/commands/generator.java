package me.mrpeterss.terrainGen.commands;

import me.mrpeterss.terrainGen.generation.CustomChunkGenerator;
import me.mrpeterss.terrainGen.Main;
import me.mrpeterss.terrainGen.utils.Data.Images;
import me.mrpeterss.terrainGen.utils.Data.Link;
import me.mrpeterss.terrainGen.utils.HeightHandler;
import me.mrpeterss.terrainGen.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class generator implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //is the sender a player
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        //correct amount of arguments?
        if (args.length != 1) {
            Utils.sendError(player, "Incorrect arguments. usage: /generator <key>");
            return false;
        }

        Link data = null;
        //generate the data;
        try { data = new Link(args[0], player); }
        catch (Exception e) {
            Utils.sendError(player, "Invalid key, ensure you are using the command generated from the website.");
            return false;
        }

        //initialize the images class
        Images images = new Images(data);

        //generate the in between tiles
        ArrayList<int[]> tiles = images.getTiles();
        System.out.println("Tiles: "+tiles.size());

        //generate the images, pass in all tiles, zoom level, and the requester (Player)
        this.getImages(tiles, data.selectionZoomLvl, (Player) sender);

        //after this code is complete, it will call the second method
        return false;
    }

    public void generateAfterImagesCreated(HashMap<int[], BufferedImage> imgMap, Player p) {

        //create a map of each image and its heights
        HashMap<int[], int[][]> heights = new HashMap<>();

        for (int[] image : imgMap.keySet()) {
            HeightHandler heightHandler = new HeightHandler(imgMap.get(image));
            heights.put(image, heightHandler.getHeight());
        }

        //create the generator
        CustomChunkGenerator generator = null;
        try {
            generator = new CustomChunkGenerator(heights);
        } catch (IOException e) {
            Utils.sendError(p, "An error occurred while generating the ChunkGenerator. Please report this on github");
            return;
        }

        //create the world
        WorldCreator worldCreator = new WorldCreator("HeightMapWorld");

        //try to add the generator
        worldCreator.generator(generator);
        //update the user
        Utils.sendMeh(p, "Generating world...");

        World world = worldCreator.createWorld();

        //teleport the player to the world
        p.teleport(world.getSpawnLocation());

        // all done!
        Utils.sendGood(p, "Finished Generating World Successfully! Enjoy!");
    }

    private int imagesTask;
    public void getImages(ArrayList<int[]> tileList, int zoom, Player requester) {

        HashMap<int[], BufferedImage> images = new HashMap<>();
        imagesTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            int i = 0;
            @Override
            public void run() {
                //get tile from index
                int[] current = tileList.get(i);

                // try to add the tile to the image map
                try { images.put(current, Utils.getTileImg(current[0],current[1], zoom)); }
                catch (IOException e) { throw new RuntimeException(e); }

                // update the user
                Utils.sendMeh(requester, "Generating height data... " + (int) ((double) i / (double) (tileList.size() - 1) * 100) + "%");

                //increment index
                i++;

                // check if the index is out of bounds
                if (tileList.size()<=i) {
                    cancelImagesTask(images, requester);
                }

            }
        }, 0L, 10L);
    }

    public void cancelImagesTask(HashMap<int[], BufferedImage> images, Player p) {
        generateAfterImagesCreated(images, p);
        Bukkit.getScheduler().cancelTask(imagesTask);
    }
}
