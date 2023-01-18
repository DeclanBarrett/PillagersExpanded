package me.flummox.pillagersexpanded.bases;

import me.flummox.pillagersexpanded.enums.BaseType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.logging.Level;

import static org.bukkit.Bukkit.*;

public class PillagerOutpost extends Base implements Listener {

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
     * @param localBaseID the base id to assign to the base
     * @param localLocation the location to place the base
     */
    public PillagerOutpost(int localBaseID, Location localLocation) {
        createDigitalBase(localBaseID, localLocation, BaseType.OUTPOST);
        buildBase();
        save();
    }

    /**
     * Load the outpost from the config fle
     * @param baseID the unique base ID
     */
    public PillagerOutpost(int baseID) {
        createDigitalBase(baseID);
        buildBase();
    }

    /**
     * Builds the physical outpost
     */
    public void buildBase() {

        World world = location.getWorld();

        if (isActive) {
            int offset = 3;
            for (int y = 0; y < building.length; y++) {
                for (int x = 0; x < building[y].length; x++) {
                    for (int z = 0; z < building[y][x].length; z++) {
                        //Spawn the specified block at the location
                       //"y: " + y + " x: " + x + " z: " + z);
                        if (building[y][x][z] != null) {
                            world.getBlockAt(new Location( world,location.getX() - offset + x,location.getY() + y, location.getZ() - offset + z)).setType(building[y][x][z]);
                        }
                    }
                }
            }
            generateWall();


            Block block = location.getBlock();

            block.setType(Material.SPAWNER);
            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
            creatureSpawner.setSpawnedType(EntityType.VINDICATOR);
            creatureSpawner.update();
        }

        save();
    }


