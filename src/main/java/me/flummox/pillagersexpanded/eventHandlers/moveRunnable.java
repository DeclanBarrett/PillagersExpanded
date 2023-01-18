package me.flummox.pillagersexpanded.eventHandlers;

import me.flummox.pillagersexpanded.*;
import me.flummox.pillagersexpanded.bases.Base;
import me.flummox.pillagersexpanded.patrols.PillagerPatrol;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.logging.Level;

import static org.bukkit.Bukkit.*;

import java.util.ArrayList;

public class moveRunnable extends BukkitRunnable {

    int interval;
    Main plugin;

    public moveRunnable(int interval, Main plugin) {
        this.interval = interval;
        this.plugin = plugin;
    }

    @Override
    public void run() {

        ArrayList<Base> pillagerBases = PillagerEventHandler.getInstance().getBases();
        ArrayList<PillagerPatrol> pillagerPatrols = PillagerEventHandler.getInstance().getPatrols();

       //"There are less pillagerPatrols than the pillagerPatrols total");

        if (pillagerBases.size() == 0 && pillagerPatrols.size() < DataManager.getInstance().getConfig().getInt("patrols.total")) {
            int chance = (int) (Math.random() * DataManager.getInstance().getConfig().getInt("patrols.newoutofchance"));
            //"Attempting to Produce PillagerPatrol");
            int patrolRadius = (int) DataManager.getInstance().getConfig().getInt("patrols.patrolRadius");
            if (chance == 0) {
                int xLocation = (int) (Math.random() * 2 * patrolRadius - patrolRadius);
                int zLocation = (int) (Math.random() * 2 * patrolRadius - patrolRadius);
                PillagerEventHandler.getInstance().createPatrol(xLocation, zLocation);
            }
        }

        //Loops through all the pillagerPatrols
       //"[Pillagers Expanded] pillagerPatrols attempting to move");
        for (PillagerPatrol pillagerPatrol : pillagerPatrols) {
           //getLogger().log(Level.INFO, "[Pillagers Expanded] Moving a PillagerPatrol");
            //If the pillagerPatrol is not already spawned then update its location and attempt to spawn it
            if (!pillagerPatrol.isSpawned()) {
                //Update each pillagerPatrols location if not spawned
                //pillagerPatrol.move(event.getInterval());
                pillagerPatrol.move();
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (PillagerPatrol pillagerPatrol : pillagerPatrols) {

                    pillagerPatrol.attemptArriveAtDestination();
                }
            }
        }.runTask(plugin);
    }
}
