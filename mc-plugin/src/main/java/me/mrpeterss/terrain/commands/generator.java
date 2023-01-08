package me.mrpeterss.terrain.commands;

import me.mrpeterss.terrain.CustomChunkGenerator;
import me.mrpeterss.terrain.Main;
import me.mrpeterss.terrain.utils.Data.Images;
import me.mrpeterss.terrain.utils.Data.Link;
import me.mrpeterss.terrain.utils.HeightHandler;
import me.mrpeterss.terrain.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class generator implements @Nullable CommandExecutor {
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

        Link data;
        //generate the data;
        try { data = new Link(args[0], player); }
        catch (IOException e) { throw new RuntimeException(e); }

        //check if the key is valid. kick out if it is not
        if (data.selectionBBox == null) return false;

        //initialize the images class
        Images images = new Images(data);

        //generate the in between tiles
        ArrayList<int[]> tiles = images.getTiles();

        //generate the images, pass in all tiles, zoom level, and the requester (Player)
        this.getImages(tiles, data.selectionZoomLvl, (Player) sender);

        //after this code is complete, it will call the second method
        return false;
    }

    public void generatePost(HashMap<int[], BufferedImage> imgMap, Player p) {

        //create a map of each image and its heights
        HashMap<int[], int[][]> heights = new HashMap<>();

        for (int[] image: imgMap.keySet()) {
            HeightHandler heightHandler = new HeightHandler(imgMap.get(image));
            heights.put(image, heightHandler.getHeight());
        }

        //create the world
        WorldCreator worldCreator = new WorldCreator("myWorld");

        //try to add the generator
        try { worldCreator.generator(new CustomChunkGenerator(heights)); }
        catch (IOException e) { throw new RuntimeException(e); }

        World world = worldCreator.createWorld();

        //teleport the player to the world
        p.teleport(world.getSpawnLocation());
    }

    private int task;
    public void getImages(ArrayList<int[]> tileList, int zoom, Player requester) {

        HashMap<int[], BufferedImage> images = new HashMap<>();
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            int i = 0;
            @Override
            public void run() {
                //get tile from index
                int[] current = tileList.get(i);

                // try to add the tile to the image map
                try { images.put(current, Utils.getTileImg(current[0],current[1], zoom)); }
                catch (IOException e) { throw new RuntimeException(e); }



                //increment index
                i++;

                // check if the index is out of bounds
                if (tileList.size()<=i) {
                    cancelTask(images, requester);
                }

            }
        }, 0L, 5L);
    }

    public void cancelTask(HashMap<int[], BufferedImage> images, Player p) {
        generatePost(images, p);
        Bukkit.getScheduler().cancelTask(task);
    }
}
