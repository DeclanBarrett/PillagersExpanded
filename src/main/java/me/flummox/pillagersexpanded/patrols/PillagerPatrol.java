package me.flummox.pillagersexpanded.patrols;

import me.flummox.pillagersexpanded.DataManager;
import me.flummox.pillagersexpanded.PillagerExpandedHelper;
import me.flummox.pillagersexpanded.Point;
import me.flummox.pillagersexpanded.bases.Base;
import me.flummox.pillagersexpanded.eventHandlers.PillagerEventHandler;
import me.flummox.pillagersexpanded.eventHandlers.UpgradeEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Level;

import java.util.*;

import static org.bukkit.Bukkit.*;

/**
 * PillagerPatrol that moves throughout the map
 */
public class PillagerPatrol {

    //The list of illagers and their quantity that should appear in a patrol
    private final HashMap<String, Integer> patrolIllagers = new HashMap<>();

    //The currently spawned leader of a patrol
    private Pillager patrolLeader;
    public Pillager getPatrolLeader() {
        return patrolLeader;
    }

    //private ArrayList<Entity> spawnedEntities = new ArrayList<>();

    //Whether or not the patrol has been spawned in the game or not
    private boolean needsRespawning;

    //The name of the leader (without being spawned)
    private String leaderName;
    public String getLeaderName() {
        return leaderName;
    }

    //Enables saving to disk
    private final DataManager data;

    //current chunk
    private int currentX;
    private int currentZ;

    //current destination chunk
    private int destinationX;
    private int destinationZ;

    //Unique identifier of the patrol
    private int patrolIdentifier;

    //base destination (not always set)
    private Base destinationPillagerBase;

    //Experience level of the patrol
    private int level;

    //Chunks moved per update
    private int speed;

    //Traits that are applied to the patrol group on spawning
    private List<String> traits;

    /**
     * Get the current location
     * @return the current location
     */
    public Location getCurrentLocation() {
        World world = getServer().getWorld("world");
        return new Location(world, currentX * 16, 100, currentZ * 16);
    }

    /**
     * Get the destination location
     * @return the destination location
     */
    public Location getDestinationLocation() {
        World world = getServer().getWorld("world");
        return new Location(world, destinationX * 16, 100, destinationZ * 16);
    }

    /**
     * Gets the current level of the patrol
     * @return integer level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Retrieves the unique identifier of the patrol
     * @return the patrol identifier which usually starts at 0 and increases
     */
    public int getPatrolIdentifier() {
        return patrolIdentifier;
    }

    /**
     * Creates a patrol from the existing patrol information in the configuration file
     * @param patrolIdentifier the unique identifier for the patrol
     */
    public PillagerPatrol(int patrolIdentifier) {
        this.patrolIdentifier = patrolIdentifier;
        this.data = DataManager.getInstance();
        patrolIllagersSetup();
        loadOnStart();
    }

