package me.flummox.pillagersexpanded;

import me.flummox.pillagersexpanded.eventHandlers.buildOutpostRunnable;
import me.flummox.pillagersexpanded.eventHandlers.moveRunnable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.util.Optional;

import static org.bukkit.Bukkit.getServer;

public class Outpost {

    public Location getOutpostLocation() {
        return outpostLocation;
    }

    private Location outpostLocation;
    private int outpostID;
    private int level;

    boolean hasWall;

    /**
     * Create a digital outpost
     * @param outpostID
     * @param outpostLocation
     */
    public Outpost(int outpostID, Location outpostLocation) {
        World world = outpostLocation.getWorld();
        Material locationMaterial = world.getBlockAt(outpostLocation.getBlockX() ,
                world.getHighestBlockYAt(outpostLocation.getBlockX() , outpostLocation.getBlockZ() ),
                outpostLocation.getBlockZ() ).getType();

        System.out.println("Outpost material" + locationMaterial );
        outpostLocation.setX(outpostLocation.getBlockX() );
        outpostLocation.setZ(outpostLocation.getBlockZ() );
        outpostLocation.setY(world.getHighestBlockYAt(outpostLocation.getBlockX(), outpostLocation.getBlockZ() ));
        if (locationMaterial != Material.SPAWNER) {
            outpostLocation.getBlock().setType(Material.SPAWNER);
            ((CreatureSpawner) outpostLocation.getBlock()).setSpawnedType(EntityType.VINDICATOR);
        } else {
            System.out.println(" " + locationMaterial);
        }
        this.outpostLocation = outpostLocation;
        this.level = 1;
        this.outpostID = outpostID;
        new buildOutpostRunnable(outpostLocation, Main.getPlugin(Main.class)).runTaskAsynchronously(Main.getPlugin(Main.class));
        generateWall();
        System.out.println("CREATED " + outpostID + "| x: " + outpostLocation.getX() + " y: " + outpostLocation.getY() + " z: " + outpostLocation.getZ());
        save();
    }

    /**
     * Load the outpost from the config fle
     * @param outpostID
     */
    public Outpost(int outpostID) {
        this.outpostID = outpostID;
        String outpostString = "outposts." + outpostID + ".";
        int currentX = DataManager.getInstance().getConfig().getInt(outpostString + "x");
        int currentY = DataManager.getInstance().getConfig().getInt(outpostString + "y");
        int currentZ = DataManager.getInstance().getConfig().getInt(outpostString + "z");
        this.level = DataManager.getInstance().getConfig().getInt(outpostString + "level");
        this.hasWall = DataManager.getInstance().getConfig().getBoolean(outpostString + "hasWall");
        this.outpostLocation = new Location(getServer().getWorld("world"), currentX, currentY, currentZ);
        System.out.println("LOADED " + outpostID + "| x: " + outpostLocation.getX() + " y: " + outpostLocation.getY() + " z: " + outpostLocation.getZ());
        generateWall();
    }

    public void upgradeOutpost() {
        level++;

    }

    private void generateWall() {
        //if (hasWall) {
            //outpostLocation.getBlock().setType(Material.BIRCH_PLANKS);
           // hasWall = true;
        //}
    }

    public void checkRaided() {

    }

    public void save() {
        System.out.println(outpostID + " is saving");
        String currentPatrolString = "outposts." + (this.outpostID) + ".";
        DataManager.getInstance().getConfig().set(currentPatrolString + "x", this.outpostLocation.getBlockX());
        DataManager.getInstance().getConfig().set(currentPatrolString + "y", this.outpostLocation.getBlockY());
        DataManager.getInstance().getConfig().set(currentPatrolString + "z", this.outpostLocation.getBlockZ());
        DataManager.getInstance().getConfig().set(currentPatrolString + "level", this.level);
        DataManager.getInstance().getConfig().set(currentPatrolString + "hasWall", this.hasWall);
        DataManager.getInstance().saveConfig();
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        //Check if o is an instance of Complex or not
        //"null instanceof [type]" also returns false */
        if (!(o instanceof Outpost)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Outpost c = (Outpost) o;

        return (this.getOutpostLocation().getBlockX() == c.getOutpostLocation().getBlockX()) &&
                (this.getOutpostLocation().getBlockZ() == c.getOutpostLocation().getBlockZ());
    }

}
