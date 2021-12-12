package me.flummox.pillagersexpanded;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.StructureType;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.swing.plaf.synth.SynthUI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Creates patrols that are persistent and wander around the map
 */
public class PillagerExpandedHelper {

    private static DataManager data;
    private static Server server;
    private static HashMap<String, Integer> patrolIllagers;

    protected PillagerExpandedHelper() {

    }

    private static class SingletonHolder {
        private final static PillagerExpandedHelper INSTANCE = new PillagerExpandedHelper();
    }

    public static PillagerExpandedHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setup(DataManager data, Server server, HashMap<String, Integer> patrolIllagers) {
        this.data = data;
        this.server = server;
        this.patrolIllagers = patrolIllagers;
    }


    /*
      Create patrols based on timing from  (configuration needed)
          Create  patrols based on presence (configuration off)

     Change location of patrols based on timing

     Spawn patrols based on presence (within the despawn distance)

     Patrol survives if Patrol leader survives
        Patrol leader gets a custom name
        Patrol leader gets a random trait
            Leader (patrol gains extra level)
            Strong (all spawned get Strength effect)
            Swift (rate of travel increases between destinations)
            Scorched (adds blazes to patrol)
            Undead (adds zombies to patrol)

        Patrol gain a level

        Example: Grognac the Leader Captain. Reuben the Strong Swift Stealthy Captain

     Spawning interrupts moving to a village - chooses new village once despawned

     Patrols move between villagers within the wander area

     Patrols gain a level for each village reached
          The villagers pay tribute rather than being destroyed
          Lore being that a raid is reparations for dead Illagers

    Integration with SaberFactions - Patrols only spawn outside faction areas

    Pillager outposts spawn Pillager patrols
    If no pillager outposts exist in the wandering range then a pillager camp spawns at a random location




     Custom commands

     /pillagercamps
     /currentpatrols



     if pillager captain is caught in a chunk unload
        remove and add the patrol back to the moving
        set pillager captains to be persistent so that they can get caught in a chunk unload




    */


    //Updates the location of a patrol
    public void updatePatrolLocation(int patrolNumber, int timeInterval) {

        //Gets the current location and puts it in a point
        System.out.println("Updating Patrol Locations");
        int currentX = data.getConfig().getInt("currentPatrols." + patrolNumber + "." + "currentX");
        int currentZ = data.getConfig().getInt("currentPatrols." + patrolNumber + "." + "currentZ");
        Point currentP = new Point(currentX, currentZ);

        //Gets the current destination
        int destinationX = data.getConfig().getInt("currentPatrols." + patrolNumber + "." + "destinationX");
        int destinationZ = data.getConfig().getInt("currentPatrols." + patrolNumber + "." + "destinationZ");
        Point destinationP = new Point(destinationX, destinationZ);

        //Gets the current progress towards the destination
        double currentProgress = data.getConfig().getDouble("currentPatrols." + patrolNumber + "." + "currentProgress");
        //Gets the speed that was calculated at the start of the journey (how far to move each increment)
        double speed = data.getConfig().getDouble("currentPatrols." + patrolNumber + "." + "speed");

        //New progress (1200 is one minute (20 ticks a second * 60 seconds)
        double newProgress = currentProgress + speed * (timeInterval/1200);
        //Set the current progress to the new progress
        data.getConfig().set("currentPatrols." + patrolNumber + "." + "currentProgress", newProgress);

        //Move alone the line by percentage (progress is based 0-1)
        currentP = currentP.moveTo(currentProgress + speed, destinationP);

        //If the distance to 0 is less than 3 chunks then get a new location
        if (currentP.dist(destinationP) < 3) {
            System.out.println("Finding New Location");
            //Sets the current patrol location to exactly the destination
            //data.getConfig().set("currentPatrols." + patrolNumber + "." + "currentX", Math.floor(currentP.x));

            //add to the level since it has reached a new destination
            int level = data.getConfig().getInt("currentPatrols." + patrolNumber + "." + "level");
            level += 1;
            data.getConfig().set("currentPatrols." + patrolNumber + "." + "level", level);

            //Locate a pillager outpost to assign to the destination
            Location destinationOutpost = null;

            //Send the patrol to a village every second visit
            if (level % 2 == 0) {
                destinationOutpost = villagerOutpost(currentX * 16, currentZ * 16);
            } else {
                destinationOutpost = pillagerOutpost();
            }

            //Create a destination point with integers
            destinationP = new Point((int) destinationOutpost.getX(), (int) destinationOutpost.getZ());

            //The distance between the current point and the destination found
            double distance = currentP.dist(destinationP);

            //The distance is in chunks (thus multiplying it by 2 means that it will move 2 chunks a minute)
            speed = 2/distance;

            //Make sure speed is not greater than 1 since if greater than 1 it will go over the destination immediately
            if (speed > 1) {
                speed = 1;
            }

            //Set the current progress to 0 again and the new speed
            data.getConfig().set("currentPatrols." + patrolNumber + "." + "currentProgress", 0);
            data.getConfig().set("currentPatrols." + patrolNumber + "." + "speed", speed);

            //Set the destination as the location and
            data.getConfig().set("currentPatrols." + patrolNumber + "." + "destinationX", Math.floor(destinationP.x/16));
            data.getConfig().set("currentPatrols." + patrolNumber + "." + "destinationZ", Math.floor(destinationP.y/16));
            System.out.println("Acquired New Target");
        }

        //Choose the closest distance to base the new coordinates off of
        data.getConfig().set("currentPatrols." + patrolNumber + "." + "currentX", Math.floor(currentP.x));
        data.getConfig().set("currentPatrols." + patrolNumber + "." + "currentZ", Math.floor(currentP.y));

        data.saveConfig();


    }