    /**
     * Creates a starter patrol at the specified x and z coordinates
     * @param x location of spawn in blocks
     * @param z location of spawn in blocks
     */
    public PillagerPatrol(int x, int z) {

        //Gets the data manager instance and the world
        data = DataManager.getInstance();
        World world = getServer().getWorld("world");

        //Set this patrol to be in memory, level 1 and location
        level = 1;
        needsRespawning = false;
        speed = 1;
        traits = new ArrayList<>();
        Location patrolSpawnLocation = new Location(world, x, 0, z);

        //Set the patrol index to be larger than any other
        if (PillagerEventHandler.getInstance().getPatrols().size() > 0) {

                List<PillagerPatrol> pillagerPatrols = PillagerEventHandler.getInstance().getPatrols();
                patrolIdentifier = 0;
                for (PillagerPatrol pillagerPatrol : pillagerPatrols) {
                    if (patrolIdentifier <= pillagerPatrol.getPatrolIdentifier()) {
                        patrolIdentifier = pillagerPatrol.getPatrolIdentifier() + 1;
                    }
                }
        }

        //The possible names of pillager leaders
        String[] names = {
                "Jack", "Henry", "Grognack", "Bronson", "Pril",
                "Ooga", "Nugba", "Booga", "Gorebash", "Slasher",
                "Blood Blood", "Stinky Pete", "Peanut Butter Kevin",
                "Dirt Muncher", "Grubby Wubby", "Pew Pew Die", "General Sam",
                "General Flummox", "General Dotten", "Broken Token", "Glub", "Glib Glob",
                "Moosh", "James", "Elizabeth", "Johnson & Johnson",
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
                "Shum", "Ulmug", "Urbul", "Urul", "Ushnar", "Uzul", "Hasan",
                "Adam Conover", "Kraut", "HBomberGuy", "Coffeezilla", "Linus",
                "Alan",  "Fisher", "Gibi", "Wan", "Lilly", "Burback", "Disillusion",
                "Catplant", "Chad Chad", "Chris Ray Gun", "Dankula", "Danny", "Gonzalez",
                "Domics", "Doug Doug", "Dovahhatty", "Sparticus", "Drew", "Gooden",
                "Friendly Jordies", "Ginny Di", "Indy", "ISP", "Charlie", "Joel Haver",
                "JSchlatt", "Mark Rober", "MxR", "Nakey", "Perun", "Shoe", "Stimpee",
                "Technoblade", "Tom Scott", "Tommy Kay", "Dunkey", "Tom Nicholas"
        };

        //Random name that isnt used elsewhere ``` - run on start up and keep in memory
        List<String> localNames = Arrays.asList(names.clone());

        try {
            if (data.getPatrols().getConfigurationSection("currentPatrols") != null) {
                Set<String> keyName = Objects.requireNonNull(data.getPatrols().getConfigurationSection("currentPatrols")).getKeys(true);

                for (String s : keyName) {
                    String name = data.getPatrols().getString("currentPatrols." + keyName + "." + "leaderName");
                    if (localNames.contains(name)) {
                        localNames.remove(s);
                    }
                }
            }
        } catch (NullPointerException e) {
            getLogger().log(Level.WARNING, "No current patrols");
        }


        //Random integer for name
        int rnd = new Random().nextInt(localNames.size() - 1);

        leaderName = localNames.get(rnd);

        //Set the location from the block amounts
        currentX = (int) Math.floor(patrolSpawnLocation.getX()/16);
        currentZ = (int) Math.floor(patrolSpawnLocation.getZ()/16);

        newDestination();
        patrolIllagersSetup();
    }

    /**
     * Retrieves the illager quantities from the configuration file on creation
     */
    private void patrolIllagersSetup() {
        patrolIllagers.put("PILLAGER", this.data.getConfig().getInt("patrols.pillager"));
        patrolIllagers.put("VINDICATOR", this.data.getConfig().getInt("patrols.vindicator"));
        patrolIllagers.put("EVOKER", this.data.getConfig().getInt("patrols.evoker"));
        patrolIllagers.put("ILLUSIONER", this.data.getConfig().getInt("patrols.illusioner"));
        patrolIllagers.put("RAVAGER", this.data.getConfig().getInt("patrols.ravager"));
        patrolIllagers.put("WITCH", this.data.getConfig().getInt("patrols.witch"));
        patrolIllagers.put("VEX", this.data.getConfig().getInt("patrols.vex"));
    }

