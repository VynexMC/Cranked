package org.mesmeralis.cranked.storage;

import org.bukkit.Bukkit;
import org.mesmeralis.cranked.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Storage {

    Main main;
    public Storage(Main main) {
        this.main = main;
    }

    private Connection connection;

    public boolean isConnected() {
        return (connection != null);
    }

    public void connectSQL() throws ClassNotFoundException, SQLException {
        String host = main.getConfig().getString("host");
        int port = main.getConfig().getInt("port");
        String database = main.getConfig().getString("database");
        String user = main.getConfig().getString("username");
        String pass = main.getConfig().getString("password");
        if(!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", user, pass);
        }
    }

    public void disconnectSQL() {
        if(isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                Bukkit.getLogger().info("Could not disconnect from the database.");
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
