package me.flummox.pillagersexpanded.eventHandlers;

import me.flummox.pillagersexpanded.Main;
import me.flummox.pillagersexpanded.Patrol;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class moveRunnable extends BukkitRunnable {

    ArrayList<Patrol> patrols;
    int interval;
    Main plugin;

    public moveRunnable(ArrayList<Patrol> patrols, int interval, Main plugin) {
        this.patrols = patrols;
        this.interval = interval;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        //Loops through all the patrols
        System.out.println("Patrol Capturing");
        for (Patrol patrol: patrols) {
            //If the patrol is not already spawned then update its location and attempt to spawn it
            if (!patrol.isSpawned()) {
                //Update each patrols location if not spawned
                //patrol.move(event.getInterval());
                patrol.move(interval);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Patrol patrol: patrols) {
                    patrol.spawnPatrol();
                    patrol.save();
                }
            }
        }.runTask(plugin);
    }
}
