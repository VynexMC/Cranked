package org.mesmeralis.cranked.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesmeralis.cranked.Main;
import org.mesmeralis.cranked.utils.ColourUtils;

public class PapiExpansion extends PlaceholderExpansion {
    private final Main main;
    public PapiExpansion (Main main) {
        this.main = main;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cranked";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Mesmeralis";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("points")) {
            String points = String.valueOf(this.main.map.get(player.getUniqueId()).points);
            return ColourUtils.colour(points);
        }
        if (params.equalsIgnoreCase("kills")) {
            String kills = String.valueOf(this.main.map.get(player.getUniqueId()).kills);
            return ColourUtils.colour(kills);
        }
        if (params.equalsIgnoreCase("deaths")) {
            String deaths = String.valueOf(this.main.map.get(player.getUniqueId()).deaths);
            return ColourUtils.colour(deaths);
        }
        if (params.equalsIgnoreCase("wins")) {
            String wins = String.valueOf(this.main.map.get(player.getUniqueId()).wins);
            return ColourUtils.colour(wins);
        }
        if (params.equalsIgnoreCase("rank")) {
            int points = this.main.map.get(player.getUniqueId()).points;
            String rank = this.main.rankManager.getRankFromPoints(points);
            String prefix = this.main.rankManager.getRankPrefix(rank);
            return ColourUtils.colour(prefix);
        }
        if (params.equalsIgnoreCase("toppoints")) {
            return ColourUtils.colour(Bukkit.getOfflinePlayer(main.topPoints).getName());
        }
        if (params.equalsIgnoreCase("topwins")) {
            return ColourUtils.colour(Bukkit.getOfflinePlayer(main.topWins).getName());
        }

        return null;
    }

    public static class Record {
        public int points;
        public int kills;
        public int deaths;
        public int wins;

        public Record(int points, int kills, int deaths, int wins) {
            this.points = points;
            this.kills = kills;
            this.deaths = deaths;
            this.wins = wins;
        }

        public void addPoints(int points) {
            this.points += points;
        }
        public void addKills(int kills) {
            this.kills += kills;
        }
        public void addDeaths(int deaths) {
            this.deaths += deaths;
        }
        public void addWins(int wins) {
            this.wins += wins;
        }

    }

}
