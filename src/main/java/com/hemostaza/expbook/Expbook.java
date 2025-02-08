package com.hemostaza.expbook;

import com.hemostaza.expbook.listeners.UseExperienceBook;
import com.hemostaza.expbook.listeners.WriteBookListener;
import com.hemostaza.updateChecker.UpdateChecker;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class Expbook extends JavaPlugin {

    private final NamespacedKey key = new NamespacedKey(this,"expValue");

    @Override
    public void onEnable() {


        new UpdateChecker(this,122221);

        this.saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", configFile, List.of());
        } catch (IOException e) {
            e.printStackTrace();
        }

        reloadConfig();

        getServer().getPluginManager().registerEvents(new WriteBookListener(this), this);
        getServer().getPluginManager().registerEvents(new UseExperienceBook(this), this);

        PluginCommand command = getCommand("expbook");
        if(command!=null){
            ExpBookCommands mc = new ExpBookCommands(this);
            command.setExecutor(mc);
        }

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public NamespacedKey getKey(){
        return key;
    }
}
