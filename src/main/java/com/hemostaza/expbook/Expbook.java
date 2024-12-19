package com.hemostaza.expbook;

import com.hemostaza.expbook.listeners.UseExperienceBook;
import com.hemostaza.expbook.listeners.WriteBookListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Expbook extends JavaPlugin {

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new WriteBookListener(this), this);
        getServer().getPluginManager().registerEvents(new UseExperienceBook(this), this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
