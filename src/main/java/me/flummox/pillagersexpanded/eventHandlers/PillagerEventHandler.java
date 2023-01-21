package me.flummox.pillagersexpanded.eventHandlers;

import me.flummox.pillagersexpanded.*;
import me.flummox.pillagersexpanded.bases.Base;
import me.flummox.pillagersexpanded.bases.PillagerOutpost;
import me.flummox.pillagersexpanded.enums.BaseLocationPair;
import me.flummox.pillagersexpanded.enums.BaseType;
import me.flummox.pillagersexpanded.patrols.PillagerPatrol;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.*;
import java.util.logging.Level;

import static org.bukkit.Bukkit.*;

/**
 * Keeps track of meta data and lists of patrol and pillagerBases
 * Also adds all events to be requested by
 */
public class PillagerEventHandler implements Listener {
    private final ArrayList<PillagerPatrol> pillagerPatrols = new ArrayList<>();

    public ArrayList<PillagerPatrol> getPatrols() {
        return pillagerPatrols;
    }

    private final ArrayList<Base> pillagerBases = new ArrayList<>();


    public ArrayList<Base> getBases() {
        return pillagerBases;
    }

    /**
     * Gets a random base from the list of pillagerBases
     * @return A random digital Base which may be a castle or outpost
     */
    public Base getRandomBase() throws ArrayIndexOutOfBoundsException {
        if (pillagerBases.size() == 0) {
            throw new ArrayIndexOutOfBoundsException("There are no more bases to destroy");
        }
        int randomInt = (int) (Math.random() * pillagerBases.size());
        return pillagerBases.get(randomInt);
    }

    /**
     * Get a specific outpost from index
     * @param index index of the outpost
     * @return the outpost
     */
    public Base getSpecificBase(int index) {
        for (Base pillagerBase : pillagerBases) {
            if (pillagerBase.getBaseID() == index) {
                return pillagerBase;
            }
        }
        return null;
    }

    protected PillagerEventHandler() {

    }

    /**
     * Makes the pillager event handler a singleton
     */
    private static class SingletonHolder {
        private final static PillagerEventHandler INSTANCE = new PillagerEventHandler();
    }

    /**
     * Makes the singleton available to other classes
     * @return the instance of the pillager event handler
     */
    public static PillagerEventHandler getInstance() {
        return PillagerEventHandler.SingletonHolder.INSTANCE;
    }

    /**
     * Sets up the pillagerPatrols that were saved to the disk
     */
    public void setupExistingPatrols() {
        //need to fill the pillagerPatrols array with the pillagerPatrols that already exist in data form;

        try {
            if (DataManager.getInstance().getPatrols().getConfigurationSection("currentPatrols") != null) {
                Set<String> keyName = Objects.requireNonNull(DataManager.getInstance().getPatrols().getConfigurationSection("currentPatrols")).getKeys(false);

                for (String s : keyName) {
                    PillagerPatrol pillagerPatrol = new PillagerPatrol(Integer.parseInt(s));
                    pillagerPatrols.add(pillagerPatrol);
                    //"[Pillagers Expanded] Adding a pillagerPatrol to the cache of pillagerPatrols");
                    pillagerPatrol.reloadOnSave();
                }
            }
        } catch (NullPointerException e) {
            getLogger().log(Level.WARNING, "There are no current patrols listed");
        }


        //setupBases();

    }

    /**
     * Setup the plugin pillagerBases by gathering them from memory or get the pillager plugin pillagerBases
     */
    public void setupBases() {
        if (DataManager.getInstance().getConfig().getBoolean("allow.outposts") || DataManager.getInstance().getConfig().getBoolean("allow.castles")) {
            if (DataManager.getInstance().getBases().getConfigurationSection("base") != null) {
                //When bases are allowed and exist - Gather existing bases
                Set<String> keyName = Objects.requireNonNull(DataManager.getInstance().getBases().getConfigurationSection("base")).getKeys(false);
                //Create a digital base in the correct base type
                for (String s : keyName) {
                    int baseID = Integer.parseInt(s);
                    String baseString = "base." + s + ".";
                    BaseType baseType = BaseType.valueOf(DataManager.getInstance().getBases().getString(baseString + "baseType"));
                    Base pillagerBase;
                    switch(baseType) {
                        case OUTPOST:
                            pillagerBase = new PillagerOutpost(baseID);
                            break;
                        default:
                            pillagerBase = new PillagerOutpost(baseID);

                    }
                    //Register the new pillager base to use new events
                    getPluginManager().registerEvents(pillagerBase, Main.getInstance());
                    pillagerBases.add(pillagerBase);
                }
            } else {
                ArrayList<BaseLocationPair> outpostLocations = PillagerExpandedHelper.getInstance().pillagerBases();
                for (int baseID = 0; baseID < outpostLocations.size(); baseID++) {
                    // baseType = BaseType.valueOf(DataManager.getInstance().getConfig().getString(baseString + "baseType"));
                    Base pillagerBase;
                    //Add outpost to list of pillagerBases
                    switch(outpostLocations.get(baseID).getBaseType()) {
                        case OUTPOST:
                            pillagerBase = new PillagerOutpost(baseID, outpostLocations.get(baseID).getLocation());
                            break;
                        default:
                            pillagerBase = new PillagerOutpost(baseID, outpostLocations.get(baseID).getLocation());

                    }

                    getPluginManager().registerEvents(pillagerBase, Main.getInstance());
                    pillagerBases.add(pillagerBase);
                }
                //getServer().reload();
                // WHY WAS THIS SETUP RECURSIVELY!
                //setupBases();
            }
        }
    }

