package org.mesmeralis.cranked.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mesmeralis.cranked.Main;
import org.mesmeralis.cranked.managers.GameManager;
import org.mesmeralis.cranked.storage.SQLGetter;
import org.mesmeralis.cranked.utils.ColourUtils;

import java.util.Objects;
import java.util.Random;

public class PlayerDamageListener implements Listener {

    public GameManager manager;
    public SQLGetter data;
    public Main main;
    Random randomLoc = new Random();
    public PlayerDamageListener (Main main) {
        this.main = main;
        this.manager = main.gameManager;
        this.data = main.data;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            if(manager.isGameRunning) {
                if(damager.getInventory().getItemInMainHand().getType() == Material.WOODEN_SWORD ||
                        damager.getInventory().getItemInMainHand().getType() == Material.STONE_SWORD
                || damager.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD
                || damager.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD
                || damager.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD) {


                }
            }
        }


        if (event.getDamager() instanceof Arrow) {
            if (event.getEntity() instanceof Player) {
                Player damaged = (Player) event.getEntity();
                Arrow arrow = (Arrow) event.getDamager();
                Player shooter = (Player) arrow.getShooter();
                Bukkit.getServer().broadcastMessage(ColourUtils.colour(main.prefix + "&c" + damaged.getName() + "&e was shot by &a" + shooter.getName() + "&e."));
                data.addKills(shooter.getUniqueId(), 1);
                this.manager.gameKills.put(shooter.getPlayer(), this.manager.gameKills.get(shooter) + 1);
                data.addDeaths(damaged.getUniqueId(), 1);
                data.addPoints(shooter.getUniqueId(), 5);
                if(this.main.map.get(damaged.getUniqueId()).points > 3) {
                    data.addPoints(damaged.getUniqueId(), -3);
                }
                int locNumber = randomLoc.nextInt(10) + 1;
                damaged.teleport(Objects.requireNonNull(main.getConfig().getLocation("gamespawn." + locNumber)));
                damaged.setHealth(20);
                shooter.setHealth(20);
                arrow.remove();
                damaged.playSound(Objects.requireNonNull(damaged.getPlayer()).getLocation(), Sound.ENTITY_PLAYER_DEATH, 10, 1);
                damaged.sendTitle(ColourUtils.colour("&c&lYOU DIED!"), "", 0, 20, 10);
                damaged.setArrowsInBody(0);
            }
        }


    }
}
