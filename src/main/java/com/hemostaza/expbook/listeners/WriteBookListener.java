package com.hemostaza.expbook.listeners;

import com.hemostaza.expbook.Expbook;
import com.hemostaza.expbook.ExperienceUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WriteBookListener implements Listener {

    Expbook plugin;
    FileConfiguration config;
    Logger l;
    public WriteBookListener(Expbook plugin){
        this.plugin = plugin;
        config = plugin.getConfig();
        l = Bukkit.getLogger();
    }

    @EventHandler
    public void onSignBook(PlayerEditBookEvent event){
        Player player = event.getPlayer();
        //l.info("Book is edited");
        if(!event.isSigning()){
            //l.info("is not signing book");
            return;
        }
        BookMeta bookMeta = event.getNewBookMeta();
        String page = bookMeta.getPage(1);
        String lineInBook = page.split(System.lineSeparator())[0];
        if(lineInBook.isEmpty()) {
            //l.info("Neded line is empty: "+neededLine);
            return;
        }
        String trim = lineInBook.
                replaceAll("[0-9]+", "{*}").
                trim().
                toUpperCase();
        List<String> validLines = config.getStringList("Expbook.neededLines");
//        l.info(trim);
//        l.info(validLines.toString());
        if(!validLines.contains(trim)){
            //l.info("Wrong line: "+trim);
            return;
        }
        event.setCancelled(true);

        int experience;
        try {
            experience = Integer.parseInt(lineInBook.replaceAll("[^0-9]", ""));
        }catch (NumberFormatException e){
            player.sendMessage("Wrong experience value in your book.");
//            l.info("Wrong experience value!");
            return;
        }
        if(experience==0){
//            l.info("Experience is 0");
            return;
        }
        if(!canSignBook(experience,player)) return;


        l.info("Kniga z expę");

        ItemStack expBook = createExperienceBook(experience);
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
    public ItemStack createExperienceBook(int experience) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        String name = config.getString("Expbook.name");
        if(name==null) name = "Book with experience";
        meta.setDisplayName(name.replace("{*}",String.valueOf(experience)));

        List<String> lore = new ArrayList<>();
        List<String> configList = config.getStringList("Expbook.lore");
        l.info(configList.toString());
        for (String line : configList){
            lore.add(line.replace("{*}",String.valueOf(experience)));
        }
        if(lore.isEmpty()) lore.add("Book with "+experience+" experience potins");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        item.setItemMeta(meta);
        return item;
    }
}