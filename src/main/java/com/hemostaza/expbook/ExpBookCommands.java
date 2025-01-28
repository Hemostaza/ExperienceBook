package com.hemostaza.expbook;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ExpBookCommands implements CommandExecutor {

    private final Expbook plugin;
    private static FileConfiguration config;
    public ExpBookCommands(Expbook plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
//        if(!(sender instanceof Player player)){
//            sender.sendMessage("Only players can use this command");
//            return true;
//        }
        if(cmd.getName().equalsIgnoreCase("expbook")){
            sender.sendMessage("Valid lines to save experience in a book are: ");
            List<String> validLines = config.getStringList("expbook.neededLines");

            //Bukkit.getLogger().info(validLines.toString());
            for (String line : validLines){
                sender.sendMessage("- "+line.toLowerCase());
            }
        }
        return true;
    }
}
