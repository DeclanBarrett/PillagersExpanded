package me.flummox.pillagersexpanded;

import me.flummox.pillagersexpanded.eventHandlers.buildOutpostRunnable;
import me.flummox.pillagersexpanded.eventHandlers.moveRunnable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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

    Material[][][] building = {
            {       {Material.DARK_OAK_LOG, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_LOG},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.DARK_OAK_LOG, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_LOG}},

            {       {Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, null, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_LOG},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {null, null, null, null, null, null, null},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, null, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_LOG}},

            {       {Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, null, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_LOG},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {null, null, null, null, null, null, null},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, null, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_LOG}},

            {       {Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_LOG},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_PLANKS, null, null, null, null, null, Material.DARK_OAK_PLANKS},
                    {Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_PLANKS, Material.DARK_OAK_LOG}},

            {       {Material.DARK_OAK_LOG, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.DARK_OAK_LOG},
                    {Material.COBBLESTONE, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.COBBLESTONE},
                    {Material.COBBLESTONE, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.COBBLESTONE},
                    {Material.COBBLESTONE, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, null, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.COBBLESTONE},
                    {Material.COBBLESTONE, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.COBBLESTONE},
                    {Material.COBBLESTONE, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.COBBLESTONE},
                    {Material.DARK_OAK_LOG, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.DARK_OAK_LOG}},

            {       {Material.DARK_OAK_LOG, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE, Material.DARK_OAK_LOG},
                    {Material.DARK_OAK_FENCE, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {Material.DARK_OAK_FENCE, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {Material.DARK_OAK_FENCE, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {Material.DARK_OAK_FENCE, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {Material.DARK_OAK_FENCE, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {Material.DARK_OAK_LOG, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE, Material.DARK_OAK_LOG}},

    };

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
            Block block = outpostLocation.getBlock();

            block.setType(Material.SPAWNER);
            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
            creatureSpawner.setSpawnedType(EntityType.VINDICATOR);
        } else {
            System.out.println(" " + locationMaterial);
        }
        this.outpostLocation = outpostLocation;
        this.level = 1;
        this.outpostID = outpostID;
        //new buildOutpostRunnable(outpostLocation, Main.getPlugin(Main.class)).runTask(Main.getPlugin(Main.class));
        int offset = 3;
        for (int y = 0; y < building.length; y++) {
            for (int x = 0; x < building[y].length; x++) {
                for (int z = 0; z < building[y][x].length; z++) {
                    //Spawn the specified block at the location
                    System.out.println("y: " + y + " x: " + x + " z: " + z);
                    if (building[y][x][z] != null) {
                        world.getBlockAt(new Location(world,outpostLocation.getX() - offset + x,outpostLocation.getY() + y, outpostLocation.getZ() - offset + z)).setType(building[y][x][z]);
                    }
                }
            }
        }
        generateWall(world);
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
    }

    public void upgradeOutpost() {
        level++;

    }

    private void generateWall(World world) {
        int squareOffset = 40;
        int yOffset = 20;
        for (int x = 0; x < 80; x++) {
            for (int y = 0; y < 40; y++) {
                for (int z = 0; z < 80; z++) {
                    world.getBlockAt(new Location(world,outpostLocation.getX() - squareOffset + x,outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - squareOffset + z)).setType(building[y][x][z]);
                }
            }
        }
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
