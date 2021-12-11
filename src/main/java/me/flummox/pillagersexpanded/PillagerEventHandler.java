package me.flummox.pillagersexpanded;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.*;

public class PillagerEventHandler implements Listener {

    private final Server server;

    private HashMap<Integer, Entity> spawnedPatrols = new HashMap<Integer, Entity>();

    private DataManager data;
    private PillagerExpandedHelper pillagerExpandedHelper;
    private final int patrolSpawnTimer = 10;
    private int currentTimer = 9;

    public PillagerEventHandler(HashMap<String, Integer> patrolIllagers, DataManager data, Server server) {
        this.data = data;
        this.server = server;
        this.pillagerExpandedHelper = PillagerExpandedHelper.getInstance();
        pillagerExpandedHelper.setup(data, server, patrolIllagers);
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent event) {

        //Go through all pillager captains that have been spawned
        for (Map.Entry patrolEntry : spawnedPatrols.entrySet()) {
            Pillager pillager = (Pillager) patrolEntry.getValue();
            if (pillager.isDead()) {

                //If the pillager is in the list but the pillager is classified as dead
                //the pillager did not die from being killed - they were killed by the chunk unloading
                //meaning they survived an encounter with the player since they spawned and then despawned
                System.out.println("The Pillager has despawned");
                spawnedPatrols.remove(patrolEntry.getKey());
                String currentPatrolString = "currentPatrols." + patrolEntry.getKey() + ".";

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

                //create a list and fill with the existing traits a pillager has
                List<String> existingTraits = new ArrayList<>();
                existingTraits = data.getConfig().getStringList(currentPatrolString + "traits");

                //remove all of the existing traits from the possible traits so that the same trait cannot be
                //given to a pillager multiple times
                possibleTraits.removeAll(existingTraits);

                //get the size of the possible traits and then create a random number between 0 and the highest index of possible traits
                int sizeOfAvailableTraits = possibleTraits.size() - 1;
                int randomTraitIndex = (int) Math.floor(Math.random() * sizeOfAvailableTraits);

                //get a random trait from the the possible traits
                String randomTrait = possibleTraits.get(randomTraitIndex);

                //add the trait to the existing traits
                existingTraits.add(randomTrait);

                //save the existing traits to the patrol with the new trait added
                data.getConfig().set(currentPatrolString + "traits", existingTraits);
                data.saveConfig();
                break;
            }
        }
    }

    @EventHandler
    public void onPillagerDied(EntityDeathEvent event) {
        if (spawnedPatrols.containsValue(event.getEntity())) {
            int patrolToRemove = -1;
            for (Map.Entry <Integer, Entity> spawnedPatrols : spawnedPatrols.entrySet()) {
                if (event.getEntity() == spawnedPatrols.getValue()) {
                    patrolToRemove = spawnedPatrols.getKey();
                }
            }

            if (patrolToRemove != -1) {
                data.getConfig().set("currentPatrols." + patrolToRemove, null);
            }

            spawnedPatrols.remove(patrolToRemove);
            data.saveConfig();

        }
    }

    //Updates when patrol is updated
    @EventHandler
    public void onPatrolUpdate(PatrolUpdateEvent event) {
        System.out.println("On Patrol Triggered");
        //After a certain amount of time - create a patrol at a pillager outpost
        currentTimer++;
        if (currentTimer % patrolSpawnTimer == 0) {
            currentTimer = 0;
            PillagerExpandedHelper help = PillagerExpandedHelper.getInstance();
            Location l = help.pillagerOutpost();
            help.createPatrol((int) l.getX(), (int) l.getZ());
        }
        // No current patrols to update
        if (data.getConfig().getConfigurationSection("currentPatrols") == null) {
            System.out.println("No Patrols");
            return;
        }

        //Get all of the keys (the patrol integers) to be used
        Set<String> currentPatrols = data.getConfig().getConfigurationSection("currentPatrols").getKeys(false);

        //Loops through all the patrols
        for (String key: currentPatrols) {
            System.out.println("Patrol: " + currentPatrols);
            //If the patrol is not already spawned then update its location and attempt to spawn it
            if (!spawnedPatrols.containsKey(Integer.parseInt(key))) {
                //Update each patrols location if not spawned
                pillagerExpandedHelper.updatePatrolLocation(Integer.parseInt(key), event.getInterval());

                //Get the current location of the patrol
                int currentX = data.getConfig().getInt("currentPatrols." + Integer.parseInt(key) + "." + "currentX");
                int currentZ = data.getConfig().getInt("currentPatrols." + Integer.parseInt(key) + "." + "currentZ");

                int nonChunkX = currentX * 16;
                int nonChunkZ = currentZ * 16;
                //Get the top block at the location to place in the location
                int y = server.getWorld("world").getHighestBlockYAt(nonChunkX, nonChunkZ);
                Location spawnPatrolLocation = new Location(server.getWorld("world"), nonChunkX, y + 2, nonChunkZ);



                spawnedPatrols.putAll(PillagerExpandedHelper.getInstance().spawnPatrol(spawnPatrolLocation, Integer.parseInt(key)));

                //}
            } else {
                System.out.println("The Patrol: " + Integer.parseInt(key) + " is still spawned");
            }
        }



    }



}
