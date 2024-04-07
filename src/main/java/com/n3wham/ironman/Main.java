package com.n3wham.ironman;

import com.n3wham.ironman.Events.IronManEvents;
import com.n3wham.ironman.Events.PlayerEvents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.IntStream;

public class Main extends JavaPlugin {

    Runnable run = () -> {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isIronMan(player) && player.isFlying()) {
                player.getLocation().getWorld().playEffect(player.getLocation(), Effect.SMOKE, 5, 5);
            }
        }
    };

    @Override
    public void onEnable() {
        loadEvents();
        loadCommands();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, run, 60, 20);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void loadEvents() {
        new IronManEvents(this);
        new PlayerEvents(this);
    }

    public void loadCommands() {
        getCommand("im").setExecutor(new IronManCommand(this));
        getCommand("ironman").setExecutor(new IronManCommand(this));
    }

    public String getPrefix() {
        return ChatColor.GOLD + "[" + ChatColor.RED + "Iron Man" + ChatColor.GOLD + "] " + ChatColor.GRAY;
    }

    public boolean hasPermission(Player player, String perm) {
        return player.isOp() || player.hasPermission(perm);
    }

    public boolean isIronMan(Player player) {
        return getConfig().getBoolean("players." + player.getName() + ".enabled");
    }

    private void updateIronManEquipment(Player player, boolean enabled) {
        PlayerInventory playerInventory = player.getInventory();

        if (enabled) {
            updateArmour(player, createIronManArmour(true), playerInventory);
        } else {
            updateArmour(player, createIronManArmour(false), playerInventory);
        }

        player.updateInventory();
    }

    private void updateArmour(Player player, ItemStack[] Armour, PlayerInventory playerInventory) {
        String[] ArmourTypes = {"helm", "plate", "legs", "boots"};
        ItemStack[] currentArmour = {playerInventory.getHelmet(), playerInventory.getChestplate(), playerInventory.getLeggings(), playerInventory.getBoots()};

        IntStream.range(0, Armour.length).forEach(i -> {
            int slot = getConfig().getInt("players.%s.%s".formatted(player.getName(), ArmourTypes[i]));
            if (slot != 999) {
                playerInventory.setItem(slot, currentArmour[i]);
                playerInventory.setItem(i + 36, Armour[i]);
                getConfig().set("players." + player.getName() + "." + ArmourTypes[i], slot);
            }
        });
    }

    private ItemStack[] createIronManArmour(boolean isIron) {
        Color helmColor = isIron ? Color.fromRGB(255, 215, 0) : Color.fromRGB(0, 0, 0);
        Color chestplateColor = isIron ? Color.fromRGB(255, 0, 0) : Color.fromRGB(0, 0, 0);
        Color leggingsColor = isIron ? Color.fromRGB(255, 0, 0) : Color.fromRGB(0, 0, 0);
        Color bootsColor = isIron ? Color.fromRGB(255, 215, 0) : Color.fromRGB(0, 0, 0);

        return new ItemStack[]{
                createArmour(Material.IRON_HELMET, helmColor, "Iron Man Helmet"),
                createArmour(Material.IRON_CHESTPLATE, chestplateColor, "Iron Man Chestplate"),
                createArmour(Material.IRON_LEGGINGS, leggingsColor, "Iron Man Leggings"),
                createArmour(Material.IRON_BOOTS, bootsColor, "Iron Man Boots")
        };
    }

    private ItemStack createArmour(Material material, Color colour, String displayName) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(colour);
        meta.setDisplayName(ChatColor.GOLD + displayName);
        item.setItemMeta(meta);
        return item;
    }

    public void setIronMan(Player player, boolean enabled) {
        getConfig().set("players." + player.getName() + ".enabled", enabled);
        saveConfig();
        updateIronManEquipment(player, enabled);
    }

    public String getArmourSetting(Player player) {
        return getConfig().getString("players." + player.getName() + ".armour");
    }

    public void setArmourSetting(Player player, String setting) {
        getConfig().set("players." + player.getName() + ".armour", setting);
    }
}