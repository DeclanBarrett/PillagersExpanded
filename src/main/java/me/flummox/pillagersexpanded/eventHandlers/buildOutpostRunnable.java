package me.flummox.pillagersexpanded.eventHandlers;

import me.flummox.pillagersexpanded.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.logging.Level;

import static org.bukkit.Bukkit.*;

public class buildOutpostRunnable extends BukkitRunnable {

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

            {       {Material.DARK_OAK_LOG, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_LOG},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS},
                    {Material.DARK_OAK_LOG, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_LOG}},

            {       {Material.DARK_OAK_LOG, null, null, null, null, null, Material.DARK_OAK_LOG},
                    {null, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {null, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {null, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {null, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {null, null, null, null, null, null, Material.DARK_OAK_FENCE},
                    {Material.DARK_OAK_LOG, null, null, null, null, null, Material.DARK_OAK_LOG}},

    };
    Main plugin;
    Location buildLocation;

    public buildOutpostRunnable(Location buildLocation, Main plugin) {
        this.buildLocation = buildLocation;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        int offset = 3;
        World world = buildLocation.getWorld();
        for (int y = 0; y < building.length; y++) {
            for (int x = 0; x < building[y].length; x++) {
                for (int z = 0; z < building[y][x].length; z++) {
                    //Spawn the specified block at the location
                    if (building[x][y][z] != null) {
                        world.getBlockAt(new Location(world,buildLocation.getX() - offset + x,buildLocation.getY() + y, buildLocation.getZ() - offset + z)).setType(building[x][y][z]);
                    }
                }
            }
        }

    }
}
