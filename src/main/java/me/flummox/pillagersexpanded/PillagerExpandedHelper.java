package me.flummox.pillagersexpanded;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.A;

import javax.swing.plaf.synth.SynthUI;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

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
        System.out.println("Finding a pillager outpost");
        //Get the current amount of patrols
        Integer xLocation = (int) (Math.random() * 40000 - 20000); //$
        Integer yLocation = (int) (Math.random() * 40000 - 20000);

        Location patrolSpawnLocation = null;

        //looks for an outpost till it is guaranteed to have one
        do {
            patrolSpawnLocation = getServer().getWorld("world").locateNearestStructure(
                    new Location(getServer().getWorld("world"), xLocation, 144, yLocation),
                    StructureType.PILLAGER_OUTPOST,
                    5000, false); //$
        } while (patrolSpawnLocation == null);

        System.out.println(patrolSpawnLocation);

        return patrolSpawnLocation;
    }

    /**
     * Find locations of pillager outposts between
     * @return a list of locations where outposts are
     */
    public ArrayList<Location> pillagerOutposts() {
        System.out.println("Attempting to find some outposts");
        ArrayList<Location> outposts = new ArrayList<>();
        World world = getServer().getWorld("world");

        for (int x = -4000; x < 4000; x += 2000) {
            for (int z = -4000; z < 4000; z += 2000) {
                Location outpostLocation = world.locateNearestStructure(new Location(getServer().getWorld("world"), x, 100, z),
                        StructureType.PILLAGER_OUTPOST,
                        2000, false);

                if (outpostLocation != null) {
                    boolean isAlreadyExisting = false;
                    for (int i = 0; i < outposts.size(); i++) {
                        //System.out.println("COMPARE: " + outposts.get(i).getBlockX() + " = " + outpostLocation.getBlockX() + " & " + outposts.get(i).getBlockX() + " = " + outpostLocation.getBlockX());
                        if (outposts.get(i).getBlockX() == outpostLocation.getBlockX() &&
                                outposts.get(i).getBlockZ() == outpostLocation.getBlockZ()) {
                            isAlreadyExisting = true;
                        }
                    }

                    if (!isAlreadyExisting) {
                        //System.out.println("ADDING OUTPOST LOCATION~~~~~~~~~~~~~~~~~~~~~~~~~~~~ x: " + outpostLocation.getBlockX() + ", z: " + outpostLocation.getBlockZ());
                        outposts.add(outpostLocation);
                    }

                }
            }
        }

        return outposts;
    }

    /**
     * Find a pillager outpost within the configured range
     * @return the location fo the pillager outpost (in block form)
     */
    public Location villagerOutpost(Integer currentX, Integer currentZ) {
        System.out.println("Finding a pillager outpost");
        //Get the current amount of patrolscurrentX
        Integer xLocation = (int) (Math.random() * 5000 - (2500 + currentX)); //$
        Integer yLocation = (int) (Math.random() * 5000 - (2500 + currentZ));

        Location patrolSpawnLocation = null;

        //looks for an outpost till it is guaranteed to have one
        do {
            patrolSpawnLocation = getServer().getWorld("world").locateNearestStructure(
                    new Location(getServer().getWorld("world"), xLocation, 144, yLocation),
                    StructureType.VILLAGE,
                    1000, false); //$
        } while (patrolSpawnLocation == null);

        System.out.println(patrolSpawnLocation);

        return patrolSpawnLocation;
    }

}
