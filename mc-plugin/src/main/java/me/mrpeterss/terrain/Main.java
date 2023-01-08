package me.mrpeterss.terrain;

import me.mrpeterss.terrain.commands.generator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Plugin plugin;
    @Override
    public void onEnable() {
        getCommand("generator").setExecutor(new generator());
        plugin = this;
    }

    @Override
    public void onDisable() {
        Bukkit.unloadWorld("myWorld", false);
    }
}
