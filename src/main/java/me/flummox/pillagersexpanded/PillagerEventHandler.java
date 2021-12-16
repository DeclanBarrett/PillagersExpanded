package me.flummox.pillagersexpanded;

import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.*;

public class PillagerEventHandler implements Listener {

    private static Server server;
    private static DataManager data;
    private ArrayList<Patrol> patrols = new ArrayList<>();

    protected PillagerEventHandler() {

    }

    private static class SingletonHolder {
        private final static PillagerEventHandler INSTANCE = new PillagerEventHandler();
    }

    public void setup(DataManager data, Server server) {
        this.data = data;
        this.server = server;
        //need to fill the patrols array with the patrols that already exist in data form
        if (data.getConfig().getConfigurationSection("currentPatrols") != null) {
            Set<String> keyName = data.getConfig().getConfigurationSection("currentPatrols").getKeys(false);

            for (String s : keyName) {
                patrols.add(new Patrol(data, Integer.parseInt(s)));
            }
        }
    }

    public static PillagerEventHandler getInstance() {
        return PillagerEventHandler.SingletonHolder.INSTANCE;
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent event) {
        //Go through all patrols and if despawned then upgrade the patrol
        for (Patrol patrol : patrols) {
            if (patrol.isDespawned()) {
                patrol.upgradePatrol();
            }
        }
    }

    @EventHandler
    public void onPillagerDied(EntityDeathEvent event) {
        //Check whether the pillager is in the spawned patrol as the leader
        for (Patrol patrol: patrols) {
            if (patrol.isSpawned() && patrol.getPatrolLeader() == event.getEntity()) {
                patrol.setPatrolLeader(null);
                patrols.remove(patrol);
                patrol.remove();
                break;
            }
        }
    }

    //Updates when patrol is updated
    @EventHandler
    public void onPatrolUpdate(PatrolUpdateEvent event) {

        new moveRunnable(patrols, event.getInterval(), Main.getPlugin(Main.class)).runTaskAsynchronously(Main.getPlugin(Main.class));

    }

    public void createPatrol(int x, int z) {
        patrols.add(new Patrol(x, z));
    }
}
