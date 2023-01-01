package me.mrpeterss.terrain.commands;

import me.mrpeterss.terrain.CustomChunkGenerator;
import org.bukkit.Location;
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
        if (!(sender instanceof Player)) return false;
        WorldCreator worldCreator = new WorldCreator("myWorld");
        try {
            worldCreator.generator(new CustomChunkGenerator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        World world = worldCreator.createWorld();

        ((Player) sender).teleport(world.getSpawnLocation());

        return false;
    }
}
