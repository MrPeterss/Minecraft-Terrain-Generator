package me.mrpeterss.terrain.commands;

import me.mrpeterss.terrain.CustomChunkGenerator;
import me.mrpeterss.terrain.Images;
import me.mrpeterss.terrain.Link;
import me.mrpeterss.terrain.Utils;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class generator implements @Nullable CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //is the sender a player
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        //correct amount of arguments?
        if (args.length!=1) {
            Utils.sendError(player,"Incorrect arguments. usage: /generator <key>");
            return false;
        }

        Link data;
        //Generate the link;
        try {
            data = new Link(args[0],player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //check if the key is valid. kick out if it is not
        if (data.selectionBBox == null) {
            return false;
        }

        //generate the images based on the data
        Images images = new Images(data);

        //create the world
        WorldCreator worldCreator = new WorldCreator("myWorld");
        try {
            worldCreator.generator(new CustomChunkGenerator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        World world = worldCreator.createWorld();

        //teleport the player to the world
        ((Player) sender).teleport(world.getSpawnLocation());

        return false;
    }
}
