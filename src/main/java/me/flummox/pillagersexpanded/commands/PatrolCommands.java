package me.flummox.pillagersexpanded.commands;

import me.flummox.pillagersexpanded.Outpost;
import me.flummox.pillagersexpanded.Patrol;
import me.flummox.pillagersexpanded.eventHandlers.PatrolUpdateEvent;
import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getPluginManager;

public class PatrolCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("spawnpatrol")) {
                sender.sendMessage("Only Administrators can spawn patrols");
               //"Spawn Patrol Executed");
            }

            if (command.getName().equalsIgnoreCase("findpatrol")) {

                ArrayList<Patrol> patrols = PillagerEventHandler.getInstance().getPatrols();
                if (patrols.size() == 0) {
                    player.sendMessage("Sorry, there are no active patrols");
                    return true;
                }

                Patrol closestPatrol = patrols.get(0);
                if (patrols.size() != 1) {
                    for (int x = 1; x < patrols.size(); x++) {
                        if (patrols.get(x).getCurrentLocation().distance(player.getLocation()) < closestPatrol.getCurrentLocation().distance(player.getLocation())) {
                            closestPatrol = patrols.get(x);
                        }
                    }
                }


                player.sendMessage("The closest patrol is at (" + closestPatrol.getCurrentLocation().getBlockX() + ", " + closestPatrol.getCurrentLocation().getBlockZ() + " )");
                player.sendMessage("It is led by " + closestPatrol.getLeaderName() + " at level " + closestPatrol.getLevel());
                player.sendMessage("The patrol is going to (" + closestPatrol.getDestinationLocation().getBlockX() + ", " + closestPatrol.getDestinationLocation().getBlockZ() + " )");
                //"Spawn Patrol Executed");
                return true;
            }

            if (command.getName().equalsIgnoreCase("findoutpost")) {

                ArrayList<Outpost> outposts = PillagerEventHandler.getInstance().getOutposts();
                if (outposts.size() == 0) {
                    player.sendMessage("Sorry, there are no active outposts");
                    return true;
                }


                Outpost closestOutpost = outposts.get(0);

                if (outposts.size() != 1) {
                    for (int x = 1; x < outposts.size(); x++) {
                        if (outposts.get(x).getOutpostLocation().distance(player.getLocation()) < closestOutpost.getOutpostLocation().distance(player.getLocation())) {
                            closestOutpost = outposts.get(x);
                        }
                    }
                }

                player.sendMessage("The closest outpost is at (" + closestOutpost.getOutpostLocation().getBlockX() + ", " + closestOutpost.getOutpostLocation().getBlockZ() + " )");
                player.sendMessage("The level of the outpost is " + closestOutpost.getLevel());
                return true;
                //"Spawn Patrol Executed");
            }
        } else {
            if (command.getName().equalsIgnoreCase("spawnpatrol")) {
                if (args.length >= 2) {
                    PillagerEventHandler.getInstance().createPatrol(
                            Integer.parseInt(args[0]),
                            Integer.parseInt(args[1]));
                    PatrolUpdateEvent patrolUpdate = new PatrolUpdateEvent(1);
                    getPluginManager().callEvent(patrolUpdate);
                   //"Spawn Patrol Executed");
                } else {
                   //"/spawnpatrol x z");
                }

            }
        }
        return true;
    }
}
