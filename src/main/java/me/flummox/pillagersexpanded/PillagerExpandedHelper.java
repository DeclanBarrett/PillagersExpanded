package me.flummox.pillagersexpanded;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.flummox.pillagersexpanded.enums.BaseLocationPair;
import me.flummox.pillagersexpanded.enums.BaseType;
import org.bukkit.*;
import org.bukkit.generator.structure.Structure;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StructureSearchResult;

import java.util.*;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Bukkit.*;

/**
 * Creates patrols that are persistent and wander around the map
 */
public class PillagerExpandedHelper {

    protected PillagerExpandedHelper() {

    }

    private static class SingletonHolder {
        private final static PillagerExpandedHelper INSTANCE = new PillagerExpandedHelper();
    }

    public static PillagerExpandedHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Find a pillager outpost within the configured range
     * @return the location fo the pillager outpost (with block location)
     */
    public Location pillagerOutpost() {
       //"Finding a pillager outpost");
        //Get the current amount of patrols
        int xLocation = (int) (Math.random() * 40000 - 20000); //$
        int yLocation = (int) (Math.random() * 40000 - 20000);

        Location patrolSpawnLocation;

        //looks for an outpost till it is guaranteed to have one
        try {
            do {
            patrolSpawnLocation = Objects.requireNonNull(getServer().getWorld("world")).locateNearestStructure(
                    new Location(getServer().getWorld("world"), xLocation, 144, yLocation),
                    StructureType.PILLAGER_OUTPOST,
                    5000, false); //$
        } while (patrolSpawnLocation == null);
        } catch (NullPointerException e) {
            patrolSpawnLocation = new Location(getServer().getWorld("world"), 0, 0, 0);
        }
       //patrolSpawnLocation);

        return patrolSpawnLocation;
    }

    /**
     * Find locations and type of pillager bases between
     * @return a list of locations where bases are
     */
    public ArrayList<BaseLocationPair> pillagerBases() {
       //"Attempting to find some bases");
        ArrayList<BaseLocationPair> bases = new ArrayList<>();
        World world = getServer().getWorld("world");
        if (world == null) {
            getLogger().log(Level.WARNING, "World not named world");
        }

        //Gather from disk and determine the spacing for the bases
        int squareRadius = DataManager.getInstance().getConfig().getInt("bases.radius");
        int maxQuantity = DataManager.getInstance().getConfig().getInt("bases.maxQuantity");

        int maxQuantitySide = (int) Math.sqrt(maxQuantity);

        double spacing = 0;
        if (maxQuantitySide > 1) {
            spacing = 2 * squareRadius / (maxQuantitySide - 1);

        }

        //Create a filled square of base locations
        for (int x = -(maxQuantitySide/2); x <= maxQuantitySide/2; x++) {
            for (int z = -(maxQuantitySide/2); z <= maxQuantitySide/2; z++) {

                int xCurrent = (int) (x * spacing);
                int zCurrent = (int) (z * spacing);

                StructureSearchResult outpostSearch = Objects.requireNonNull(world).locateNearestStructure(
                        new Location(world, xCurrent, 100, zCurrent),
                        Structure.PILLAGER_OUTPOST,
                        (int) spacing,
                        false
                        );

                //Do not spawn a castle if an outpost already exists in the area
                boolean isOutpostPresent = false;
                Location outpostLocation = new Location(world, outpostSearch.getLocation().getBlockX(), world.getHighestBlockYAt(outpostSearch.getLocation().getBlockX(), outpostSearch.getLocation().getBlockZ()), outpostSearch.getLocation().getBlockZ());

                //Adds a new outpost base nearby the square point if one exists and is new
                if (DataManager.getInstance().getConfig().getBoolean("allow.outposts")) {

                    if (outpostLocation != null) {

                        boolean isAlreadyExisting = false;
                        for (BaseLocationPair basis : bases) {
                            if (basis.getLocation().getBlockX() == outpostLocation.getBlockX() &&
                                    basis.getLocation().getBlockZ() == outpostLocation.getBlockZ()) {
                                isAlreadyExisting = true;
                            }
                        }

                        if (!isAlreadyExisting) {
                            bases.add(new BaseLocationPair(outpostLocation, BaseType.OUTPOST));
                            isOutpostPresent = true;
                        }
                    }

                }

                //Adds a base if an outpost isn't present
                if (DataManager.getInstance().getConfig().getBoolean("allow.castles") && !isOutpostPresent) {

                    int yLocation = world.getHighestBlockYAt(xCurrent, zCurrent);

                    Location baseLocation = new Location(world, xCurrent, yLocation, zCurrent);
                    BaseLocationPair couplingInformation = new BaseLocationPair(baseLocation, BaseType.CASTLE);
                    bases.add(couplingInformation);
                }
            }
        }

        return bases;
    }

    /**
     * Find a pillager outpost within the configured range
     * @return the location fo the pillager outpost (in block form)
     */
    public Location villagerOutpost(Integer currentX, Integer currentZ) {
       //"Finding a pillager outpost");
        //Get the current amount of patrolscurrentX
        int villagerRadius = DataManager.getInstance().getConfig().getInt("patrols.patrolRadius");
        int xLocation = (int) (Math.random() * 2 * villagerRadius - (villagerRadius + currentX)); //$
        int yLocation = (int) (Math.random() * 2 * villagerRadius - (villagerRadius + currentZ));

        //patrols.patrolRadius

        Location patrolDestinationLocation;

        World world = getServer().getWorld("world");

        //looks for an outpost till it is guaranteed to have one
        assert world != null;
        patrolDestinationLocation = world.locateNearestStructure(
                new Location(world, xLocation, 144, yLocation),
                StructureType.VILLAGE,
                villagerRadius, false);

        if (patrolDestinationLocation == null) {
            patrolDestinationLocation = new Location(world, (Math.random() * villagerRadius), 144, (Math.random() * villagerRadius));
        }

       //patrolSpawnLocation);

        return patrolDestinationLocation;
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin p = getServer().getPluginManager().getPlugin("WorldEdit");

        if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }

}