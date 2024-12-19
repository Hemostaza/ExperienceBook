package com.hemostaza.expbook.listeners;

import com.hemostaza.expbook.Expbook;
import com.hemostaza.expbook.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class UseExperienceBook implements Listener {

    FileConfiguration config;
    Logger l;
    public UseExperienceBook(Expbook plugin){
        config = plugin.getConfig();
        l = Bukkit.getLogger();
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!(Objects.equals(event.getHand(), EquipmentSlot.HAND))) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        int experience = getExperienceFromBook(itemInHand);

        if(experience==0){
            //l.info("It's not exp book");
            return;
        }
        //l.info("Exp given "+experience);
        ExperienceOrb expOrb = (ExperienceOrb)player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
        expOrb.setExperience(experience);

        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    }

    int getExperienceFromBook(ItemStack book){
        ItemMeta meta = book.getItemMeta();
        if(meta==null) return 0;
        List<String> lore = meta.getLore();
        if(lore == null) return 0;
        int experience = 0;
        for (String line : meta.getLore()){
            try{
                experience = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                break;
            }catch (NumberFormatException e){
                //l.info("Something went wrong");
            }
        }
        return experience;
    }

}
