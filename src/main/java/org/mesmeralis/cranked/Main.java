package org.mesmeralis.cranked;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mesmeralis.cranked.commands.AdminCommand;
import org.mesmeralis.cranked.listeners.PlayerDamageListener;
import org.mesmeralis.cranked.listeners.PlayerListener;
import org.mesmeralis.cranked.managers.GameManager;
import org.mesmeralis.cranked.managers.RankManager;
import org.mesmeralis.cranked.papi.PapiExpansion;
import org.mesmeralis.cranked.storage.SQLGetter;
import org.mesmeralis.cranked.storage.Storage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public Storage storage;
    public SQLGetter data;
    public GameManager gameManager;
    public RankManager rankManager;
    public HashMap<UUID, PapiExpansion.Record> map = new HashMap<UUID, PapiExpansion.Record>();
    public UUID topWins;
    public UUID topPoints;
    public String prefix = this.getConfig().getString("prefix");
    public String spawnWorld = this.getConfig().getString("spawnworld");

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.storage = new Storage(this);
        try {
            storage.connectSQL();
            Bukkit.getLogger().info("Database connected successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info("Database was unable to connect.");
        }
        this.data = new SQLGetter(this);
        this.gameManager = new GameManager(this);
        this.initListeners();
        this.rankManager = new RankManager(this);
        this.getCommand("admin").setExecutor(new AdminCommand( this));
        rankManager.loadRanks();
        new PapiExpansion(this).register();
        Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            topWins = data.getTopWins().join();
            topPoints = data.getTopPoints().join();
        }, 0L,2400L);
        for (Player player : Bukkit.getOnlinePlayers()) {
            final int points = this.data.getPoints(player.getUniqueId()).join();
            final int kills = this.data.getKills(player.getUniqueId()).join();
            final int wins = this.data.getWins(player.getUniqueId()).join();
            final int deaths = this.data.getDeaths(player.getUniqueId()).join();
            PapiExpansion.Record record = new PapiExpansion.Record(points, kills, deaths, wins);
            this.map.put(player.getUniqueId(), record);
        }
    }

    @Override
    public void onDisable() {
        storage.disconnectSQL();
        Bukkit.getLogger().info("Database disconnected.");
    }

    private void initListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new PlayerDamageListener(this), this);
    }
}