    /**
     * Spawns a patrol physically in the world
     * @return whether or not a patrol was spawned
     */
    public boolean attemptSpawningPatrol() {

        //Check whether the patrol is already spawned
        if (patrolLeader != null) {
            return false;
        }

        World world = getServer().getWorld("world");
        Location spawnLocation;

        //Check whether the chunk it is attempting to spawn in is loaded
        try {
            if (Objects.requireNonNull(world).isChunkLoaded(currentX, currentZ)) {

                //If loaded then check if there are players within a radius
                boolean spawnCausePlayerInRange = false;
                spawnLocation = new Location(world, currentX * 16, world.getHighestBlockYAt(currentX * 16, currentZ * 16) + 2, currentZ * 16);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    Location playerLocation = new Location(world, players.getLocation().getBlockX(), players.getLocation().getBlockY(), players.getLocation().getBlockZ());
                    /* Point playerPoint = new Point(players.getLocation().getBlockX(), players.getLocation().getBlockZ());
                    Point spawnLocation = new Point(currentX, currentZ);*/
                    double distanceFromLocation = playerLocation.distance(spawnLocation);
                    if (distanceFromLocation < 70) {
                        spawnCausePlayerInRange = true;
                    }
                }

                //Will stop if the player not in range
                if (!spawnCausePlayerInRange) {
                    return false;
                }
            } else {
                //Refused to spawn as the chunk is not loaded
                return false;
            }


            //Begin to spawn the pillagers
            patrolLeader = (Pillager) world.spawnEntity(spawnLocation, EntityType.PILLAGER);

            //Create the name
            StringBuilder leaderName = new StringBuilder(this.leaderName);
            leaderName.append(" the ");

            // Handle gathering the traits that the pillager leader has
            //######################################################
            List<String> currentTraits = traits;

            //Loop through each trait to add the trait to the pillager name
            // also add any entities to the patrolIllagers dictionary so they can be spawned
            // add to the list of potion effects that will be applied to the patrol
            ArrayList<PotionEffectType> potionEffectsTemp = new ArrayList<>();
            HashMap<Enchantment, Integer> enchantmentCrossbowEffectsTemp = new HashMap<>();
            HashMap<Enchantment, Integer> enchantmentAxeEffectsTemp = new HashMap<>();
            HashMap<Enchantment, Integer> enchantmentArmourEffectsTemp = new HashMap<>();
            boolean flamebow = false;
            HashMap<String, Integer> patrolIllagersTemp = patrolIllagers;

            for (String trait : currentTraits) {
                leaderName.append(trait).append(" ");
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
                    case "piercing":
                        enchantmentCrossbowEffectsTemp.put(Enchantment.PIERCING, 1);
                    break;
                    case "flaming":
                        flamebow = true;
                        break; //Flame
                    case "powerful":
                        enchantmentCrossbowEffectsTemp.put(Enchantment.MENDING, 1);
                        break; //Mending
                    case "gangster":
                        enchantmentCrossbowEffectsTemp.put(Enchantment.DURABILITY, 4);
                        break; //Unbreaking
                    case "plentiful":
                        enchantmentCrossbowEffectsTemp.put(Enchantment.MULTISHOT, 1);
                        break; //multishot
                    case "sleight of hand":
                        enchantmentCrossbowEffectsTemp.put(Enchantment.QUICK_CHARGE, 1);
                        break; //Quick Charge
                    case "sharp":
                        enchantmentAxeEffectsTemp.put(Enchantment.DAMAGE_ALL, 3);
                        break; //Sharp
                    case "searing":
                        enchantmentAxeEffectsTemp.put(Enchantment.DAMAGE_ARTHROPODS, 3);
                        break; //Fire Aspect
                    case "resourceful":
                        enchantmentAxeEffectsTemp.put(Enchantment.MENDING, 2);
                        break; //Knockback
                    case "resentful":
                        enchantmentAxeEffectsTemp.put(Enchantment.DAMAGE_UNDEAD, 2);
                        break; //Smite
                    case "light":
                        enchantmentArmourEffectsTemp.put(Enchantment.PROTECTION_FALL, 3);
                        patrolIllagersTemp.put(EntityType.ZOMBIE.toString(), 5);
                        break; //feather falling
                    case "absorbent":
                        enchantmentArmourEffectsTemp.put(Enchantment.PROTECTION_EXPLOSIONS, 3);
                        patrolIllagersTemp.put(EntityType.ZOMBIE.toString(), 5);
                        break; //Blast Protection
                    case "frozen":
                        enchantmentArmourEffectsTemp.put(Enchantment.PROTECTION_FIRE, 3);
                        patrolIllagersTemp.put(EntityType.ZOMBIE.toString(), 5);
                        break; //Heat Resistance
                    case "reinforced":
                        enchantmentArmourEffectsTemp.put(Enchantment.PROTECTION_PROJECTILE, 3);
                        patrolIllagersTemp.put(EntityType.ZOMBIE.toString(), 5);
                        break; //Projectile Protection
                    case "protected":
                        enchantmentArmourEffectsTemp.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                        patrolIllagersTemp.put(EntityType.ZOMBIE.toString(), 5);
                        break; //Protection
                    case "poor":
                        enchantmentArmourEffectsTemp.put(Enchantment.VANISHING_CURSE, 1);
                        patrolIllagersTemp.put(EntityType.ZOMBIE.toString(), 5);
                        break; //Curse of Vanishing
                }
            }

            leaderName.append("captain");

            patrolLeader.setCustomName(leaderName.toString());