    //Create a patrol at a pillager outpost
    public void createPatrol(int x, int z) {
        System.out.println("Create Patrol");
        Location patrolSpawnLocation = new Location(server.getWorld("world"), x, 0, z);

        Integer patrolNumber = Collections.max(data.getConfig().getConfigurationSection("currentPatrols").getKeys(false).stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toSet()));

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

        int rnd = new Random().nextInt(localNames.size() - 1);

        double startingLocationX = (int) Math.floor(patrolSpawnLocation.getX()/16);
        double startingLocationZ = (int) Math.floor(patrolSpawnLocation.getZ()/16);

        String currentPatrolString = "currentPatrols." + (patrolNumber + 1) + ".";
        data.getConfig().set(currentPatrolString + "currentX", startingLocationX);
        data.getConfig().set(currentPatrolString + "currentZ", startingLocationZ);
        data.getConfig().set(currentPatrolString + "destinationX", 0);
        data.getConfig().set(currentPatrolString + "destinationZ", 0);
        data.getConfig().set(currentPatrolString + "leaderName", localNames.get(rnd));

        ArrayList<String> traits = new ArrayList<>();
        data.getConfig().set(currentPatrolString + "traits", traits);
        data.saveConfig();


        Point currentP = new Point(startingLocationX, startingLocationZ);
        Point destinationP = new Point(0, 0);
        double distance = currentP.dist(destinationP);

        double speed = 2/distance;