    /**
     * Upgrades the outpost by adding obsidian around the block that needs to be destroyed
     */
    public void upgradeBase() {
        World world = location.getWorld();
        level++;
        if (level == 2) {
            world.getBlockAt(new Location(world,location.getX() + 1,location.getY(), location.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX() - 1,location.getY(), location.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX(),location.getY() + 1, location.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX(),location.getY(), location.getZ() + 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX(),location.getY(), location.getZ() - 1)).setType(Material.OBSIDIAN);
        } else if (level == 4) {
            world.getBlockAt(new Location(world,location.getX() + 1,location.getY(), location.getZ() + 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX() - 1,location.getY(), location.getZ() - 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX() + 1,location.getY(), location.getZ() - 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX() - 1,location.getY(), location.getZ() + 1)).setType(Material.OBSIDIAN);
        } else if (level == 6) {
            world.getBlockAt(new Location(world,location.getX() + 1,location.getY() + 1, location.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX() - 1,location.getY() + 1, location.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX(),location.getY() + 2, location.getZ())).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX(),location.getY() + 1, location.getZ() + 1)).setType(Material.OBSIDIAN);
            world.getBlockAt(new Location(world,location.getX(),location.getY() + 1, location.getZ() - 1)).setType(Material.OBSIDIAN);
        } else if (level > 10) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    for (int y = -3; y <= 3; y++) {
                        if (!(x == 0 && y == 0 && z == 0)) {
                            world.getBlockAt(new Location(world,location.getX() + x,location.getY() + y, location.getZ() + z)).setType(Material.OBSIDIAN);
                        }
                    }
                }
            }
        } else if (level % 10 == 0) {
            int spawnerLevel = level / 10;
            Block block = world.getBlockAt(new Location(world,location.getX(),location.getY() + 3 + spawnerLevel, location.getZ()));

            block.setType(Material.SPAWNER);
            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
            creatureSpawner.setSpawnedType(EntityType.VINDICATOR);
        }

    }

    /**
     * Generate a wall surrounding the outpost
     */
    private void generateWall() {
       //"Generating Wall");
        World world = location.getWorld();

        int squareOffset = 40;
        int yOffset = 30;

        int y = 45;
        int offset = squareOffset - 2;
        //Top of the wall

        for (int x = 0; x <= (offset * 2); x++) {
            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y, location.getZ() - offset)).setType(Material.SPRUCE_SLAB);
            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y, location.getZ() + offset)).setType(Material.SPRUCE_SLAB);
            world.getBlockAt(new Location(world, location.getX() - offset, location.getY() - yOffset + y, location.getZ() - offset + x)).setType(Material.SPRUCE_SLAB);
            world.getBlockAt(new Location(world, location.getX() + offset, location.getY() - yOffset + y, location.getZ() - offset + x)).setType(Material.SPRUCE_SLAB);

            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y + 1, location.getZ() - offset)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y + 1, location.getZ() + offset)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() - offset, location.getY() - yOffset + y + 1, location.getZ() - offset + x)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() + offset, location.getY() - yOffset + y + 1, location.getZ() - offset + x)).setType(Material.AIR);

            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y + 2, location.getZ() - offset)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y + 2, location.getZ() + offset)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() - offset, location.getY() - yOffset + y + 2, location.getZ() - offset + x)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() + offset, location.getY() - yOffset + y + 2, location.getZ() - offset + x)).setType(Material.AIR);

        }

        offset = squareOffset - 1;
        for (int x = 0; x <= (offset * 2); x++) {
            Material placingMaterial = Material.SPRUCE_SLAB;
            world.getBlockAt(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() - offset)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() + offset)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() - offset,location.getY() - yOffset + y, location.getZ() - offset + x)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() + offset,location.getY() - yOffset + y, location.getZ() - offset + x)).setType(placingMaterial);
            placingMaterial = Material.AIR;
            world.getBlockAt(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y + 1, location.getZ() - offset)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y + 1, location.getZ() + offset)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() - offset,location.getY() - yOffset + y + 1, location.getZ() - offset + x)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() + offset,location.getY() - yOffset + y + 1, location.getZ() - offset + x)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y + 2, location.getZ() - offset)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y + 2, location.getZ() + offset)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() - offset,location.getY() - yOffset + y + 2, location.getZ() - offset + x)).setType(placingMaterial);
            world.getBlockAt(new Location(world,location.getX() + offset,location.getY() - yOffset + y + 2, location.getZ() - offset + x)).setType(placingMaterial);
        }
        offset = squareOffset;
        for (int x = 0; x <= (offset * 2); x++) {
            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y, location.getZ() - offset)).setType(Material.DARK_OAK_LOG);
            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y, location.getZ() + offset)).setType(Material.DARK_OAK_LOG);
            world.getBlockAt(new Location(world, location.getX() - offset, location.getY() - yOffset + y, location.getZ() - offset + x)).setType(Material.DARK_OAK_LOG);
            world.getBlockAt(new Location(world, location.getX() + offset, location.getY() - yOffset + y, location.getZ() - offset + x)).setType(Material.DARK_OAK_LOG);

            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y + 1, location.getZ() - offset)).setType(Material.DARK_OAK_FENCE);
            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y + 1, location.getZ() + offset)).setType(Material.DARK_OAK_FENCE);
            world.getBlockAt(new Location(world, location.getX() - offset, location.getY() - yOffset + y + 1, location.getZ() - offset + x)).setType(Material.DARK_OAK_FENCE);
            world.getBlockAt(new Location(world, location.getX() + offset, location.getY() - yOffset + y + 1, location.getZ() - offset + x)).setType(Material.DARK_OAK_FENCE);

            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y + 2, location.getZ() - offset)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() - offset + x, location.getY() - yOffset + y + 2, location.getZ() + offset)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() - offset, location.getY() - yOffset + y + 2, location.getZ() - offset + x)).setType(Material.AIR);
            world.getBlockAt(new Location(world, location.getX() + offset, location.getY() - yOffset + y + 2, location.getZ() - offset + x)).setType(Material.AIR);
        }

        offset = squareOffset - 1;
        for (int x = 0; x <= (offset * 2); x++) {
            //1st side of the square

            y = 44;

            while (
                    !(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() - offset).getBlock().getType().equals(Material.DIRT)) &&
                    !(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() - offset).getBlock().getType().equals(Material.STONE)) &&
                    !(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() - offset).getBlock().getType().equals(Material.DEEPSLATE)) &&
                    !(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() - offset).getBlock().getType().equals(Material.BEDROCK)) && y > -50
            ) {
                /*getLogger().log(Level.INFO, "1st side: " + new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() - offset).getBlock().getType());
                getLogger().log(Level.INFO, "1st side: " + new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() - offset).getBlock().getType().equals(Material.DIRT));*/

                Material placingMaterial;
                if (x == 0 || x == offset * 2) {
                    placingMaterial = Material.DARK_OAK_LOG;
                } else if (x % 5 == 0 || y % 11 == 0) {
                    placingMaterial = Material.DARK_OAK_PLANKS;
                } else {
                    placingMaterial = Material.COBBLESTONE;
                }
                world.getBlockAt(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() - offset)).setType(placingMaterial);
                y--;
            }

            //2nd side of the square
            y = 44;
            while (
                    !(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() + offset).getBlock().getType().equals(Material.DIRT)) &&
                            !(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() + offset).getBlock().getType().equals(Material.STONE)) &&
                            !(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() + offset).getBlock().getType().equals(Material.DEEPSLATE)) &&
                            !(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() + offset).getBlock().getType().equals(Material.BEDROCK)) && y > -50
            ) {
                Material placingMaterial;
                if (x == 0 || x == offset * 2) {
                    placingMaterial = Material.DARK_OAK_LOG;
                } else if (x % 5 == 0 || y % 11 == 0) {
                    placingMaterial = Material.DARK_OAK_PLANKS;
                } else {
                    placingMaterial = Material.COBBLESTONE;
                }
                world.getBlockAt(new Location(world,location.getX() - offset + x,location.getY() - yOffset + y, location.getZ() + offset)).setType(placingMaterial);
                y--;
            }

            //3rd side of the square
            y = 44;
            while (
                    !(new Location(world,location.getX() - offset,location.getY() - yOffset + y, location.getZ() - offset + x).getBlock().getType().equals(Material.DIRT)) &&
                            !(new Location(world,location.getX() - offset,location.getY() - yOffset + y, location.getZ() - offset + x).getBlock().getType().equals(Material.STONE)) &&
                            !(new Location(world,location.getX() - offset,location.getY() - yOffset + y, location.getZ() - offset + x).getBlock().getType().equals(Material.DEEPSLATE)) &&
                            !(new Location(world,location.getX() - offset,location.getY() - yOffset + y, location.getZ() - offset + x).getBlock().getType().equals(Material.BEDROCK)) && y > -50
            ) {
                Material placingMaterial;
                if (x == 0 || x == offset * 2) {
                    placingMaterial = Material.DARK_OAK_LOG;
                } else if (x % 5 == 0 || y % 11 == 0) {
                    placingMaterial = Material.DARK_OAK_PLANKS;
                } else {
                    placingMaterial = Material.COBBLESTONE;
                }
                world.getBlockAt(new Location(world,location.getX() - offset,location.getY() - yOffset + y, location.getZ() - offset + x)).setType(placingMaterial);
                y--;
            }

            //4th side of the square
            y = 44;
            while (
                    !(new Location(world,location.getX() + offset,location.getY() - yOffset + y, location.getZ() - offset + x).getBlock().getType().equals(Material.DIRT)) &&
                    !(new Location(world,location.getX() + offset,location.getY() - yOffset + y, location.getZ() - offset + x).getBlock().getType().equals(Material.STONE)) &&
                    !(new Location(world,location.getX() + offset,location.getY() - yOffset + y, location.getZ() - offset + x).getBlock().getType().equals(Material.DEEPSLATE)) &&
                    !(new Location(world,location.getX() + offset,location.getY() - yOffset + y, location.getZ() - offset + x).getBlock().getType().equals(Material.BEDROCK)) && y > -50
            ) {
                Material placingMaterial;
                if (x == 0 || x == offset * 2) {
                    placingMaterial = Material.DARK_OAK_LOG;
                } else if (x % 5 == 0 || y % 11 == 0) {
                    placingMaterial = Material.DARK_OAK_PLANKS;
                } else {
                    placingMaterial = Material.COBBLESTONE;
                }
                world.getBlockAt(new Location(world,location.getX() + offset,location.getY() - yOffset + y, location.getZ() - offset + x)).setType(placingMaterial);
                y--;
            }
        }

    }

    /**
     * Check if the main spawner is still there to determine if raided
     * Check if the main spawner is still there to determine if raided
     * @return a boolean of whether or not the outpost has been raided
     */
    public boolean checkRaided(BlockBreakEvent breakEvent) {
        if (!breakEvent.getBlock().getLocation().equals(location)) {
            return false;
        }

        //When the block broken is a spawner then it is no longer active
        isActive = !breakEvent.getBlock().getType().equals(Material.SPAWNER);

        if (!isActive) {
            remove();
            save();
        }
        return !isActive;
    }

    /**
     * Declare a nearby outpost as destroyed
     */
    public void showBaseDestroyed() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(location) < 300) {
                final Component mainTitle = Component.text("OUTPOST DESTROYED", NamedTextColor.WHITE);
                final Component subtitle = Component.text("Patrols will no longer spawn here", NamedTextColor.GRAY);

                // Creates a simple title with the default values for fade-in, stay on screen and fade-out durations
                final Title title = Title.title(mainTitle, subtitle);

                // Send the title to your audience
                p.showTitle(title);
                //Title("OUTPOST DESTROYED", "PillagerOutpost will no longer produce patrols");
                //p.resetTitle();

            }
        }
    }
}
