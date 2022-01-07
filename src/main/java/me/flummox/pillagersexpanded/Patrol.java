package me.flummox.pillagersexpanded;

import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
import me.flummox.pillagersexpanded.eventHandlers.UpgradeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.*;

public class Patrol {

    private HashMap<String, Integer> patrolIllagers = new HashMap<>();

    public Pillager getPatrolLeader() {
        return patrolLeader;
    }

    private Pillager patrolLeader;
    private boolean spawned;

    private DataManager data;

    private int currentX;
    private int currentZ;

    private int destinationX;
    private int destinationZ;

    private Outpost destinationOutpost;

    public int getLevel() {
        return level;
    }

    private int level;
    private int patrolIndex;

    //private double speedMultiplier;
    //private double currentSpeed;
    //private double currentProgress;
    private int speed;

    public String getLeaderName() {
        return leaderName;
    }

    private String leaderName;

    private List<String> traits;

    /**
     * Creates a patrol from the existing patrol information in the configuration file
     * @param data
     * @param patrolIndex
     */
    public Patrol(DataManager data, int patrolIndex) {
        this.patrolIndex = patrolIndex;
        this.data = DataManager.getInstance();
        patrolIllagersSetup();
        load();
    }

    /**
     * Creates a patrol
     * @param data the datamanager that handles saving and loading information
     * @param patrolIndex the identifier of the patrol
     * @param currentX current x chunk location
     * @param currentZ current z chunk location
     * @param destinationX destination x chunk location
     * @param destinationZ destination z chunk location
     * @param level power level of the patrol
     * @param leaderName
     * @param traits
     */
    public Patrol(DataManager data, int patrolIndex, int currentX, int currentZ, int destinationX, int destinationZ, int level, int speed, String leaderName, ArrayList<String> traits, boolean spawned) {
        this.data = DataManager.getInstance();
        this.patrolIndex = patrolIndex;
        this.currentX = currentX;
        this.currentZ = currentZ;
        this.destinationX = destinationX;
        this.destinationZ = destinationZ;
        this.level = level;
        //this.speedMultiplier = speedMultiplier;
        //this.currentSpeed = currentSpeed;
        //this.currentProgress = currentProgress;
        this.speed = speed;
        this.leaderName = leaderName;
        this.traits = traits;
        this.spawned = spawned;
        patrolIllagersSetup();
        save();
    }

    /**
     * Creates a starter patrol at the specified x and z coordinates
     */
    public Patrol(int x, int z) {
        data = DataManager.getInstance();
       //"Creating Patrol");
        World world = getServer().getWorld("world");
        level = 1;
        spawned = false;
        Location patrolSpawnLocation = new Location(world, x, 0, z);

        //Set the patrol index to be larger than any other
        if (data.getConfig().getConfigurationSection("currentPatrols") != null) {
            if (data.getConfig().getConfigurationSection("currentPatrols").getKeys(false) != null) {
                patrolIndex = Collections.max(data.getConfig().getConfigurationSection("currentPatrols").getKeys(false).stream()
                        .map(s -> Integer.parseInt(s))
                        .collect(Collectors.toSet())) + 1;
            }
        }

        String[] names = {
                "Jack", "Henry", "Grognack", "Bron's Son", "Pril",
                "Ooga", "Nugba", "Booga", "Gorebash", "Slasher",
                "Blood Blood", "Stinky Pete", "Peanut Butter Kevin",
                "Dirt Muncher", "Grubby Wubby", "Pew Pew Die", "General Gam",
                "General Flummox", "General Dotten", "Glub Lub", "Glib Glob",
                "Moosh Moosh", "James", "Elizabeth", "Johnson & Johnson",
                "Astrozenica", "Covid", "Pfizer", "Big Pharma", "Big Stinky",
                "Smelly Joe", "Hangry Hannibal", "Doug Doug", "Rock Smash",
                "Leaf Drop", "Plop", "Mud Gluck", "Dirt Sniffer", "Stink Sniffer",
                "Leacher", "Bark Eater", "Rock Eater", "Barnie", "Fart Gobbler",
                "Cub Clubber", "Supgugh", "Agronak", "Bat", "Bazur",
                "Brugo", "Bogrum", "Brag", "Brokil", "Bugak", "Buramog",
                "Burz", "Dubious Ox", "Dull Brain", "Dull Fish", "Dull Thumb", "Dumag",
                "Gaturn", "Ghola", "Go Rub Grubs", "Gogron", "Gorgonzola",
                "Graklak", "Graman", "Grommok", "Gull the Sea Gull", "Hanz",
                "Krognak", "Kurdan", "Kurz", "Lum", "Lumdum",
                "Luronk", "Magra", "Magub", "Maknok", "Mug",
                "Orok", "Rugdumph", "Shagol", "Shagrol", "Shobob",
                "Shum", "Ulmug", "Urbul", "Urul", "Ushnar", "Uzul"
        };

        //Random name that isnt used elsewhere
        List<String> localNames = Arrays.asList(names.clone());

        if (data.getConfig().getConfigurationSection("currentPatrols") != null) {
            Set<String> keyName = data.getConfig().getConfigurationSection("currentPatrols").getKeys(true);

            for (String s : keyName) {
                String name = data.getConfig().getString("currentPatrols." + keyName + "." + "leaderName");
                if (localNames.contains(name)) {
                    localNames.remove(s);
                }
            }
        }

        //Random integer for name
        int rnd = new Random().nextInt(localNames.size() - 1);

        currentX = (int) Math.floor(patrolSpawnLocation.getX()/16);
        currentZ = (int) Math.floor(patrolSpawnLocation.getZ()/16);
        speed = 1; //$

        leaderName = localNames.get(rnd);

        traits = new ArrayList<>();

        newDestination();
        patrolIllagersSetup();
        save();
    }

