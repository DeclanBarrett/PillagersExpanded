package me.flummox.pillagersexpanded;

import me.flummox.pillagersexpanded.commands.PatrolCommands;
import me.flummox.pillagersexpanded.eventHandlers.PatrolUpdateEvent;
import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static org.bukkit.Bukkit.getPluginManager;

public final class Main extends JavaPlugin {

    public DataManager data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setupPatrols();
        System.out.println("[PillagersExpanded] Finished Enabling");

        //Trigger the patrol update every minute
        int id = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                PatrolUpdateEvent patrolUpdate = new PatrolUpdateEvent(1200);
                getPluginManager().callEvent(patrolUpdate);
            }
        }, 120, 1200);

        PatrolCommands patrolCommands = new PatrolCommands();
        getCommand("spawnpatrol").setExecutor(patrolCommands);
        getCommand("findpatrol").setExecutor(patrolCommands);
        getCommand("findoutpost").setExecutor(patrolCommands);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PillagerEventHandler.getInstance().cleanUp();
    }

    private void setupPatrols() {
        HashMap<String, Integer> patrolIllagers = new HashMap<>();
        this.data = DataManager.getInstance();
        this.data.setupData(this);
        if (!this.data.getConfig().contains("patrols.patrolRadius")) {
            data.getConfig().set("patrols.pillager", 3);
            data.getConfig().set("patrols.vindicator", 3);
            data.getConfig().set("patrols.evoker", 0);
            data.getConfig().set("patrols.illusioner", 0);
            data.getConfig().set("patrols.ravager", 0);
            data.getConfig().set("patrols.witch", 1);
            data.getConfig().set("patrols.vex", 0);

            data.getConfig().set("patrols.wander", true);
            data.getConfig().set("patrols.patrolsPersist", true);
            data.getConfig().set("patrols.total", 25);
            data.getConfig().set("patrols.patrolRadius", 1000);

            data.saveConfig();
        }

        patrolIllagers.put("PILLAGER", this.data.getConfig().getInt("patrols.pillager"));
        patrolIllagers.put("VINDICATOR", this.data.getConfig().getInt("patrols.vindicator"));
        patrolIllagers.put("EVOKER", this.data.getConfig().getInt("patrols.evoker"));
        patrolIllagers.put("ILLUSIONER", this.data.getConfig().getInt("patrols.illusioner"));
        patrolIllagers.put("RAVAGER", this.data.getConfig().getInt("patrols.ravager"));
        patrolIllagers.put("WITCH", this.data.getConfig().getInt("patrols.witch"));
        patrolIllagers.put("VEX", this.data.getConfig().getInt("patrols.vex"));

        getServer().getPluginManager().registerEvents(PillagerEventHandler.getInstance(), this);
        PillagerEventHandler.getInstance().setup( data, getServer());
    }

}