    /**
     * Despawn all pillagerPatrols that have spawned but remember they need respawning
     */
    public void cleanUp() {
        for (PillagerPatrol pillagerPatrol : pillagerPatrols) {
            pillagerPatrol.wipeSpawnedEntities();
            pillagerPatrol.saveOnExit();
        }
    }



    /**
     * Check if pillager patrols have been naturally despawned on each chunk unload event and trigger a pillager patrol upgrade
     * @param event the chunk unload event
     */
    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent event) {
        //Go through all pillagerPatrols and if despawned then upgrade the patrol
        for (PillagerPatrol pillagerPatrol : pillagerPatrols) {
            if (pillagerPatrol.isNaturallyDespawned()) {
                pillagerPatrol.upgradePatrol();
            }
        }
    }

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent event) {
        for (PillagerPatrol pillagerPatrol : pillagerPatrols) {
            pillagerPatrol.attemptSpawningPatrol();
        }
    }

    /**
     * Patrol was destroyed
     * Check whether the pillager is in the spawned patrol as the leader
     * and then destroy the patrol
     * @param event Entity death event
     */
    @EventHandler
    public void onPillagerDied(EntityDeathEvent event) {
        for (PillagerPatrol pillagerPatrol : pillagerPatrols) {
            if (pillagerPatrol.isSpawned() && pillagerPatrol.getPatrolLeader() == event.getEntity()) {
                pillagerPatrol.naturalDespawn();
                pillagerPatrols.remove(pillagerPatrol);
                pillagerPatrol.disbandPatrol();
                break;
            }
        }
    }

    /**
     * This really isn't how I wanted to implement as an event handler
     * @param event
     */
    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {
        ArrayList<Base> pillagerBasesToRemove = new ArrayList<>();
        for (Base base: pillagerBases) {
            if (base.checkRaided(event)) {
                pillagerBasesToRemove.add(base);
            }
        }

        pillagerBases.removeAll(pillagerBasesToRemove);

        if (pillagerBases.isEmpty()) {
            BlockBreakEvent.getHandlerList().unregister(this);
        }
    }

    /**
     * Updates pillagerPatrols
     */
    @EventHandler
    public void onPatrolUpdate(PatrolUpdateEvent event) {

        //new moveRunnable(event.getInterval(), Main.getPlugin(Main.class)).runTaskAsynchronously(Main.getPlugin(Main.class));
        new moveRunnable(event.getInterval(), Main.getPlugin(Main.class)).runTask(Main.getPlugin(Main.class));

        for (Base pillagerBase : pillagerBases) {
            if (pillagerBase.isActive() && (pillagerPatrols.size() < DataManager.getInstance().getConfig().getInt("patrols.total"))) {
                //Update each pillagerPatrols location if not spawned
                pillagerBase.attemptProducePatrol();
            }
        }


    }

    /**
     * Updates the bases
     */
    @EventHandler
    public void onBaseUpdate(BaseUpdateEvent event) {
        for (Base pillagerBase : pillagerBases) {
            //If the patrol is not already spawned then update its location and attempt to spawn it
            //if (pillagerBase.isActive() && (pillagerPatrols.size() < DataManager.getInstance().getConfig().getInt("patrols.total"))) {
            //    //Update each pillagerPatrols location if not spawned
            //    //patrol.move(event.getInterval());
            //    pillagerBase.attemptProducePatrol();
            //}

            if (!pillagerBase.isActive()) {
                pillagerBases.remove(pillagerBase);
                pillagerBase.remove();
                break;
            }
        }
    }

    /**
     * Upgrades pillagerPatrols
     * @param event Upgrade event
     */
    @EventHandler
    public void onUpgrade(UpgradeEvent event) {
        getLogger().log(Level.INFO, String.format("The base at %d %d was upgraded to level %d", event.getBase().getLocation().getBlockX(), event.getBase().getLocation().getBlockZ(), event.getBase().getLevel()));
        event.getBase().upgradeBase();
    }

    /**
     *
     * @param x the x coordinate in chunks
     * @param z the z coordinate in chunks
     */
    public void createPatrol(int x, int z) {
        pillagerPatrols.add(new PillagerPatrol(x, z));
    }

}
