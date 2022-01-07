package me.flummox.pillagersexpanded;

import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
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

    public int getOutpostID() {
        return outpostID;
    }

    private int outpostID;
    private int level;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    boolean isActive;

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

        outpostLocation.setX(outpostLocation.getBlockX() );
        outpostLocation.setZ(outpostLocation.getBlockZ() );
        outpostLocation.setY(world.getHighestBlockYAt(outpostLocation.getBlockX(), outpostLocation.getBlockZ() ));

        this.outpostLocation = outpostLocation;
        this.level = 1;
        this.outpostID = outpostID;
        this.isActive = true;
        //new buildOutpostRunnable(outpostLocation, Main.getPlugin(Main.class)).runTask(Main.getPlugin(Main.class));
        rebuildOutpost();
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
        this.isActive = DataManager.getInstance().getConfig().getBoolean(outpostString + "isActive");
        this.outpostLocation = new Location(getServer().getWorld("world"), currentX, currentY, currentZ);
        rebuildOutpost();
        System.out.println("LOADED " + outpostID + "| x: " + outpostLocation.getX() + " y: " + outpostLocation.getY() + " z: " + outpostLocation.getZ());
    }

    private void rebuildOutpost() {
        World world = outpostLocation.getWorld();

        Material locationMaterial = world.getBlockAt(outpostLocation.getBlockX() ,
                world.getHighestBlockYAt(outpostLocation.getBlockX() , outpostLocation.getBlockZ() ),
                outpostLocation.getBlockZ() ).getType();

        System.out.println("Outpost material" + locationMaterial );

        if (isActive == true) {
            int offset = 3;
            for (int y = 0; y < building.length; y++) {
                for (int x = 0; x < building[y].length; x++) {
                    for (int z = 0; z < building[y][x].length; z++) {
                        //Spawn the specified block at the location
                        System.out.println("y: " + y + " x: " + x + " z: " + z);
                        if (building[y][x][z] != null) {
                            world.getBlockAt(new Location( world,outpostLocation.getX() - offset + x,outpostLocation.getY() + y, outpostLocation.getZ() - offset + z)).setType(building[y][x][z]);
                        }
                    }
                }
            }
            generateWall();


            Block block = outpostLocation.getBlock();

            block.setType(Material.SPAWNER);
            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
            creatureSpawner.setSpawnedType(EntityType.VINDICATOR);
        } else {
            System.out.println(" Material at Spawner " + locationMaterial);
        }

        System.out.println("CREATED " + outpostID + "| x: " + outpostLocation.getX() + " y: " + outpostLocation.getY() + " z: " + outpostLocation.getZ());
        save();
    }



    public void upgradeOutpost() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!Outpost Upgrading!!!!!!!!!!!!!!!!!!!!!!!!");
        World world = outpostLocation.getWorld();
        level++;
        if (level == 2) {
            world.getBlockAt(new Location(world,outpostLocation.getX() + 1,outpostLocation.getY(), outpostLocation.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX() - 1,outpostLocation.getY(), outpostLocation.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX(),outpostLocation.getY() + 1, outpostLocation.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX(),outpostLocation.getY(), outpostLocation.getZ() + 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX(),outpostLocation.getY(), outpostLocation.getZ() - 1)).setType(Material.OBSIDIAN);
        } else if (level == 4) {
            world.getBlockAt(new Location(world,outpostLocation.getX() + 1,outpostLocation.getY(), outpostLocation.getZ() + 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX() - 1,outpostLocation.getY(), outpostLocation.getZ() - 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX() + 1,outpostLocation.getY(), outpostLocation.getZ() - 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX() - 1,outpostLocation.getY(), outpostLocation.getZ() + 1)).setType(Material.OBSIDIAN);
        } else if (level == 6) {
            world.getBlockAt(new Location(world,outpostLocation.getX() + 1,outpostLocation.getY() + 1, outpostLocation.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX() - 1,outpostLocation.getY() + 1, outpostLocation.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX(),outpostLocation.getY() + 2, outpostLocation.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX(),outpostLocation.getY() + 1, outpostLocation.getZ() + 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,outpostLocation.getX(),outpostLocation.getY() + 1, outpostLocation.getZ() - 1)).setType(Material.OBSIDIAN);
        } else if (level > 10) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    for (int y = -3; y <= 3; y++) {
                        if (!(x == 0 && y == 0 && z == 0)) {
                            world.getBlockAt(new Location(world,outpostLocation.getX() + x,outpostLocation.getY() + y, outpostLocation.getZ() + z)).setType(Material.OBSIDIAN);
                        }
                    }
                }
            }
        } else if (level % 10 == 0) {
            int spawnerLevel = level / 10;
            Block block = world.getBlockAt(new Location(world,outpostLocation.getX(),outpostLocation.getY() + 3 + spawnerLevel, outpostLocation.getZ()));

            block.setType(Material.SPAWNER);
            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
            creatureSpawner.setSpawnedType(EntityType.VINDICATOR);
        }

    }

    private void generateWall() {
        System.out.println("Generating Wall");
        World world = outpostLocation.getWorld();
        int squareOffset = 40;
        int yOffset = 30;

        for (int y = 0; y <= 45; y++) {

            int offset = squareOffset - 2;
            for (int x = 0; x <= (offset * 2); x++) {
                if (y == 45) {
                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset + x, outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset)).setType(Material.SPRUCE_SLAB);
                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset + x, outpostLocation.getY() - yOffset + y, outpostLocation.getZ() + offset)).setType(Material.SPRUCE_SLAB);
                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset, outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset + x)).setType(Material.SPRUCE_SLAB);
                    world.getBlockAt(new Location(world, outpostLocation.getX() + offset, outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset + x)).setType(Material.SPRUCE_SLAB);
                }
            }

            offset = squareOffset - 1;
            for (int x = 0; x <= (offset * 2); x++) {
                Material placingMaterial;
                if (y == 45) {
                    placingMaterial = Material.SPRUCE_SLAB;
                } else if (x == 0 || x == offset * 2) {
                    placingMaterial = Material.DARK_OAK_LOG;
                } else if (x % 5 == 0 || y % 11 == 0) {
                    placingMaterial = Material.DARK_OAK_PLANKS;
                } else {
                    placingMaterial = Material.COBBLESTONE;
                }
                world.getBlockAt(new Location(world,outpostLocation.getX() - offset + x,outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset)).setType(placingMaterial);
                world.getBlockAt(new Location(world,outpostLocation.getX() - offset + x,outpostLocation.getY() - yOffset + y, outpostLocation.getZ() + offset)).setType(placingMaterial);
                world.getBlockAt(new Location(world,outpostLocation.getX() - offset,outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset + x)).setType(placingMaterial);
                world.getBlockAt(new Location(world,outpostLocation.getX() + offset,outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset + x)).setType(placingMaterial);
            }
            offset = squareOffset;
            for (int x = 0; x <= (offset * 2); x++) {
                if (y == 45) {
                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset + x, outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset)).setType(Material.DARK_OAK_LOG);
                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset + x, outpostLocation.getY() - yOffset + y, outpostLocation.getZ() + offset)).setType(Material.DARK_OAK_LOG);
                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset, outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset + x)).setType(Material.DARK_OAK_LOG);
                    world.getBlockAt(new Location(world, outpostLocation.getX() + offset, outpostLocation.getY() - yOffset + y, outpostLocation.getZ() - offset + x)).setType(Material.DARK_OAK_LOG);

                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset + x, outpostLocation.getY() - yOffset + y + 1, outpostLocation.getZ() - offset)).setType(Material.DARK_OAK_FENCE);
                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset + x, outpostLocation.getY() - yOffset + y + 1, outpostLocation.getZ() + offset)).setType(Material.DARK_OAK_FENCE);
                    world.getBlockAt(new Location(world, outpostLocation.getX() - offset, outpostLocation.getY() - yOffset + y + 1, outpostLocation.getZ() - offset + x)).setType(Material.DARK_OAK_FENCE);
                    world.getBlockAt(new Location(world, outpostLocation.getX() + offset, outpostLocation.getY() - yOffset + y + 1, outpostLocation.getZ() - offset + x)).setType(Material.DARK_OAK_FENCE);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean checkRaided() {
        Block block = outpostLocation.getBlock();

        if (block.getType() != Material.SPAWNER) {
            isActive = false;
        } else {
            isActive = true;
        }
        save();
        return isActive;
    }

    public void save() {
        System.out.println(outpostID + " is saving");
        String currentPatrolString = "outposts." + (this.outpostID) + ".";
        DataManager.getInstance().getConfig().set(currentPatrolString + "x", this.outpostLocation.getBlockX());
        DataManager.getInstance().getConfig().set(currentPatrolString + "y", this.outpostLocation.getBlockY());
        DataManager.getInstance().getConfig().set(currentPatrolString + "z", this.outpostLocation.getBlockZ());
        DataManager.getInstance().getConfig().set(currentPatrolString + "level", this.level);
        DataManager.getInstance().getConfig().set(currentPatrolString + "isActive", this.isActive);
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

    public void attemptProducePatrol() {
        int chance = (int) (Math.random() * 20);
        System.out.println("Attempting to Produce Patrol");
        if (chance < level) {
            PillagerEventHandler.getInstance().createPatrol(
                    outpostLocation.getBlockX(),
                    outpostLocation.getBlockZ());
        }
    }

    public void remove() {
        System.out.println(outpostID + " DESTROYED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String currentOutpostString = "outposts." + (this.getOutpostID());
        DataManager data = DataManager.getInstance();
        data.getConfig().set(currentOutpostString, null);
        data.saveConfig();
    }
}