            //Set the leader settings
            patrolLeader.setPatrolLeader(true);
            patrolLeader.getEquipment().setItemInMainHand(null);
            patrolLeader.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60000, 1));

            //Get the highest block at the destination for the pillager to set as their patrol target
            int y = world.getHighestBlockYAt(destinationX, destinationZ); //$ consolidate world
            Location loc = new Location(world, destinationX, y, destinationZ);
            patrolLeader.setPatrolTarget(loc.getBlock()); //$ Doesnt currently work - need to find another way to have patrol moving

            patrolLeader.setRemoveWhenFarAway(true);

            //Get the patrol leader to use his location
            if (patrolLeader.isPatrolLeader()) {

                Location location = patrolLeader.getLocation();

                //For every entry in the local patrol illagers spawning hashmap
                for (Map.Entry<String, Integer> entry : patrolIllagersTemp.entrySet()) {

                    //Spawn entity from hashmap and apply effects
                    String entityKey = entry.getKey();
                    int entityValue = entry.getValue();
                    for (int illagerIndex = 0; illagerIndex < (entityValue * (level * 0.75)); illagerIndex++) { //$ need to have a config that modifies the effect of levels
                        //getLogger().log(Level.INFO, "Spawning Illager");
                        //Spawn the entity
                        LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.valueOf(entityKey));

                        //Apply potion effects to everyone and make them last forever
                        for (PotionEffectType potionEffect : potionEffectsTemp) {
                            entity.addPotionEffect(new PotionEffect(potionEffect, 60000000, 1));
                        }

                        //Create a new crossbow and add enchantments to it
                        if (entity.getType().equals(EntityType.PILLAGER) && (!enchantmentCrossbowEffectsTemp.isEmpty() | flamebow )) {
                            ItemStack crossbow = new ItemStack(Material.CROSSBOW);
                            crossbow.addEnchantments(enchantmentCrossbowEffectsTemp);
                            Pillager pillager = (Pillager) entity;
                            pillager.getEquipment().setItemInMainHand(crossbow);

                            //Use fireworks in offhand
                            if (flamebow) {
                                ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET, 64);
                                pillager.getEquipment().setItemInOffHand(firework);

                            }
                        }

                        //Create a new axe and add enchantments to it
                        if (entity.getType().equals(EntityType.VINDICATOR) && !enchantmentAxeEffectsTemp.isEmpty()) {
                            ItemStack axe = new ItemStack(Material.IRON_AXE);
                            axe.addEnchantments(enchantmentAxeEffectsTemp);
                            Vindicator vindicator = (Vindicator) entity;
                            vindicator.getEquipment().setItemInMainHand(axe);
                            vindicator.getEquipment().setItem(EquipmentSlot.OFF_HAND, axe);
                        }

                        //Add enchanted armour to zombies since they can wear armour
                        if (entity.getType().equals(EntityType.ZOMBIE) && !enchantmentArmourEffectsTemp.isEmpty()) {
                            ItemStack helmet = new ItemStack(Material.IRON_HELMET);
                            ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                            ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
                            ItemStack boots = new ItemStack(Material.IRON_BOOTS);
                            ItemStack[] armour = {
                                    helmet,
                                    chestplate,
                                    leggings,
                                    boots
                            };
                            if (enchantmentArmourEffectsTemp.containsKey(Enchantment.PROTECTION_FALL)) {
                                boots.addEnchantments(enchantmentArmourEffectsTemp);
                                enchantmentArmourEffectsTemp.remove(Enchantment.PROTECTION_FALL);
                            }

                            for (ItemStack piece: armour) {
                                piece.addEnchantments(enchantmentArmourEffectsTemp);
                            }

                            entity.getEquipment().setItem(EquipmentSlot.HEAD, helmet);
                            entity.getEquipment().setItem(EquipmentSlot.CHEST, chestplate);
                            entity.getEquipment().setItem(EquipmentSlot.LEGS, leggings);
                            entity.getEquipment().setItem(EquipmentSlot.FEET, boots);
                        }


                    }
                }
                //return true if spawned
                return true;

            }
        } catch (NullPointerException e) {
            getLogger().log(Level.WARNING, "Sir, it appears the chunk you are trying to check does not exist");
        }
        return false;
    }

    /**
     * Move towards preset destination
     */
    public void move() {
        //Gets the current location and puts it in a point
        if (patrolLeader != null) {
           //patrolIndex + " - Spawned - CANT MOVE");
            return;
        }

        //Move diagonally towards the location
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

        //If the distance to 0 is less than 3 chunks then get a new location
        if (currentX == destinationX && currentZ == destinationZ) {
            //Sets the current patrol location to exactly the destination
            this.level += 1;
            getLogger().log(Level.INFO, String.format("The patrol at %d %d led by %s was upgraded to level %d", getCurrentLocation().getBlockX(), getCurrentLocation().getBlockZ(), getLeaderName(), getLevel()));
            newDestination();
        }
    }

    /**
     * Detect whether the patrol has arrived at its destination
     */
    public void attemptArriveAtDestination() {
        Point currentP = new Point(currentX, currentZ);
        Point destinationP = new Point(destinationX, destinationZ);
        //If the distance to 0 is less than 3 chunks then get a new location
        if (currentP.dist(destinationP) < 3) {
            //Sets the current patrol location to exactly the destination
            this.level += 1;
            getLogger().log(Level.INFO, String.format("The patrol at %d %d led by %s was upgraded to level %d", getCurrentLocation().getBlockX(), getCurrentLocation().getBlockZ(), getLeaderName(), getLevel()));
            if (destinationPillagerBase != null) {
                getPluginManager().callEvent(new UpgradeEvent(destinationPillagerBase));
            }
            newDestination();

        }
    }

    /**
     * Check if spawned
     * @return is spawned
     */
    public boolean isSpawned() {
        return patrolLeader != null;
    }

    /**
     * Sets the despawn based on the patrol leader being spawned or patrol leader dying
     * @return whether or not the patrol leader is naturally despawned, dead (true) or neither (false)
     */
    public boolean isNaturallyDespawned() {
        if (isSpawned()) {
            if (patrolLeader.isDead()) {
                naturalDespawn();
                return true;
            }
        }
        return false;
    }

    /**
     * Set the patrol leader
     * @param pillager the pillager to set as patrol leader
     */
    public void setPatrolLeader(Pillager pillager) {
        patrolLeader = pillager;
    }

    /**
     * Despawn by setting the patrol leader to null
     */
    public void naturalDespawn() {
        setPatrolLeader(null);
        //spawnedEntities = new ArrayList<>();
        needsRespawning = false;
    }

    /**
     * Upgrade the patrol traits
     */
    public void upgradePatrol() {
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

        //Enchantment Effects Pillagers
        possibleTraits.add("piercing"); //Piercing 1
        possibleTraits.add("flaming"); //Rockets
        possibleTraits.add("powerful"); //Mending
        possibleTraits.add("gangster"); //Unbreaking
        possibleTraits.add("plentiful"); //multishot
        possibleTraits.add("sleight of hand"); //Quick Charge

        //Enchantment Effects Vindicators
        possibleTraits.add("sharp"); //Sharp
        possibleTraits.add("searing"); //Fire Aspect
        possibleTraits.add("resourceful"); //Mending
        possibleTraits.add("resentful"); //Smite

        //Enchanted Armour
        possibleTraits.add("light"); //feather falling
        possibleTraits.add("absorbent"); //Blast Protection
        possibleTraits.add("frozen"); //Heat Resistance
        possibleTraits.add("reinforced"); //Projectile Protection
        possibleTraits.add("protected"); //Protection

        possibleTraits.add("poor"); //Curse of Vanishing


        //remove all of the existing traits from the possible traits so that the same trait cannot be
        //given to a pillager multiple times
        possibleTraits.removeAll(traits);

        //get the size of the possible traits and then create a random number between 0 and the highest index of possible traits
        int sizeOfAvailableTraits = possibleTraits.size() - 1;
        int randomTraitIndex = (int) Math.floor(Math.random() * sizeOfAvailableTraits);

        //get a random trait from the list of possible traits
        String randomTrait = possibleTraits.get(randomTraitIndex);

        //add the trait to the existing traits
        traits.add(randomTrait);

        getLogger().log(Level.INFO, String.format("The patrol at %d %d led by %s gained the trait %s", getCurrentLocation().getBlockX(), getCurrentLocation().getBlockZ(), getLeaderName(), randomTrait));
    }

    /**
     * Saves all data about the patrol to the disk - should only occur on server shutdown
     */
    public void saveOnExit() {
       //patrolIndex + " is saving");
        String currentPatrolString = "currentPatrols." + (this.patrolIdentifier) + ".";
        data.getPatrols().set(currentPatrolString + "currentX", this.currentX);
        data.getPatrols().set(currentPatrolString + "currentZ", this.currentZ);
        data.getPatrols().set(currentPatrolString + "destinationX", this.destinationX);
        data.getPatrols().set(currentPatrolString + "destinationZ", this.destinationZ);
        data.getPatrols().set(currentPatrolString + "leaderName", this.leaderName);
        data.getPatrols().set(currentPatrolString + "level", this.level);
        data.getPatrols().set(currentPatrolString + "speed", this.speed);
        data.getPatrols().set(currentPatrolString + "traits", this.traits);
        data.getPatrols().set(currentPatrolString + "spawned", this.needsRespawning);
        int baseID = -1;
        if (destinationPillagerBase != null) {
            baseID = destinationPillagerBase.getBaseID();
        }
        data.getPatrols().set(currentPatrolString + "destinationBaseID", baseID);
        data.savePatrols();
    }

    /**
     * Loads all the data from the disk (used upon reloading)
     */
    public void loadOnStart() {
        String currentPatrolString = "currentPatrols." + (this.patrolIdentifier) + ".";
        this.currentX = data.getPatrols().getInt(currentPatrolString + "currentX");
        this.currentZ = data.getPatrols().getInt(currentPatrolString + "currentZ");
        this.destinationX = data.getPatrols().getInt(currentPatrolString + "destinationX");
        this.destinationZ = data.getPatrols().getInt(currentPatrolString + "destinationZ");
        this.leaderName = data.getPatrols().getString(currentPatrolString + "leaderName");
        this.level = data.getPatrols().getInt(currentPatrolString + "level");
        this.speed = data.getPatrols().getInt(currentPatrolString + "speed");
        this.needsRespawning = data.getPatrols().getBoolean(currentPatrolString + "spawned");
        this.traits = data.getPatrols().getStringList(currentPatrolString + "traits");
        this.destinationPillagerBase = PillagerEventHandler.getInstance().getSpecificBase(data.getPatrols().getInt(currentPatrolString + "destinationBaseID"));
    }

    /**
     * Picks a new destination for the patrol to move to
     */
    private void newDestination() {
        //Locate a pillager base to assign to the destination
        Location destinationBaseLocation;

        //Send the patrol to a village every second visit
        if (level % 2 == 0) {
            destinationBaseLocation = PillagerExpandedHelper.getInstance().villagerOutpost(currentX * 16, currentZ * 16);
            destinationPillagerBase = null;
        } else {
            try {
                if (!DataManager.getInstance().getConfig().getBoolean("allow.outposts") && !DataManager.getInstance().getConfig().getBoolean("allow.bases")) {
                    throw new ArrayIndexOutOfBoundsException("Bases are turned off");
                }
                destinationPillagerBase = PillagerEventHandler.getInstance().getRandomBase();
                destinationBaseLocation = destinationPillagerBase.getLocation();
            } catch (ArrayIndexOutOfBoundsException e) {
                destinationPillagerBase = null;
                destinationBaseLocation = new Location(getWorld("world"), Math.random() * 5000, 144, Math.random() * 5000);
            }

        }

        //Create a destination point with integers
        destinationX = (int) (destinationBaseLocation.getX()/16);
        destinationZ = (int) (destinationBaseLocation.getZ()/16);
    }

    /**
     * Removes a patrol from the list of patrols
     */
    public void disbandPatrol() {
        String currentPatrolString = "currentPatrols." + (this.patrolIdentifier);
        data.getPatrols().set(currentPatrolString, null);
        data.savePatrols();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(getCurrentLocation()) < 300) {
                p.sendTitle("PATROL DESTROYED", "Pillager patrol was led by " + leaderName);
            }
        }
    }

    /**
     * Remove all patrols from spawn and save
     */
    public void wipeSpawnedEntities() {
        if (isSpawned()) {
            patrolLeader.remove();
            needsRespawning = true;
            patrolLeader = null;
        }
    }

    /**
     * Reload on save
     */
    public void reloadOnSave() {
        if (needsRespawning) {
            //World world = getServer().getWorld("world");
            //spawnLeader(world, new Location(world, currentX, world.getHighestBlockYAt(currentX, currentZ) ,currentZ));
            needsRespawning = false;
        }
    }
}
