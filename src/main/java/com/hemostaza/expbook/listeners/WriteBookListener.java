package com.hemostaza.expbook.listeners;

import com.hemostaza.expbook.Expbook;
import com.hemostaza.expbook.ExperienceUtils;
import com.hemostaza.expbook.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Logger;

public class WriteBookListener implements Listener {

    Expbook plugin;
    Logger l;
    public WriteBookListener(Expbook plugin){
        this.plugin = plugin;
        l = Bukkit.getLogger();
    }

    @EventHandler
    public void onSignBook(PlayerEditBookEvent event){
        Player player = event.getPlayer();
        l.info("Book is edited");
        if(!event.isSigning()){
            l.info("is not signing book");
            return;
        }
        BookMeta bookMeta = event.getNewBookMeta();
        String page = bookMeta.getPage(1);
        String neededLine = page.split(System.lineSeparator())[0];
        if(neededLine.isEmpty()) {
            l.info("Neded line is empty: "+neededLine);
            return;
        }
        String trim = neededLine.replaceAll("[0-9]", "").trim();
        if(!(trim.equalsIgnoreCase("Save experience"))){
            l.info("Wrong line: "+trim);
            return;
        }
        int experience = 0;

        event.setCancelled(true);

        try {
            experience = Integer.parseInt(neededLine.replaceAll("[^0-9]", ""));
        }catch (NumberFormatException e){
            player.sendMessage("Wrong experience value in your book.");
            l.info("Wrong experience value!");
            return;
        }
        if(experience==0){
            l.info("Experience is 0");
            return;
        }
        if(!canSignBook(experience,player)) return;


        l.info("Kniga z expę");

        ItemStack expBook = ItemManager.createExperienceBook(experience);
        l.info("add: "+expBook);
        l.info(player.getInventory()+"");
        player.getInventory().addItem(expBook);

        ItemStack book = player.getInventory().getItemInMainHand();
        book.setAmount(1);
        l.info("remove: "+book);
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        l.info(player.getInventory().getItemInMainHand()+"");
        Bukkit.getScheduler().runTask(plugin, ()->{
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        });
    }

    public boolean canSignBook(int exp, Player player){
        if(ExperienceUtils.getExp(player)<exp){
            //player.sendMessage(config.getString("messages.not_enough_exp"));
            player.sendMessage("Not enough experience");
            return false;
        }
        ExperienceUtils.changeExp(player,-exp);
        return true;
    }
}