package me.flummox.pillagersexpanded.bases;

import me.flummox.pillagersexpanded.DataManager;
import me.flummox.pillagersexpanded.enums.BaseType;
import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public abstract class Base implements Listener{

    protected BaseType baseType;
    protected int baseID;
    protected int level;
    protected Location location;
    protected boolean isActive;

    /**
     * Declare a nearby base as destroyed
     */
    public abstract void showBaseDestroyed();

    /**
     * Check if the main block is still there and then set whether the base is active or not
     * @return isActive()
     */
    public abstract boolean checkRaided(BlockBreakEvent event);

    /**
     * Builds the physical base
     */
    public abstract void buildBase();

    /**
     * Upgrade base by adding to the base by more protection
     */
    public abstract void upgradeBase();

    /**
     * Gets the corner location of the base that the base was generated from
     * @return Location of base
     */
    public Location getLocation(){return location;}

    /**
     * Gets the current level the base is at which is determined by patrols interacting with the base
     * @return Integer level
     */
    public int getLevel(){return level;}

    /**
     * Returns the ID of the base
     * @return ID of base
     */
    public int getBaseID(){return baseID;}

    /**
     * Whether the digital base is still alive/not raided/active
     * @return True or False of active/not-active
     */
    public boolean isActive() {
        return isActive;
    }

    public String getBaseType() {
        return baseType.toString();
    }

    /**
     * Create a digital base at a location
     */
    protected void createDigitalBase(int baseID, Location baseLocation, BaseType baseType) {
        this.location = baseLocation;
        this.level = 1;
        this.baseID = baseID;
        this.isActive = true;
        this.baseType = baseType;
    }

    /**
     * Load the base from the bases file
     */
    protected void createDigitalBase(int baseID) {
        this.baseID = baseID;
        String baseString = "base." + baseID + ".";
        int currentX = DataManager.getInstance().getBases().getInt(baseString + "x");
        int currentY = DataManager.getInstance().getBases().getInt(baseString + "y");
        int currentZ = DataManager.getInstance().getBases().getInt(baseString + "z");
        this.level = DataManager.getInstance().getBases().getInt(baseString + "level");
        this.isActive = DataManager.getInstance().getBases().getBoolean(baseString + "isActive");
        this.baseType = BaseType.valueOf(DataManager.getInstance().getBases().getString(baseString + "baseType"));
        this.location = new Location(getServer().getWorld("world"), currentX, currentY, currentZ);
    }

    /**
     * Produce a digital patrol at random
     */
    public void attemptProducePatrol() {
        int chance = (int) (Math.random() * 20);
        //"Attempting to Produce PillagerPatrol");
        if (chance < level && isActive) {
            PillagerEventHandler.getInstance().createPatrol(
                    location.getBlockX(),
                    location.getBlockZ());
        }
    }

    /**
     * Save the digital base to disk
     */
    public void save() {
        FileConfiguration baseConfig = DataManager.getInstance().getBases();
        String currentBaseString = "base." + (this.baseID) + ".";
        baseConfig.set(currentBaseString + "x", this.location.getBlockX());
        baseConfig.set(currentBaseString + "y", this.location.getBlockY());
        baseConfig.set(currentBaseString + "z", this.location.getBlockZ());
        baseConfig.set(currentBaseString + "level", this.level);
        baseConfig.set(currentBaseString + "isActive", this.isActive);
        baseConfig.set(currentBaseString + "baseType", this.baseType.toString());
        DataManager.getInstance().saveBases();
    }

    /**
     * Remove a digital outpost from memory
     */
    public void remove() {
        //baseID + x" DESTROYED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String currentOutpostString = "base." + (this.getBaseID());
        DataManager data = DataManager.getInstance();
        data.getBases().set(currentOutpostString, null);
        data.saveBases();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(location) < 300) {
                this.showBaseDestroyed();
            }
        }
    }



    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        //Check if o is an instance of Complex or not
        //"null instanceof [type]" also returns false */
        if (!(o instanceof Base)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Base c = (Base) o;

        return (this.getLocation().getBlockX() == c.getLocation().getBlockX()) &&
                (this.getLocation().getBlockZ() == c.getLocation().getBlockZ());
    }

}

