package org.mesmeralis.cranked.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.mesmeralis.cranked.Main;
import org.mesmeralis.cranked.utils.ColourUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {
    public Main main;
    public boolean isGameRunning = false;
    public HashMap<Player, Integer> gameKills = new HashMap<>();
    public HashMap<Player, Integer> activeKills = new HashMap<>();

    public GameManager(Main main) {
        this.main = main;
    }

    public void startGame() {
        AtomicInteger counter = new AtomicInteger(6);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        int starting;
        int exploding;
        for (Player online : Bukkit.getOnlinePlayers()) {
            gameKills.put(online.getPlayer(), 0);
        }

        starting = scheduler.scheduleSyncRepeatingTask(main, () -> {
            counter.getAndDecrement();
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(main.prefix + "&eStarting in " + counter));
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online, Sound.UI_BUTTON_CLICK, 10, 1);
            }
        }, 0L, 20L);

        scheduler.scheduleSyncDelayedTask(main, () -> {
            scheduler.cancelTask(starting);
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendTitle(ColourUtils.colour("&b&lCRANKED"), "Custom coded.", 20, 60, 20);
                online.playSound(online, Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 1);
                main.data.createPlayer(online.getPlayer());
                online.setWalkSpeed(0.6F);
            }
            giveItems();
            isGameRunning = true;
            teleport();
        }, 100L);

        Random randomLoc = new Random();

        exploding = scheduler.scheduleSyncRepeatingTask(main, () -> {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (activeKills.getOrDefault(online, 0) == 0) {
                    online.playSound(online.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                    int locNumber = randomLoc.nextInt(10) + 1;
                    online.teleport(Objects.requireNonNull(main.getConfig().getLocation("gamespawn." + locNumber)));
                    online.getInventory().clear();
                    online.getInventory().setItem(0, gameSword);
                    Bukkit.getServer().broadcastMessage(ColourUtils.colour(main.prefix + "&c" + online.getName() + " &eblew up!"));
                    if (this.main.map.get(online.getUniqueId()).points > 3) {
                        main.data.addPoints(online.getUniqueId(), -3);
                        main.data.addDeaths(online.getUniqueId(), 1);
                    }
                    online.setHealth(20);
                    online.setWalkSpeed(0.6F);
                }
            }
        }, 100L, 600L);


        scheduler.scheduleSyncDelayedTask(main, () -> {
            scheduler.cancelTask(exploding);
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendTitle(ColourUtils.colour("&4&lGAME OVER"), ColourUtils.colour("&e" + getWinner().getName() + "&a won the game!"), 20, 60, 20);
                online.playSound(online, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                online.getInventory().clear();
                online.teleport(Bukkit.getServer().getWorld(main.spawnWorld).getSpawnLocation());
            }
            Player winner = this.getWinner();
            winner.playSound(winner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 5);
            main.data.addPoints(winner.getUniqueId(), 15);
            main.data.addWins(winner.getUniqueId(), 1);
            gameKills.clear();
            isGameRunning = false;
        }, 2400L);
    }

    ItemStack gameSword = new ItemStack(Material.WOODEN_SWORD, 1);

    public void giveItems() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().clear();
            online.getInventory().setItem(0, gameSword);
        }
    }

    public void teleport() {
        Random randomLoc = new Random();
        for (Player online : Bukkit.getOnlinePlayers()) {
            int locNumber = randomLoc.nextInt(10) + 1;
            online.teleport(Objects.requireNonNull(main.getConfig().getLocation("gamespawn." + locNumber)));
        }
    }

    public Player getWinner() {
        int highest = 0;
        Player winner = null;
        for (Player online : Bukkit.getOnlinePlayers()) {
            int amount = this.gameKills.getOrDefault(online.getPlayer(), 0);
            if (highest < amount) {
                highest = amount;
                winner = online;
            }
        }
        return winner;
    }

}
