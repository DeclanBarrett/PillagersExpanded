package me.flummox.pillagersexpanded;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

import static org.bukkit.Bukkit.*;

public class DataManager {

    private Main plugin;
    private FileConfiguration configConfig = null;
    private File configFile = null;
    private FileConfiguration patrolConfig = null;
    private File patrolFile = null;
    private FileConfiguration baseConfig = null;
    private File baseFile = null;
    private ArrayList<Clipboard> castleStructures = new ArrayList<>();


    public void setupData(Main plugin) {
        this.plugin = plugin;
        //save/initializes the config
        saveAllDefaults();
    }

    protected DataManager() {

    }

    private static class SingletonHolder {
        private final static DataManager INSTANCE = new DataManager();
    }

    /**
     * Gets the single data manager that handles data to and from the disk
     * @return the singleton data manager object
     */
    public static DataManager getInstance() {
        return DataManager.SingletonHolder.INSTANCE;
    }

    /**
     * Gets the castle schematics from the "schematics" folder in the data folder
     * with the castle schematics being named "castle" integer ".schem" from 0 through to 9
     * @return array of clipboards containing castle
     */
    public ArrayList<Clipboard> gatherCastleSchematics() throws NullPointerException {
        if (castleStructures.size() == 0) {
            ArrayList<File> structureFiles = new ArrayList<>();
            castleStructures = new ArrayList<>();
            File folder = new File(this.plugin.getDataFolder(), "schematics");

            for (int i = 0; i < 9; i++) {
                structureFiles.add(new File(folder, "castle" + i + ".schem"));
            }


                for (File structureFile : structureFiles) {
                    ClipboardFormat format = ClipboardFormats.findByFile(structureFile);
                    try (ClipboardReader reader = Objects.requireNonNull(format).getReader(new FileInputStream(structureFile))) {
                        castleStructures.add(reader.read());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


        }
        return castleStructures;
    }

    /**
     * Updates the config file information stored in memory from the disk
     */
    public void reloadConfigFile() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "conf.yml");

        this.configConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("conf.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.configConfig.setDefaults(defaultConfig);
        }
    }

    /**
     * Updates the patrol file information stored in memory from the disk
     */
    public void reloadPatrolFile() {
        if (this.patrolFile == null)
            this.patrolFile = new File(this.plugin.getDataFolder(), "patrols.yml");

        this.patrolConfig = YamlConfiguration.loadConfiguration(this.patrolFile);

        InputStream defaultStream = this.plugin.getResource("patrols.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.patrolConfig.setDefaults(defaultConfig);
        }
    }

    /**
     * Updates the patrol file information stored in memory from the disk
     */
    public void reloadBaseFile() {
        if (this.baseFile == null)
            this.baseFile = new File(this.plugin.getDataFolder(), "bases.yml");

        this.baseConfig = YamlConfiguration.loadConfiguration(this.baseFile);

        InputStream defaultStream = this.plugin.getResource("bases.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.baseConfig.setDefaults(defaultConfig);
        }
    }

    /**
     * Gets the configuration file that contains the settings for the plugin
     * @return configuration file
     */
    public FileConfiguration getConfig() {
        if (this.configConfig == null) {
            reloadConfigFile();
        }

        return this.configConfig;
    }

    /**
     * Gets the patrol file that contains the settings for the plugin
     * @return patrol file
     */
    public FileConfiguration getPatrols() {
        if (this.patrolConfig == null) {
            reloadPatrolFile();
        }

        return this.patrolConfig;
    }

    /**
     * Gets the base file that contains the settings for the plugin
     * @return base file
     */
    public FileConfiguration getBases() {
        if (this.baseConfig == null) {
            reloadBaseFile();
        }

        return this.baseConfig;
    }

    /**
     * Updates the disk with the config stored in memory
     */
    public void saveConfig() {
        if (this.configConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    /**
     * Updates the disk with the patrol stored in memory
     */
    public void savePatrols() {
        if (this.patrolConfig == null || this.patrolFile == null)
            return;

        try {
            this.getPatrols().save(this.patrolFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save patrol to " + this.patrolFile, e);
        }
    }

    /**
     * Updates the disk with the base stored in memory
     */
    public void saveBases() {
        if (this.baseConfig == null || this.baseFile == null)
            return;

        try {
            this.getBases().save(this.baseFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save base to " + this.baseFile, e);
        }
    }

    /**
     * Saves a default configuration file (usually used to creating it for the first time)
     */
    public void saveAllDefaults() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "conf.yml");
        }
        if (this.patrolFile == null) {
            this.patrolFile = new File(this.plugin.getDataFolder(), "patrols.yml");
        }
        if (this.baseFile == null) {
            this.baseFile = new File(this.plugin.getDataFolder(), "bases.yml");
        }

        if (!this.configFile.exists()) {
            this.plugin.saveResource("conf.yml", false);
        }
        if (!this.patrolFile.exists()) {
            this.plugin.saveResource("patrols.yml", false);
        }
        if (!this.baseFile.exists()) {
            this.plugin.saveResource("bases.yml", false);
        }
    }
}