        data.getConfig().set(currentPatrolString + "currentProgress", 0);
        data.getConfig().set(currentPatrolString + "level", 1);
        //Speed is how many chunks moved an update (every minute)
        data.getConfig().set(currentPatrolString + "speed", speed);
        data.saveConfig();
    }

    /**
     * Find a pillager outpost within the configured range
     * @return the location fo the pillager outpost
     */
    public Location pillagerOutpost() {
        System.out.println("Finding a pillager outpost");
        //Get the current amount of patrols
        Integer xLocation = (int) (Math.random() * 40000 - 20000); //$
        Integer yLocation = (int) (Math.random() * 40000 - 20000);

        Location patrolSpawnLocation = null;

        //looks for an outpost till it is guaranteed to have one
        do {
            patrolSpawnLocation = server.getWorld("world").locateNearestStructure(
                    new Location(server.getWorld("world"), xLocation, 144, yLocation),
                    StructureType.PILLAGER_OUTPOST,
                    5000, false); //$
        } while (patrolSpawnLocation == null);

        System.out.println(patrolSpawnLocation);

        return patrolSpawnLocation;
    }

    /**
     * Find a pillager outpost within the configured range
     * @return the location fo the pillager outpost
     */
    public Location villagerOutpost(Integer currentX, Integer currentZ) {
        System.out.println("Finding a pillager outpost");
        //Get the current amount of patrolscurrentX
        Integer xLocation = (int) (Math.random() * 5000 - (2500 + currentX)); //$
        Integer yLocation = (int) (Math.random() * 5000 - (2500 + currentZ));

        Location patrolSpawnLocation = null;

        //looks for an outpost till it is guaranteed to have one
        do {
            patrolSpawnLocation = server.getWorld("world").locateNearestStructure(
                    new Location(server.getWorld("world"), xLocation, 144, yLocation),
                    StructureType.VILLAGE,
                    1000, false); //$
        } while (patrolSpawnLocation == null);

        System.out.println(patrolSpawnLocation);

        return patrolSpawnLocation;
    }


    /**
     * Attempts to spawn a patrol given the patrol number and the location to spawn it
     * @param spawnPatrolLocation The location to spawn the patrol at (needs to be within 70 blocks of a player)
     * @param patrolIndex The number of the patrol to spawn (patrols are referenced by their number)
     * @return The patrol index and patrol leader (entity) if they have been spawned
     */
    public static HashMap<Integer, Entity> spawnPatrol(Location spawnPatrolLocation, int patrolIndex) {

        System.out.println("Attempting to spawn a patrol");

        //Check whether the chunk it is attempting to spawn in is loaded
        HashMap<Integer, Entity> returnHash = new HashMap<>();
        if (server.getWorld("world").getChunkAt((int) spawnPatrolLocation.getX(), (int) spawnPatrolLocation.getZ()).isLoaded()) {

            //If loaded then check if there are players within a radius
            boolean spawnCausePlayerInRange = false;
            for (Player players: Bukkit.getOnlinePlayers()) {
                double location = players.getLocation().distance(spawnPatrolLocation);
                if (location < 70) {
                    spawnCausePlayerInRange = true;
                }
            }

            //Will stop if the player not in range
            if (!spawnCausePlayerInRange) {
                System.out.println("Refused to spawn as no players nearby");
                return returnHash;
            }
        } else {
            System.out.println("Refused to spawn as the chunk is not loaded");
            return returnHash;
        }

        //Begin to spawn the pillagers
        Pillager pillager = (Pillager) spawnPatrolLocation.getWorld().spawnEntity(spawnPatrolLocation, EntityType.PILLAGER);

        //Create the name
        String leaderName = data.getConfig().getString("currentPatrols." + patrolIndex + "." + "leaderName");
        leaderName += " the ";

        // Handle gathering the traits that the pillager leader has
        //######################################################

        //Retrieve the current traits from the config file
        String currentPatrolString = "currentPatrols." + patrolIndex + ".";
        List<String> currentTraits = data.getConfig().getStringList(currentPatrolString + "traits");

        //Loop through each trait to add the trait to the pillager name
        // also add any entities to the patrolIllagers dictionary so they can be spawned
        // add to the list of potion effects that will be applied to the patrol
        ArrayList<PotionEffectType> potionEffects = new ArrayList<>();

        for (String trait: currentTraits) {
            leaderName += trait + " ";
            switch (trait) {
                case "leader": // 3 Vindicators
                    int vindicators = patrolIllagers.get(EntityType.VINDICATOR.toString());
                    patrolIllagers.put(EntityType.VINDICATOR.toString(), vindicators + 3);
                    break;
                case "pillaging": // 3 Pillagers
                    int pillagers = patrolIllagers.get(EntityType.PILLAGER.toString());
                    patrolIllagers.put(EntityType.PILLAGER.toString(), pillagers + 3);
                    break;
                case "undead": // 3 Zombies
                    patrolIllagers.put(EntityType.ZOMBIE.toString(), 3);
                    break;
                case "charged": // 1 Charged Creeper
                    patrolIllagers.put(EntityType.CREEPER.toString(), 1);
                    patrolIllagers.put(EntityType.ENDERMITE.toString(), 1);
                    patrolIllagers.put(EntityType.ENDERMAN.toString(), 1);
                    break;
                case "ghastly": // 1 Ghast
                    patrolIllagers.put(EntityType.GHAST.toString(), 1);
                    break;
                case "magic": // 2 Vexes
                    patrolIllagers.put(EntityType.VEX.toString(), 2);
                    break;
                case "sticky": // 1 Large Slime
                    patrolIllagers.put(EntityType.SLIME.toString(), 1);
                    break;
                case "explosive": // 3 Creepers
                    patrolIllagers.put(EntityType.CREEPER.toString(), 3);
                    break;
                case "withering": // 2 Wither Skeletons
                    patrolIllagers.put(EntityType.WITHER_SKELETON.toString(), 2);
                    break;
                case "scorched": // 1 Blaze
                    patrolIllagers.put(EntityType.BLAZE.toString(),1);
                    break;
                case "confused": // 10 Villagers
                    patrolIllagers.put(EntityType.VILLAGER.toString(), 10);
                    break;
                case "bony": // 3 Skeletons
                    patrolIllagers.put(EntityType.SKELETON.toString(), 3);
                    break;
                case "bulky": // 1 Ravager
                    patrolIllagers.put(EntityType.RAVAGER.toString(), 1);
                    break;
                case "stupid": // 5 Iron Golems
                    patrolIllagers.put(EntityType.IRON_GOLEM.toString(), 5);
                    break;
                case "buzzing": // 5 Bees
                    patrolIllagers.put(EntityType.BEE.toString(), 5);
                    break;
                case "poisonous": // 2 Cave Spiders
                    patrolIllagers.put(EntityType.CAVE_SPIDER.toString(), 2);
                    break;
                case "annoying": // 4 Silverfish
                    patrolIllagers.put(EntityType.SILVERFISH.toString(), 4);
                    break;
                case "terrifying": // 3 Phantoms
                    patrolIllagers.put(EntityType.PHANTOM.toString(), 3);
                    break;
                case "creepy": // 3 Spiders
                    patrolIllagers.put(EntityType.SPIDER.toString(), 3);
                    break;
                case "feathery": // Slow Falling 1
                    potionEffects.add(PotionEffectType.SLOW_FALLING);
                    break;
                case "regenerating": // Regeneration 1
                    potionEffects.add(PotionEffectType.REGENERATION);
                    break;
                case "jumpy": // Jump Boost 1
                    potionEffects.add(PotionEffectType.JUMP);
                    break;
                case "stealthy": // Invisibility
                    potionEffects.add(PotionEffectType.INVISIBILITY);
                    break;
                case "strong": // Strength 1
                    potionEffects.add(PotionEffectType.INCREASE_DAMAGE);
                    break;
                case "swift": // Speed 1
                    potionEffects.add(PotionEffectType.SPEED);
                    break;
                case "resistant": // Resistance 1
                    potionEffects.add(PotionEffectType.DAMAGE_RESISTANCE);
                    break;
                case "fishy": // Water Breathing 1 + 5 Tropical Fish
                    potionEffects.add(PotionEffectType.WATER_BREATHING);
                    patrolIllagers.put(EntityType.TROPICAL_FISH.toString(), 5);
                    break;
                case "stony": //Absorption
                    potionEffects.add(PotionEffectType.ABSORPTION);
                    break;
                case "farting": //Levitation
                    potionEffects.add(PotionEffectType.LEVITATION);
                    break;
                case "raiding": //bad omen
                    potionEffects.add(PotionEffectType.BAD_OMEN);
                    break;
                case "glowing": //bad omen
                    potionEffects.add(PotionEffectType.GLOWING);
                    break;
            }
        }

        leaderName += "captain";
        pillager.setCustomName(leaderName);

        //Set the leader settings
        pillager.setPatrolLeader(true);
        pillager.getEquipment().setItemInMainHand(null);
        pillager.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60000, 1));

        //Get the destination
        int destinationX = data.getConfig().getInt("currentPatrols." + patrolIndex + "." + "destinationX");
        int destinationZ = data.getConfig().getInt("currentPatrols." + patrolIndex + "." + "destinationZ");

        //Get the highest block at the destination for the pillager to set as their patrol target
        int y = server.getWorld("world").getHighestBlockYAt(destinationX, destinationZ); //$ consolidate world
        Location loc = new Location(server.getWorld("world"), destinationX, y, destinationZ);
        pillager.setPatrolTarget(loc.getBlock()); //$ Doesnt currently work - need to find another way to have patrol moving

        pillager.setRemoveWhenFarAway(true); //$

        //Get the patrol leader to use his location
        if (pillager.isPatrolLeader())  {
            System.out.println("Instantiating a pillager patrol");
            Location location = pillager.getLocation();

            //For every entry in the local patrol illagers spawning hashmap
            for (Map.Entry <String, Integer> entry : patrolIllagers.entrySet()) {
                String entityKey = entry.getKey();
                Integer entityValue = entry.getValue();
                Integer level = data.getConfig().getInt("currentPatrols." + patrolIndex + "." + "level");
                for (Integer illagerIndex = 0; illagerIndex < (entityValue * (level * 0.75)); illagerIndex++) { //$ need to have a config that modifies the effect of levels

                    //Spawn the entity
                    LivingEntity entity = (LivingEntity) pillager.getWorld().spawnEntity(location, EntityType.valueOf(entityKey));

                    //Apply potion effects to everyone
                    for (PotionEffectType potionEffect : potionEffects) {
                        entity.addPotionEffect(new PotionEffect(potionEffect, 60000, 1));
                    }
                }
            }
        }

        returnHash.put(patrolIndex, pillager);
        return returnHash;
    }

    public static void patrolDespawner(HashMap<Integer, Entity> spawnedPatrols) {
        //Go through all pillager captains that have been spawned
        for (Map.Entry patrolEntry : spawnedPatrols.entrySet()) {
            Pillager pillager = (Pillager) patrolEntry.getValue();
            if (pillager.isDead()) {
                System.out.println("The Pillager has despawned");
                spawnedPatrols.remove(patrolEntry.getKey());
                String currentPatrolString = "currentPatrols." + patrolEntry.getKey() + ".";

                Integer random = (int) Math.random() * 5;
                if (random != 0) {
                    break;
                }


                if (!data.getConfig().getBoolean(currentPatrolString + "swift")) {
                    data.getConfig().set(currentPatrolString + "swift", true);
                } else if (!data.getConfig().getBoolean(currentPatrolString + "leader")) {
                    data.getConfig().set(currentPatrolString + "leader", true);
                } else if (!data.getConfig().getBoolean(currentPatrolString + "strong")) {
                    data.getConfig().set(currentPatrolString + "strong", true);
                } else if (!data.getConfig().getBoolean(currentPatrolString + "undead")) {
                    data.getConfig().set(currentPatrolString + "undead", true);
                } else if (!data.getConfig().getBoolean(currentPatrolString + "scorched")) {
                    data.getConfig().set(currentPatrolString + "scorched", true);
                }
                data.saveConfig();
                break;
            }
        }
    }

    class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Here you are. This is what you want to implement.
         * from.moveTo(0.0, to) => from
         * from.moveTo(1.0, to) => to
         *
         * @param by - from 0.0 to 1.0 (from 0% to 100%)
         * @param target - move toward target by delta
         */
        public Point moveTo(double by, Point target) {
            Point delta = target.subtract(this);
            return add(delta.dot(by));
        }

        public Point add(Point point) {
            return new Point(x + point.x, y + point.y);
        }

        public Point subtract(Point point) {
            return new Point(x - point.x, y - point.y);
        }

        public Point dot(double v) {
            return new Point(v * x, v * y);
        }

        public double dist(Point point) {
            return subtract(point).length();
        }

        public double length() {
            return Math.sqrt(x * x + y * y);
        }

        public String toString() {
            return x + ":" + y;
        }
    }

}
