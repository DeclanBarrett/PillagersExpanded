package me.flummox.pillagersexpanded.commands;

import me.flummox.pillagersexpanded.eventHandlers.PatrolUpdateEvent;
import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
