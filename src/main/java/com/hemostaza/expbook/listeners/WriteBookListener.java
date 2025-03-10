package com.hemostaza.expbook.listeners;

import com.hemostaza.expbook.Expbook;
import com.hemostaza.expbook.ExperienceUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WriteBookListener implements Listener {

    Expbook plugin;
    FileConfiguration config;
    Logger l;

    public WriteBookListener(Expbook plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        l = Bukkit.getLogger();
    }

    @EventHandler
    public void onSignBook(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        if (!event.isSigning()) {
            return;
        }
        BookMeta bookMeta = event.getNewBookMeta();
        String page = bookMeta.getPage(1);
        String lineInBook = page.split(System.lineSeparator())[0];
        if (lineInBook.isEmpty()) {
            return;
        }
        String trim = lineInBook.
                replaceAll("[0-9]+", "{*}").
                trim().
                toUpperCase();
        List<String> validLines = config.getStringList("expbook.neededLines");
        if (!validLines.contains(trim)) {
            return;
        }
        event.setCancelled(true);

        int experience;
        try {
            experience = Integer.parseInt(lineInBook.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            player.sendMessage("Wrong experience value in your book.");
            return;
        }
        if (experience == 0) {
            player.sendMessage("Experience value is 0.");
            return;
        }
        if (ExperienceUtils.getExp(player) < experience) {
            player.sendMessage("Not enough experience");
            return;
        }

        int percentage = config.getInt("takepercentage");
        int calculatedExp = experience;
        if (percentage > 0 && percentage <= 100) {
            float fvalue = (float) percentage / 100 * experience;
            calculatedExp -= (int) fvalue;
            //l.info("calcaulated experience: " + calculatedExp);
            if (calculatedExp <= 0) calculatedExp = 0;
            player.sendMessage("You lost " + percentage + "% of experience when saving it.");
        } else if (percentage == 0) {
            //l.info("Percentage is set to 0");
        } else {
            l.info("The percentage has wrong value, so it won't be used");
        }

        ItemStack expBook = createExperienceBook(calculatedExp);

        Bukkit.getScheduler().runTask(plugin, () -> {
            player.getInventory().setItemInMainHand(expBook);
            ExperienceUtils.changeExp(player, -experience);
        });
    }

    public ItemStack createExperienceBook(int experience) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        String name = config.getString("expbook.name");
        if (name == null) name = "Book with experience";

        meta.setDisplayName(name.replace("{*}", String.valueOf(experience)));

        NamespacedKey key = plugin.getKey();

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, experience);

        List<String> lore = new ArrayList<>();
        List<String> configList = config.getStringList("expbook.lore");
        //l.info(configList.toString());
        for (String line : configList) {
            lore.add(line.replace("{*}", String.valueOf(experience)));
        }
        if (lore.isEmpty()) lore.add("Book with " + experience + " experience potins");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        item.setItemMeta(meta);
        return item;
    }

    private int calculateExperience(int experience) {

        int percentage = config.getInt("takepercentage");
        if (percentage == 0) {
            l.info("Percentage is set to 0");
            return experience;
        }
        if (percentage > 0 && percentage <= 100) {
            float fvalue = (float) percentage / 100 * experience;
            experience -= (int) fvalue;
            l.info("calcaulated experience: " + experience);
            if (experience <= 0) experience = 0;
        } else {
            l.info("The percentage has wrong value, so it won't be used");
        }
        return experience;
    }
}