    /**
     * Retrueves the illager quantities from the configuration file
     */
    private void patrolIllagersSetup() {
       //patrolIndex + " - Setting up Patrol");
        patrolIllagers.put("PILLAGER", this.data.getConfig().getInt("patrols.pillager"));
        patrolIllagers.put("VINDICATOR", this.data.getConfig().getInt("patrols.vindicator"));
        patrolIllagers.put("EVOKER", this.data.getConfig().getInt("patrols.evoker"));
        patrolIllagers.put("ILLUSIONER", this.data.getConfig().getInt("patrols.illusioner"));
        patrolIllagers.put("RAVAGER", this.data.getConfig().getInt("patrols.ravager"));
        patrolIllagers.put("WITCH", this.data.getConfig().getInt("patrols.witch"));
        patrolIllagers.put("VEX", this.data.getConfig().getInt("patrols.vex"));
    }

    /**
     * Spawns a patrol physically in the the world
     * @return
     */
    public boolean spawnPatrol() {
        //Check whether the chunk it is attempting to spawn in is loaded
        if (patrolLeader != null) {
           //"Cannot spawn ITS ALREADY SPAWNED");
            return false;
        }

        World world = getServer().getWorld("world");
        Location spawnLocation = null;

       //patrolIndex + " Chunk is loaded " + world.isChunkLoaded(currentX, currentZ));
        if (world.isChunkLoaded(currentX, currentZ)) {

            //If loaded then check if there are players within a radius
            boolean spawnCausePlayerInRange = false;
            for (Player players : Bukkit.getOnlinePlayers()) {
                spawnLocation = new Location(world, currentX * 16, world.getHighestBlockYAt(currentX * 16, currentZ * 16) + 2, currentZ * 16);
                Location playerLocation = new Location(world, players.getLocation().getBlockX(), players.getLocation().getBlockY(), players.getLocation().getBlockZ());
                //Point playerPoint = new Point(players.getLocation().getBlockX(), players.getLocation().getBlockZ());
                //Point spawnLocation = new Point(currentX, currentZ);
                double location = playerLocation.distance(spawnLocation);
                if (location < 70) {
                    spawnCausePlayerInRange = true;
                }
            }

            //Will stop if the player not in range
            if (!spawnCausePlayerInRange) {
               //patrolIndex + " Refused to spawn as no players nearby");
                return false;
            }
        } else {
           //patrolIndex + " Refused to spawn as the chunk is not loaded");
            return false;
        }

        //Begin to spawn the pillagers
        patrolLeader = (Pillager) world.spawnEntity(spawnLocation, EntityType.PILLAGER);

        //Create the name
        String leaderName = this.leaderName;
        leaderName += " the ";

        // Handle gathering the traits that the pillager leader has
        //######################################################
        List<String> currentTraits = traits;

        //Loop through each trait to add the trait to the pillager name
        // also add any entities to the patrolIllagers dictionary so they can be spawned
        // add to the list of potion effects that will be applied to the patrol
        ArrayList<PotionEffectType> potionEffectsTemp = new ArrayList<>();
        HashMap<String, Integer> patrolIllagersTemp = patrolIllagers;
        for (String trait : currentTraits) {
            leaderName += trait + " ";
            switch (trait) {
                case "leader": // 3 Vindicators
                    int vindicators = patrolIllagers.get(EntityType.VINDICATOR.toString());
                    patrolIllagersTemp.put(EntityType.VINDICATOR.toString(), vindicators + 3);
                    break;
                case "pillaging": // 3 Pillagers
                    int pillagers = patrolIllagers.get(EntityType.PILLAGER.toString());
                    patrolIllagersTemp.put(EntityType.PILLAGER.toString(), pillagers + 3);
                    break;
                case "undead": // 3 Zombies
                    patrolIllagersTemp.put(EntityType.ZOMBIE.toString(), 3);
                    break;
                case "charged": // 1 Charged Creeper
                    patrolIllagersTemp.put(EntityType.CREEPER.toString(), 1);
                    patrolIllagersTemp.put(EntityType.ENDERMITE.toString(), 1);
                    patrolIllagersTemp.put(EntityType.ENDERMAN.toString(), 1);
                    break;
                case "ghastly": // 1 Ghast
                    patrolIllagersTemp.put(EntityType.GHAST.toString(), 1);
                    break;
                case "magic": // 2 Vexes
                    patrolIllagersTemp.put(EntityType.VEX.toString(), 2);
                    break;
                case "sticky": // 1 Large Slime
                    patrolIllagersTemp.put(EntityType.SLIME.toString(), 1);
                    break;
                case "explosive": // 3 Creepers
                    patrolIllagersTemp.put(EntityType.CREEPER.toString(), 3);
                    break;
                case "withering": // 2 Wither Skeletons
                    patrolIllagersTemp.put(EntityType.WITHER_SKELETON.toString(), 2);
                    break;
                case "scorched": // 1 Blaze
                    patrolIllagersTemp.put(EntityType.BLAZE.toString(), 1);
                    break;
                case "confused": // 10 Villagers
                    patrolIllagersTemp.put(EntityType.VILLAGER.toString(), 10);
                    break;
                case "bony": // 3 Skeletons
                    patrolIllagersTemp.put(EntityType.SKELETON.toString(), 3);
                    break;
                case "bulky": // 1 Ravager
                    patrolIllagersTemp.put(EntityType.RAVAGER.toString(), 1);
                    break;
                case "stupid": // 5 Iron Golems
                    patrolIllagersTemp.put(EntityType.IRON_GOLEM.toString(), 5);
                    break;
                case "buzzing": // 5 Bees
                    patrolIllagersTemp.put(EntityType.BEE.toString(), 5);
                    break;
                case "poisonous": // 2 Cave Spiders
                    patrolIllagersTemp.put(EntityType.CAVE_SPIDER.toString(), 2);
                    break;
                case "annoying": // 4 Silverfish
                    patrolIllagersTemp.put(EntityType.SILVERFISH.toString(), 4);
                    break;
                case "terrifying": // 3 Phantoms
                    patrolIllagersTemp.put(EntityType.PHANTOM.toString(), 3);
                    break;
                case "creepy": // 3 Spiders
                    patrolIllagersTemp.put(EntityType.SPIDER.toString(), 3);
                    break;
                case "feathery": // Slow Falling 1
                    potionEffectsTemp.add(PotionEffectType.SLOW_FALLING);
                    break;
                case "regenerating": // Regeneration 1
                    potionEffectsTemp.add(PotionEffectType.REGENERATION);
                    break;
                case "jumpy": // Jump Boost 1
                    potionEffectsTemp.add(PotionEffectType.JUMP);
                    break;
                case "stealthy": // Invisibility
                    potionEffectsTemp.add(PotionEffectType.INVISIBILITY);
                    break;
                case "strong": // Strength 1
                    potionEffectsTemp.add(PotionEffectType.INCREASE_DAMAGE);
                    break;
                case "swift": // Speed 1
                    potionEffectsTemp.add(PotionEffectType.SPEED);
                    break;
                case "resistant": // Resistance 1
                    potionEffectsTemp.add(PotionEffectType.DAMAGE_RESISTANCE);
                    break;
                case "fishy": // Water Breathing 1 + 5 Tropical Fish
                    potionEffectsTemp.add(PotionEffectType.WATER_BREATHING);
                    patrolIllagers.put(EntityType.TROPICAL_FISH.toString(), 5);
                    break;
                case "stony": //Absorption
                    potionEffectsTemp.add(PotionEffectType.ABSORPTION);
                    break;
                case "farting": //Levitation
                    potionEffectsTemp.add(PotionEffectType.LEVITATION);
                    break;
                case "raiding": //bad omen
                    potionEffectsTemp.add(PotionEffectType.BAD_OMEN);
                    break;
                case "glowing": //makes all pillagers glow hiding the leader
                    potionEffectsTemp.add(PotionEffectType.GLOWING);
                    break;
            }
        }

        leaderName += "captain";
        patrolLeader.setCustomName(leaderName);

        //Set the leader settings
        patrolLeader.setPatrolLeader(true);
        patrolLeader.getEquipment().setItemInMainHand(null);
        patrolLeader.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60000, 1));

        //Get the highest block at the destination for the pillager to set as their patrol target
        int y = world.getHighestBlockYAt(destinationX, destinationZ); //$ consolidate world
        Location loc = new Location(world, destinationX, y, destinationZ);
        patrolLeader.setPatrolTarget(loc.getBlock()); //$ Doesnt currently work - need to find another way to have patrol moving

        patrolLeader.setRemoveWhenFarAway(true); //$

        //Get the patrol leader to use his location
       //"Is patrol leader? " + patrolLeader.isPatrolLeader());
        if (patrolLeader.isPatrolLeader()) {
           //"Instantiating a pillager patrol");
            Location location = patrolLeader.getLocation();

            //For every entry in the local patrol illagers spawning hashmap
            for (Map.Entry<String, Integer> entry : patrolIllagersTemp.entrySet()) {
               //"Spawning Illagers of Type: " + entry.getKey());
                String entityKey = entry.getKey();
                Integer entityValue = entry.getValue();
                for (Integer illagerIndex = 0; illagerIndex < (entityValue * (level * 0.75)); illagerIndex++) { //$ need to have a config that modifies the effect of levels
                    //System.out.println("Spawning Illager");
                    //Spawn the entity
                    LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.valueOf(entityKey));

                    //Apply potion effects to everyone
                    for (PotionEffectType potionEffect : potionEffectsTemp) {
                        entity.addPotionEffect(new PotionEffect(potionEffect, 60000, 1));
                    }
                }
            }
            //return true if spawned
            save();
            return true;
        }
        return false;
    }

    public void move(int timeInterval) {
        //Gets the current location and puts it in a point
        if (patrolLeader != null) {
           //patrolIndex + " - Spawned - CANT MOVE");
            return;
        }

        /*
        Point currentP = new Point(currentX, currentZ);

        //Gets the current destination
        Point destinationP = new Point(destinationX, destinationZ);

        //New progress (1200 is one minute (20 ticks a second * 60 seconds)
        currentProgress = currentProgress + currentSpeed * ((double) timeInterval/2400);
        //Set the current progress to the new progress

        //Move alone the line by percentage (progress is based 0-1)
        System.out.print(patrolIndex + " Current x: " + currentP.x + ", z: " + currentP.y + " ~ ");
        currentP = currentP.moveTo(currentProgress, destinationP);
        System.out.print(" Next x: " + currentP.x + ", z: " + currentP.y);
        System.out.println(" Dest x: " + destinationX + ", z: " + destinationZ);
        currentX = (int) currentP.x;
        currentZ = (int) currentP.y;

         */
        System.out.print(patrolIndex + " Current x: " + currentX + ", z: " + currentZ);

        if (currentX < destinationX) {
            currentX += speed;
        } else if (currentX > destinationX) {
            currentX -= speed;
        }

        if (currentZ < destinationZ) {
            currentZ += speed;
        } else if (currentZ > destinationZ) {
            currentZ -= speed;
        }


        System.out.print(" Next x: " + currentX + ", z: " + currentZ);
        System.out.println(" Dest x: " + destinationX + ", z: " + destinationZ);

        //If the distance to 0 is less than 3 chunks then get a new location
        if (currentX == destinationX && currentZ == destinationZ) {
           //"Finding New Location");
            //Sets the current patrol location to exactly the destination

            this.level += 1;

            newDestination();

           //"Acquired New Target To Travel To");
        }

        save();
    }

    public void attemptArrive() {
        Point currentP = new Point(currentX, currentZ);
        Point destinationP = new Point(destinationX, destinationZ);
        //If the distance to 0 is less than 3 chunks then get a new location
        if (currentP.dist(destinationP) < 3) {
            System.out.println("Finding New Location");
            //Sets the current patrol location to exactly the destination

            this.level += 1;

            if (destinationOutpost != null) {
                getPluginManager().callEvent(new UpgradeEvent(destinationOutpost));
            }
            newDestination();

           //"Acquired New Target To Travel To");
        }
    }

    public boolean isSpawned() {
        if (patrolLeader != null) {
           //patrolIndex + " - is spawned");
            return true;
        }
        return false;
    }

    public boolean isDespawned() {
        if (patrolLeader != null) {
            if (patrolLeader.isDead()) {
                despawn();
               //patrolIndex + " - set spawned: " + spawned);
                return true;
            }
        }
        return false;
    }

    public void setPatrolLeader(Pillager pillager) {
        patrolLeader = pillager;
        save();
    }

    public void despawn() {
        setPatrolLeader(null);
        spawned = false;
    }

    public void upgradePatrol() {
       //"[Pillagers Expanded] Upgrade Patrol");
        //Stop adding traits (and move on to the next patrol) if the patrol already has maximum traits
        if (traits.size() >= level) {
            return;
        }

        //Create an empty list that will be filled with possible traits a pillager can have
        ArrayList<String> possibleTraits = new ArrayList<>();

        //Extra Spawns
        possibleTraits.add("leader"); // 3 Vindicators
        possibleTraits.add("pillaging"); // 3 Pillagers
        possibleTraits.add("undead"); // 3 Zombies
        possibleTraits.add("charged"); // 1 Charged Creeper
        possibleTraits.add("ghastly"); // 1 Ghast
        possibleTraits.add("magic"); // 2 Vexes
        possibleTraits.add("sticky"); // 1 Large Slime
        possibleTraits.add("explosive"); // 3 Creepers
        possibleTraits.add("withering"); // 2 Wither Skeletons
        possibleTraits.add("scorched"); // 1 Blaze
        possibleTraits.add("confused"); // 10 Villagers
        possibleTraits.add("bony"); // 3 Skeletons
        possibleTraits.add("bulky"); // 1 Ravager
        possibleTraits.add("stupid"); // 5 Iron Golems
        possibleTraits.add("buzzing"); // 5 Bees
        possibleTraits.add("poisonous"); // 2 Cave Spiders
        possibleTraits.add("annoying"); // 4 Silverfish
        possibleTraits.add("terrifying"); // 3 Phantoms
        possibleTraits.add("creepy"); // 3 Spiders

        //Potion Effects
        possibleTraits.add("feathery"); // Slow Falling 1
        possibleTraits.add("regenerating"); // Regeneration 1
        possibleTraits.add("jumpy"); // Jump Boost 1
        possibleTraits.add("stealthy"); // Invisibility
        possibleTraits.add("strong"); // Strength 1
        possibleTraits.add("swift"); // Speed 2
        possibleTraits.add("resistant"); // Resistance 1
        possibleTraits.add("fishy"); // Water Breathing 1 + 5 Tropical Fish
        possibleTraits.add("stony"); //Absorption 4
        possibleTraits.add("farting"); //Levitation
        possibleTraits.add("raiding"); //bad omen
        possibleTraits.add("glowing"); //glowing

        //remove all of the existing traits from the possible traits so that the same trait cannot be
        //given to a pillager multiple times
        possibleTraits.removeAll(traits);

        //get the size of the possible traits and then create a random number between 0 and the highest index of possible traits
        int sizeOfAvailableTraits = possibleTraits.size() - 1;
        int randomTraitIndex = (int) Math.floor(Math.random() * sizeOfAvailableTraits);

        //get a random trait from the the possible traits
        String randomTrait = possibleTraits.get(randomTraitIndex);

        //add the trait to the existing traits
        traits.add(randomTrait);

        //save the existing traits to the patrol with the new trait added
        save();
    }

    public void save() {
       //patrolIndex + " is saving");
        String currentPatrolString = "currentPatrols." + (this.patrolIndex) + ".";
        data.getConfig().set(currentPatrolString + "currentX", this.currentX);
        data.getConfig().set(currentPatrolString + "currentZ", this.currentZ);
        data.getConfig().set(currentPatrolString + "destinationX", this.destinationX);
        data.getConfig().set(currentPatrolString + "destinationZ", this.destinationZ);
        data.getConfig().set(currentPatrolString + "leaderName", this.leaderName);
        data.getConfig().set(currentPatrolString + "level", this.level);
        data.getConfig().set(currentPatrolString + "speed", this.speed);
        //data.getConfig().set(currentPatrolString + "currentSpeed", this.currentSpeed);
        data.getConfig().set(currentPatrolString + "traits", this.traits);
        data.getConfig().set(currentPatrolString + "spawned", this.spawned);
        int outpostID = -1;
        if (destinationOutpost != null) {
            outpostID = destinationOutpost.getOutpostID();
        }
        data.getConfig().set(currentPatrolString + "destinationOutpostID", outpostID);
        data.saveConfig();
    }

    public void load() {
       //patrolIndex + " is loading");
        String currentPatrolString = "currentPatrols." + (this.patrolIndex) + ".";
        this.currentX = data.getConfig().getInt(currentPatrolString + "currentX");
        this.currentZ = data.getConfig().getInt(currentPatrolString + "currentZ");
        this.destinationX = data.getConfig().getInt(currentPatrolString + "destinationX");
        this.destinationZ = data.getConfig().getInt(currentPatrolString + "destinationZ");
        this.leaderName = data.getConfig().getString(currentPatrolString + "leaderName");
        this.level = data.getConfig().getInt(currentPatrolString + "level");
        this.speed = data.getConfig().getInt(currentPatrolString + "speed");
        //this.currentSpeed = data.getConfig().getDouble(currentPatrolString + "currentSpeed");
        this.spawned = data.getConfig().getBoolean(currentPatrolString + "spawned");
        this.traits = data.getConfig().getStringList(currentPatrolString + "traits");
        this.destinationOutpost = PillagerEventHandler.getInstance().getSpecificOutpost(data.getConfig().getInt(currentPatrolString + "destinationOutpostID"));
    }

    private void newDestination() {
        //Locate a pillager outpost to assign to the destination
        Location destinationOutpostLocation;

        //Send the patrol to a village every second visit
        if (level % 2 == 0) {
            destinationOutpostLocation = PillagerExpandedHelper.getInstance().villagerOutpost(currentX * 16, currentZ * 16);
            System.out.println("[Pillagers Expanded] Going to a village");
            destinationOutpost = null;
        } else {
            destinationOutpost = PillagerEventHandler.getInstance().getRandomOutpost();
            System.out.println("[Pillagers Expanded] Going to an outpost");
            destinationOutpostLocation = destinationOutpost.getOutpostLocation();
            save();
        }

        //Create a destination point with integers
        destinationX = (int) (destinationOutpostLocation.getX()/16);
        destinationZ = (int) (destinationOutpostLocation.getZ()/16);

        /*
        Point destinationP = new Point(destinationX, destinationZ);
        Point currentP = new Point(currentX, currentZ);

        //The distance between the current point and the destination found
        double distance = currentP.dist(destinationP);

        //The distance is in chunks (thus multiplying it by 2 means that it will move 2 chunks a minute)
        currentSpeed = speedMultiplier/distance;

        //Make sure speed is not greater than 1 since if greater than 1 it will go over the destination immediately
        if (currentSpeed > 1) {
            currentSpeed = 1;
        }
         */
       //patrolIndex + " is getting new destination");
        //return destinationP;
    }

    public void remove() {
       //patrolIndex + " DESTROYED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String currentPatrolString = "currentPatrols." + (this.patrolIndex);
        data.getConfig().set(currentPatrolString, null);
        data.saveConfig();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(getCurrentLocation()) < 300) {
                p.sendMessage("The Nearby Patrol Led By " + leaderName + " Was Destroyed!");
            }
        }
    }

    public void cleanOnDisable() {
       //"CLEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAN");
        if (patrolLeader != null) {
            patrolLeader.remove();
            spawned = true;
            patrolLeader = null;
            save();
        }
    }

    public void reloadOnSave() {
       //"IM RELOADING!!!!!!!!!!!!!!!!!!!!!!!!!!!!! NEED AMMO NOW ");
        if (spawned == true) {
            World world = getServer().getWorld("world");
            spawnLeader(world, new Location(world, currentX, world.getHighestBlockYAt(currentX, currentZ) ,currentZ));
            spawned = false;
        }
    }

    private void spawnLeader(World world, Location spawnLocation) {

        if (world.isChunkLoaded(currentX, currentZ)) {

            //If loaded then check if there are players within a radius
            boolean spawnCausePlayerInRange = false;
            for (Player players : Bukkit.getOnlinePlayers()) {
                spawnLocation = new Location(world, currentX * 16, world.getHighestBlockYAt(currentX * 16, currentZ * 16) + 2, currentZ * 16);
                Location playerLocation = new Location(world, players.getLocation().getBlockX(), players.getLocation().getBlockY(), players.getLocation().getBlockZ());
                //Point playerPoint = new Point(players.getLocation().getBlockX(), players.getLocation().getBlockZ());
                //Point spawnLocation = new Point(currentX, currentZ);
                double location = playerLocation.distance(spawnLocation);
                if (location < 70) {
                    spawnCausePlayerInRange = true;
                }
            }

            //Will stop if the player not in range
            if (!spawnCausePlayerInRange) {
               //patrolIndex + " LEADER Refused to spawn as no players nearby");
                return;
            }
        } else {
           //patrolIndex + " LEADER Refused to spawn as the chunk is not loaded");
            return;
        }

       //"COMMAND LEADER HAS BEEN SPAWNED!!!");

        //Begin to spawn the pillagers
        patrolLeader = (Pillager) world.spawnEntity(spawnLocation, EntityType.PILLAGER);

        //Create the name
        String leaderName = this.leaderName;
        leaderName += " the ";

        // Handle gathering the traits that the pillager leader has
        //######################################################
        List<String> currentTraits = traits;

        //Loop through each trait to add the trait to the pillager name
        // also add any entities to the patrolIllagers dictionary so they can be spawned
        // add to the list of potion effects that will be applied to the patrol
        for (String trait : currentTraits) {
            leaderName += trait + " ";
        }

        leaderName += "captain";
        patrolLeader.setCustomName(leaderName);

        //Set the leader settings
        patrolLeader.setPatrolLeader(true);
        patrolLeader.getEquipment().setItemInMainHand(null);
        patrolLeader.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60000, 1));
    }

    public Location getCurrentLocation() {
        World world = getServer().getWorld("world");
        return new Location(world, currentX * 16, 100, currentZ * 16);
    }

    public Location getDestinationLocation() {
        World world = getServer().getWorld("world");
        return new Location(world, destinationX * 16, 100, destinationZ * 16);
    }
}
