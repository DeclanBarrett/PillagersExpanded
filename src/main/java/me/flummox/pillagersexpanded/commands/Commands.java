package me.flummox.pillagersexpanded.commands;

import me.flummox.pillagersexpanded.bases.Base;
import me.flummox.pillagersexpanded.DataManager;
import me.flummox.pillagersexpanded.patrols.PillagerPatrol;
import me.flummox.pillagersexpanded.eventHandlers.PatrolUpdateEvent;
import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Level;

import static org.bukkit.Bukkit.*;

public class Commands implements CommandExecutor {
    private int buildingID = 0;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("spawnpatrol")) {
                sender.sendMessage("Only Administrators can spawn patrols");
            }
            if (command.getName().equalsIgnoreCase("listpatrols")) {
                sender.sendMessage("Only Administrators can list patrols");
            }
            if (command.getName().equalsIgnoreCase("listbases")) {
                sender.sendMessage("Only Administrators can list bases");
            }

            /*
             * Finds the nearest patrol by going through the list of patrols and then checking the distance to the nearest manually
             */
            if (command.getName().equalsIgnoreCase("findpatrol")) {

                ArrayList<PillagerPatrol> pillagerPatrols = PillagerEventHandler.getInstance().getPatrols();
                if (pillagerPatrols.size() == 0) {
                    player.sendMessage("Sorry, there are no active pillager patrols");
                    return true;
                }

                PillagerPatrol closestPillagerPatrol = pillagerPatrols.get(0);
                if (pillagerPatrols.size() != 1) {
                    for (int x = 1; x < pillagerPatrols.size(); x++) {
                        if (pillagerPatrols.get(x).getCurrentLocation().distance(player.getLocation()) < closestPillagerPatrol.getCurrentLocation().distance(player.getLocation())) {
                            closestPillagerPatrol = pillagerPatrols.get(x);
                        }
                    }
                }


                player.sendMessage("The closest patrol is at (" + closestPillagerPatrol.getCurrentLocation().getBlockX() + ", " + closestPillagerPatrol.getCurrentLocation().getBlockZ() + " )");
                player.sendMessage("It is led by " + closestPillagerPatrol.getLeaderName() + " at level " + closestPillagerPatrol.getLevel());
                player.sendMessage("The patrol is going to (" + closestPillagerPatrol.getDestinationLocation().getBlockX() + ", " + closestPillagerPatrol.getDestinationLocation().getBlockZ() + " )");
                //"Spawn PillagerPatrol Executed");
                return true;
            }

            /*
             * Finds the nearest patrol by going through the list of digital pillager outposts and then checking the distance to the nearest manually
             */
            if (command.getName().equalsIgnoreCase("findbase")) {

                ArrayList<Base> pillagerBases = PillagerEventHandler.getInstance().getBases();
                if (pillagerBases.size() == 0) {
                    player.sendMessage("Sorry, there are no active pillager bases");
                    return true;
                }


                Base closestPillagerOutpost = pillagerBases.get(0);

                if (pillagerBases.size() != 1) {
                    for (int x = 1; x < pillagerBases.size(); x++) {
                        if (pillagerBases.get(x).getLocation().distance(player.getLocation()) < closestPillagerOutpost.getLocation().distance(player.getLocation())) {
                            closestPillagerOutpost = pillagerBases.get(x);
                        }
                    }
                }

                player.sendMessage("The closest outpost is at (" + closestPillagerOutpost.getLocation().getBlockX() + ", " + closestPillagerOutpost.getLocation().getBlockZ() + " )");
                player.sendMessage("The level of the outpost is " + closestPillagerOutpost.getLevel());
                return true;
                //"Spawn PillagerPatrol Executed");
            }
        } else {
            if (command.getName().equalsIgnoreCase("spawnpatrol")) {
                if (args.length >= 2) {
                    getLogger().log(Level.INFO, "Command creating patrol");
                    PillagerEventHandler.getInstance().createPatrol(
                            Integer.parseInt(args[0]),
                            Integer.parseInt(args[1]));
                    PatrolUpdateEvent patrolUpdate = new PatrolUpdateEvent(1);
                    getPluginManager().callEvent(patrolUpdate);
                   //"Spawn PillagerPatrol Executed");
                }
            }
            if (command.getName().equalsIgnoreCase("listpatrols")) {
                getLogger().log(Level.INFO, "Listing Patrols:");
                for (PillagerPatrol patrol: PillagerEventHandler.getInstance().getPatrols()) {
                    getLogger().log(Level.INFO, String.format("The patrol led by %s is at level %d. It is at %d %d and going to %d %d", patrol.getLeaderName(), patrol.getLevel(), patrol.getCurrentLocation().getBlockX(), patrol.getCurrentLocation().getBlockZ(), patrol.getDestinationLocation().getBlockX(), patrol.getDestinationLocation().getBlockZ()));
                }

            }

            if (command.getName().equalsIgnoreCase("listbases")) {
                getLogger().log(Level.INFO, "Listing Bases:");
                for (Base base: PillagerEventHandler.getInstance().getBases()) {
                    getLogger().log(Level.INFO, String.format("The base at %d %d is at level %d and is type %s", base.getLocation().getBlockX(), base.getLocation().getBlockZ(), base.getLevel(), base.getBaseType()));
                }
            }
        }
        return true;
    }
}
