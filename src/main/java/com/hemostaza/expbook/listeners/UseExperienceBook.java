package com.hemostaza.expbook.listeners;

import com.hemostaza.expbook.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.logging.Logger;

public class UseExperienceBook implements Listener {

    Logger l;
    public UseExperienceBook(){
        l = Bukkit.getLogger();
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!Utils.isValidUse(player,event,true,false)){
            l.info("Not valid use");
            return;
        }
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta bookMeta = itemInHand.getItemMeta();
        if(bookMeta==null || bookMeta.getLore() == null){
            l.info("Book meta or lore is null");
            return;
        }
        if(!Utils.isValidMeta(player,"Book with written experience")){
            l.info("not valid meta");
            return;
        }
        String str = bookMeta.getLore().getFirst();
        int experience = 0;
        try{
            experience = Integer.parseInt(str.replaceAll("[^0-9]", ""));
        }catch (NumberFormatException e){
            Bukkit.getLogger().info("Something went wrong");
            return;
        }
        if(experience==0) return;
        l.info("Exp given "+experience);
        ExperienceOrb expOrb = (ExperienceOrb)player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
        expOrb.setExperience(experience);

        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

    }
}
