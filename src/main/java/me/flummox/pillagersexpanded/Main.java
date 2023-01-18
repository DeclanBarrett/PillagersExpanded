package me.flummox.pillagersexpanded;

import me.flummox.pillagersexpanded.commands.Commands;
import me.flummox.pillagersexpanded.eventHandlers.BaseUpdateEvent;
import me.flummox.pillagersexpanded.eventHandlers.PatrolUpdateEvent;
import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getServer;

/**
 * Main class that is the entry point for bukkit
 */
public final class Main extends JavaPlugin {

    public DataManager data;

    public static Main instance;

    /**
     * Occurs on enable of the plugin
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        gatherConfigDefaults();

        //Register events
        getServer().getPluginManager().registerEvents(PillagerEventHandler.getInstance(), this);

        if (data.getConfig().getBoolean("allow.patrols")) {
            PillagerEventHandler.getInstance().setupExistingPatrols();

            //Trigger the patrol update every minute
            getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
                PatrolUpdateEvent patrolUpdate = new PatrolUpdateEvent(data.getConfig().getInt("patrols.updateTime"));
                getPluginManager().callEvent(patrolUpdate);
            }, 120, data.getConfig().getInt("patrols.updateTime"));
        }

        //A
        if (data.getConfig().getBoolean("allow.outposts") || data.getConfig().getBoolean("allow.castles")) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
                BaseUpdateEvent baseUpdate = new BaseUpdateEvent(DataManager.getInstance().getConfig().getInt("bases.checktime"));
                getPluginManager().callEvent(baseUpdate);
            }, 120, DataManager.getInstance().getConfig().getInt("bases.checktime"));
        }



        PillagerEventHandler.getInstance().setupBases();

        if (data.getConfig().getBoolean("allow.commands")) {
            Commands commands = new Commands();
            try {
                Objects.requireNonNull(getCommand("spawnpatrol")).setExecutor(commands);
                Objects.requireNonNull(getCommand("listpatrols")).setExecutor(commands);
                Objects.requireNonNull(getCommand("listbases")).setExecutor(commands);
                Objects.requireNonNull(getCommand("findpatrol")).setExecutor(commands);
                Objects.requireNonNull(getCommand("findbase")).setExecutor(commands);
            } catch (NullPointerException e) {
                getLogger().log(Level.WARNING, "Command not found");
            }
        }


        this.getLogger().log(Level.INFO, "[PillagersExpanded] Finished Enabling");
    }

    /**
     * Occurs on disable
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PillagerEventHandler.getInstance().cleanUp();
    }

    public static Main getInstance() {
        return instance;
    }

    /**
     * Sets the config up with the default values if the config file is not set up by default
     */
    private void gatherConfigDefaults() {
        //HashMap<String, Integer> patrolIllagers = new HashMap<>();
        this.data = DataManager.getInstance();
        this.data.setupData(this);
        if (!this.data.getConfig().contains("bases.checkTime")) {
            data.getConfig().set("allow.patrols", true);
            data.getConfig().set("allow.castles", true);
            data.getConfig().set("allow.outposts", true);
            data.getConfig().set("allow.commands", true);

            data.getConfig().set("patrols.total", 25);
            data.getConfig().set("patrols.newoutofchance", 1);
            data.getConfig().set("patrols.patrolRadius", 1000);
            data.getConfig().set("patrols.updateTime", 1200);

            data.getConfig().set("patrols.pillager", 3);
            data.getConfig().set("patrols.vindicator", 3);
            data.getConfig().set("patrols.evoker", 0);
            data.getConfig().set("patrols.illusioner", 0);
            data.getConfig().set("patrols.ravager", 0);
            data.getConfig().set("patrols.witch", 1);
            data.getConfig().set("patrols.vex", 0);


            data.getConfig().set("bases.radius", 5000); //5000
            data.getConfig().set("bases.maxQuantity", 30); //1000
            data.getConfig().set("bases.checkTime", 400);

            data.saveConfig();
        }
    }
}
