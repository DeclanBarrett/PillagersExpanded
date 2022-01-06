package me.flummox.pillagersexpanded.eventHandlers;

import me.flummox.pillagersexpanded.DataManager;
import me.flummox.pillagersexpanded.Main;
import me.flummox.pillagersexpanded.Outpost;
import me.flummox.pillagersexpanded.Patrol;
import org.bukkit.scheduler.BukkitRunnable;

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

        System.out.println("Running the produce outpost patrol runnable");

        if (PillagerEventHandler.getInstance().getPatrols().size() > DataManager.getInstance().getConfig().getInt("patrols.total")) {
            return;
        }

        ArrayList<Outpost> outposts = PillagerEventHandler.getInstance().getOutposts();

        System.out.println("There are less patrols than the patrols total");
        for (Outpost outpost: outposts) {
            //If the patrol is not already spawned then update its location and attempt to spawn it
            if (outpost.isActive()) {
                //Update each patrols location if not spawned
                //patrol.move(event.getInterval());
                outpost.attemptProducePatrol();
            }
        }

        //Loops through all the patrols
        ArrayList<Patrol> patrols = PillagerEventHandler.getInstance().getPatrols();

        for (Patrol patrol: patrols) {
            //If the patrol is not already spawned then update its location and attempt to spawn it
            if (!patrol.isSpawned()) {
                //Update each patrols location if not spawned
                //patrol.move(event.getInterval());
                patrol.move(interval);
            } else {
                System.out.println("#############  SPAWNED PATROL CANNOT MOVE  ################");
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Patrol patrol: patrols) {
                    patrol.attemptArrive();
                    patrol.spawnPatrol();
                    patrol.save();
                }
            }
        }.runTask(plugin);
    }
}
