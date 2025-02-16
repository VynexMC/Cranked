package org.mesmeralis.cranked.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.checkerframework.checker.units.qual.C;
import org.mesmeralis.cranked.Main;
import org.mesmeralis.cranked.managers.GameManager;
import org.mesmeralis.cranked.storage.SQLGetter;
import org.mesmeralis.cranked.utils.ColourUtils;

import java.util.Objects;
import java.util.Random;

public class PlayerKillListener implements Listener {

    public GameManager manager;
    public SQLGetter data;
    public Main main;
    Random randomLoc = new Random();

    public PlayerKillListener(Main main) {
        this.main = main;
        this.manager = main.gameManager;
        this.data = main.data;
    }

    @EventHandler
    public void onPlayerKillEvent(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if (manager.isGameRunning) {
            data.addDeaths(killed.getUniqueId(), 1);
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(main.prefix + "&c" + killed.getName() + "&e was killed by &a" + killer.getName() + "&e."));
            data.addKills(killer.getUniqueId(), 1);
            manager.activeKills.put(killer.getPlayer(), manager.activeKills.get(killer.getPlayer()) + 1);
            this.manager.gameKills.put(killer.getPlayer(), this.manager.gameKills.get(killer) + 1);
            data.addPoints(killer.getUniqueId(), 5);
            Bukkit.getServer().broadcastMessage(ColourUtils.colour(main.prefix + "&c" + killed.getName() + " &eblew up!"));
            if (this.main.map.get(killed.getUniqueId()).points > 3) {
                data.addPoints(killed.getUniqueId(), -3);
            }
            manager.activeKills.put(killed.getPlayer(), 0);
            int locNumber = randomLoc.nextInt(10) + 1;
            killed.teleport(Objects.requireNonNull(main.getConfig().getLocation("gamespawn." + locNumber)));
        }
    }
}
