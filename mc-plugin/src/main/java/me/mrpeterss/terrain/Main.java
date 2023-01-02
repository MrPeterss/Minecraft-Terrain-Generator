package me.mrpeterss.terrain;

import me.mrpeterss.terrain.commands.generator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("generator").setExecutor(new generator());
    }

    @Override
    public void onDisable() {
        Bukkit.unloadWorld("myWorld", false);
    }
}
