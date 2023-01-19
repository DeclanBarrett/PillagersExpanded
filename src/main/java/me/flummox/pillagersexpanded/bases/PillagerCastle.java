package me.flummox.pillagersexpanded.bases;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.flummox.pillagersexpanded.DataManager;
import me.flummox.pillagersexpanded.enums.BaseType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.logging.Level;

import static com.sk89q.worldedit.bukkit.BukkitAdapter.adapt;
import static org.bukkit.Bukkit.getLogger;

public class PillagerCastle extends Base {

    private Clipboard clipboardCastle;
    private Location goldBlockLocation;
    //private File file = this.plugin.getDataFolder()/* figure out where to save the clipboard */;


    /**
     * Create a digital outpost
     * @param localBaseID the base id to assign to the base
     * @param localLocation the location to place the base
     */
    public PillagerCastle(int localBaseID, Location localLocation) {
        createDigitalBase(localBaseID, localLocation, BaseType.CASTLE);
        World world = location.getWorld();
        goldBlockLocation = new Location(world, location.getBlockX(), location.getBlockY() + 15, location.getBlockZ());

        try {
            int size = DataManager.getInstance().gatherCastleSchematics().size();
            clipboardCastle = DataManager.getInstance().gatherCastleSchematics().get(this.getBaseID() % (size));
        } catch (NullPointerException e) {
            getLogger().log(Level.WARNING, "Please include the schematics folder inside /plugins/pillagersExpanded with castles 0-8");
        }

        buildBase();
        save();
    }

    /**
     * Load the outpost from the config fle
     * @param baseID
     */
    public PillagerCastle(int baseID) {
        createDigitalBase(baseID);
        World world = location.getWorld();
        goldBlockLocation = new Location(world, location.getBlockX(), location.getBlockY() + 15, location.getBlockZ());
    }

    /**
     * Builds the physical outpost
     */
    public void buildBase() {
        World world = location.getWorld();

        if (isActive == true) {
            com.sk89q.worldedit.world.World editedWorld = BukkitAdapter.adapt(world);

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(editedWorld)) {
                Operation operation = new ClipboardHolder(clipboardCastle)
                        .createPaste(editSession)
                        .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                        // configure here
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }

            Block block = goldBlockLocation.getBlock();

            block.setType(Material.GOLD_BLOCK);

            world.getBlockAt(new Location(world,goldBlockLocation.getX() + 1,goldBlockLocation.getY(), goldBlockLocation.getZ())).setType(Material.ANCIENT_DEBRIS);
            world.getBlockAt(new Location(world,goldBlockLocation.getX() - 1,goldBlockLocation.getY(), goldBlockLocation.getZ())).setType(Material.ANCIENT_DEBRIS);
            world.getBlockAt(new Location(world,goldBlockLocation.getX(),goldBlockLocation.getY() + 1, goldBlockLocation.getZ())).setType(Material.ANCIENT_DEBRIS);
            world.getBlockAt(new Location(world,goldBlockLocation.getX(),goldBlockLocation.getY() - 1, goldBlockLocation.getZ())).setType(Material.ANCIENT_DEBRIS);
            world.getBlockAt(new Location(world,goldBlockLocation.getX(),goldBlockLocation.getY(), goldBlockLocation.getZ() + 1)).setType(Material.ANCIENT_DEBRIS);
            world.getBlockAt(new Location(world,goldBlockLocation.getX(),goldBlockLocation.getY(), goldBlockLocation.getZ() - 1)).setType(Material.ANCIENT_DEBRIS);
        }

        save();
    }

    /**
     * Places the obsidian around the central gold block
     * @param world current world
     * @param cubeRadius the layers of obsidian
     */
    private void placeProtectionBlocks(World world, int cubeRadius) {
        for (int x = -(cubeRadius); x <= (cubeRadius); x++) {
            for (int y = -(cubeRadius); y <= (cubeRadius); y++) {
                for (int z = -(cubeRadius); z<= (cubeRadius); z++) {
                    if (!(x <= 1 && x >= -1 && y <= 1 && y >= -1 && z <= 1 && z >= -1)) {
                        world.getBlockAt(new Location(world,location.getX() + x,location.getY() + y, location.getZ() + z)).setType(Material.OBSIDIAN);
                    }
                }
            }
        }
    }

    /**
     * Upgrades the outpost by adding obsidian around the block that needs to be destroyed
     */
    public void upgradeBase() {
        World world = location.getWorld();
        level++;
        int protectionRadius = (level+3) / 2;
        placeProtectionBlocks(world, protectionRadius);
    }


    /**
     * Check if the golden block is still there to determine if raided
     * @param breakEvent
     */
    public boolean checkRaided(BlockBreakEvent breakEvent) {
        if (!breakEvent.getBlock().getLocation().equals(goldBlockLocation)) {
            return false;
        }

        isActive = !breakEvent.getBlock().getType().equals(Material.GOLD_BLOCK);

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
                //final Component mainTitle = Component.text("CASTLE DESTROYED", NamedTextColor.WHITE);
                //final Component subtitle = Component.text("Patrols will no longer spawn here", NamedTextColor.GRAY);
                //// Creates a simple title with the default values for fade-in, stay on screen and fade-out durations
                //final Title title = Title.title(mainTitle, subtitle);
                //// Send the title to your audience
                //p.showTitle(title);
                p.sendTitle("CASTLE DESTROYED", "Patrols will no longer spawn here");
                //Title("OUTPOST DESTROYED", "PillagerOutpost will no longer produce patrols");
                //p.resetTitle();

            }
        }
    }
}
