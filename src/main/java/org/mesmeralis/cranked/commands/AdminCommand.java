package org.mesmeralis.cranked.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mesmeralis.cranked.Main;
import org.mesmeralis.cranked.managers.GameManager;
import org.mesmeralis.cranked.utils.ColourUtils;

public class AdminCommand implements CommandExecutor {

    public GameManager manager;
    public Main main;
    public AdminCommand(Main main) {
        this.main = main;
        this.manager = main.gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(cmd.getLabel().equalsIgnoreCase("admin")) {
            if(args.length == 0) {
                return false;
            }
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("start")) {
                    manager.startGame();
                    sender.sendMessage(ColourUtils.colour(main.prefix + "&aCommand issued successfully."));
                }
                if(args[0].equalsIgnoreCase("setgamespawn")) {
                    sender.sendMessage(ColourUtils.colour(main.prefix + "&c/admin setgamespawn [1-10]"));
                }
                if(args[0].equalsIgnoreCase("setpoints")) {
                    sender.sendMessage(ColourUtils.colour(main.prefix + "/admin setpoints [user] [points]"));
                }
            }

            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("setgamespawn")) {
                    switch(args[1]) {
                        case "1":
                            main.getConfig().set("gamespawn.1", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "2":
                            main.getConfig().set("gamespawn.2", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "3":
                            main.getConfig().set("gamespawn.3", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "4":
                            main.getConfig().set("gamespawn.4", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "5":
                            main.getConfig().set("gamespawn.5", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "6":
                            main.getConfig().set("gamespawn.6", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "7":
                            main.getConfig().set("gamespawn.7", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "8":
                            main.getConfig().set("gamespawn.8", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "9":
                            main.getConfig().set("gamespawn.9", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        case "10":
                            main.getConfig().set("gamespawn.10", player.getLocation());
                            main.saveConfig();
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&aGame spawn set successfully."));
                            break;
                        default:
                            sender.sendMessage(ColourUtils.colour(main.prefix + "&cIncorrect spawn value."));
                            break;
                    }
                }
            }
        }

        return true;
    }

}
