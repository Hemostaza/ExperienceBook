package com.hemostaza.expbook;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ExpBookCommands implements CommandExecutor {

    private static FileConfiguration config;
    public ExpBookCommands(Expbook plugin) {
        config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(cmd.getName().equalsIgnoreCase("expbook")){
            int percentage = config.getInt("takepercentage");
            if(percentage>0&&percentage<=100){
                sender.sendMessage("You will lose "+percentage+"% of your saved experience.");
            }
            sender.sendMessage("Valid lines to save experience in a book are: ");
            List<String> validLines = config.getStringList("expbook.neededLines");

            for (String line : validLines){
                sender.sendMessage("- "+line.toLowerCase());
            }
        }
        return true;
    }
}
