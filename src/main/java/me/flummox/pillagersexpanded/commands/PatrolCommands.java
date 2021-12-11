package me.flummox.pillagersexpanded.commands;

import me.flummox.pillagersexpanded.PillagerExpandedHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PatrolCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("spawnpatrol")) {
                sender.sendMessage("Only Administrators can spawn patrols");
                System.out.println("Spawn Patrol Executed");
            }
        } else {
            if (command.getName().equalsIgnoreCase("spawnpatrol")) {
                if (args.length >= 2) {
                    PillagerExpandedHelper.getInstance().createPatrol(
                            Integer.parseInt(args[0]),
                            Integer.parseInt(args[1]));
                    System.out.println("Spawn Patrol Executed");
                } else {
                    System.out.println("/spawnpatrol x z");
                }

            }
        }
        return true;
    }
}
