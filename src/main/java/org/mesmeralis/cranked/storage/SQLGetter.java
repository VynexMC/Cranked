package org.mesmeralis.cranked.storage;

import org.bukkit.entity.Player;
import org.mesmeralis.cranked.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLGetter {
    private Main main;

    public SQLGetter(Main main) {
        this.main = main;
        this.createTable();
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = main.storage.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS cranked "
                    + "(name VARCHAR(100), uuid VARCHAR(100), kills INT(100), deaths INT(100), wins INT(100), points INT(100), PRIMARY KEY(name))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<Void> createPlayer(Player player) {
        return CompletableFuture.runAsync(() -> {
            try {
                UUID uuid = player.getUniqueId();
                if (!exists(uuid).join()) {
                    PreparedStatement ps2 = main.storage.getConnection().prepareStatement("INSERT IGNORE INTO cranked (name,uuid) VALUES (?,?)");
                    ps2.setString(1, player.getName());
                    ps2.setString(2, uuid.toString());
                    ps2.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Boolean> exists(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT * FROM cranked WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet results = ps.executeQuery();
                return results.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public CompletableFuture<Void> addPoints(UUID uuid, int points) {
        this.main.map.compute(uuid, (uid, pointss) -> {
            pointss.addPoints(points);
            return pointss;
        });
        return CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("UPDATE cranked SET POINTS=? WHERE uuid=?");
                ps.setInt(1, getPoints(uuid).join() + points);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> addWins(UUID uuid, int wins) {
        this.main.map.compute(uuid, (uid, winss) -> {
            winss.addWins(wins);
            return winss;
        });
        return CompletableFuture.runAsync(() -> {
                    try {
                        PreparedStatement ps = main.storage.getConnection().prepareStatement("UPDATE cranked SET wins=? WHERE uuid=?");
                        ps.setInt(1, getWins(uuid).join() + wins);
                        ps.setString(2, uuid.toString());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public CompletableFuture<Void> addDeaths(UUID uuid, int deaths) {
        this.main.map.compute(uuid, (uid, deathss) -> {
            deathss.addDeaths(deaths);
            return deathss;
        });
        return CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("UPDATE cranked SET deaths=? WHERE uuid=?");
                ps.setInt(1, getDeaths(uuid).join() + deaths);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> addKills(UUID uuid, int kills) {
        this.main.map.compute(uuid, (uid, killss) -> {
            killss.addKills(kills);
            return killss;
        });
        return CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("UPDATE cranked SET kills=? WHERE uuid=?");
                ps.setInt(1, getKills(uuid).join() + kills);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Integer> getPoints(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT points FROM cranked WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("points");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public CompletableFuture<Integer> getKills(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT kills FROM cranked WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("kills");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public CompletableFuture<Integer> getDeaths(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT deaths FROM cranked WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("deaths");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public CompletableFuture<Integer> getWins(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT wins FROM cranked WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("wins");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public CompletableFuture<UUID> getTopWins() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT uuid FROM cranked ORDER BY wins DESC LIMIT 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return UUID.fromString(rs.getString("uuid"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<UUID> getTopPoints() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = main.storage.getConnection().prepareStatement("SELECT uuid FROM cranked ORDER BY points DESC LIMIT 1");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return UUID.fromString(rs.getString("uuid"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
