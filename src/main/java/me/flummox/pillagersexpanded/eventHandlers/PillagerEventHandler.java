package me.flummox.pillagersexpanded.eventHandlers;

import me.flummox.pillagersexpanded.*;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class PillagerEventHandler implements Listener {
    private ArrayList<Patrol> patrols = new ArrayList<>();

    public ArrayList<Patrol> getPatrols() {
        return patrols;
    }

    public ArrayList<Outpost> getOutposts() {
        return outposts;
    }

    public Outpost getRandomOutpost() {
        int randomInt = (int) (Math.random() * outposts.size());
        return outposts.get(randomInt);
    }

    public Outpost getSpecificOutpost(int index) {
        for (Outpost outpost: outposts) {
            if (outpost.getOutpostID() == index) {
                return outpost;
            }
        }
        return null;
    }

    private ArrayList<Outpost> outposts = new ArrayList<>();

    protected PillagerEventHandler() {

    }

    private static class SingletonHolder {
        private final static PillagerEventHandler INSTANCE = new PillagerEventHandler();
    }

    public void setup(DataManager data, Server server) {
        //need to fill the patrols array with the patrols that already exist in data form
        if (data.getConfig().getConfigurationSection("currentPatrols") != null) {
            Set<String> keyName = data.getConfig().getConfigurationSection("currentPatrols").getKeys(false);

            for (String s : keyName) {
                Patrol patrol = new Patrol(data, Integer.parseInt(s));
                patrols.add(patrol);
                patrol.reloadOnSave();
            }
        }

        setupOutposts(data);

    }

    private void setupOutposts(DataManager data) {
        if (data.getConfig().getConfigurationSection("outposts") != null) {
            System.out.println("Attempting to store outposts to memory!");
            Set<String> keyName = data.getConfig().getConfigurationSection("outposts").getKeys(false);

            for (String s : keyName) {
                Outpost outpost = new Outpost(Integer.parseInt(s));
                outposts.add(outpost);
            }
        } else {
            ArrayList<Location> outpostLocations = PillagerExpandedHelper.getInstance().pillagerOutposts();
            for (int x = 0; x < outpostLocations.size(); x++) {
                new Outpost(x, outpostLocations.get(x));
            }
            setupOutposts(data);
        }
    }

    public void cleanUp() {
        for (Patrol patrol : patrols) {
            patrol.cleanOnDisable();
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
                patrol.despawn();
                patrols.remove(patrol);
                patrol.remove();
                break;
            }
        }
    }

    //Updates when patrol is updated
    @EventHandler
    public void onPatrolUpdate(PatrolUpdateEvent event) {

        new moveRunnable(event.getInterval(), Main.getPlugin(Main.class)).runTaskAsynchronously(Main.getPlugin(Main.class));

    }

    @EventHandler
    public void onUpgrade(UpgradeEvent event) {
        event.getOutpost().upgradeOutpost();
    }

    public void createPatrol(int x, int z) {
        patrols.add(new Patrol(x, z));
    }

}
