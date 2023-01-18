package me.flummox.pillagersexpanded.enums;
import org.bukkit.Location;

/**
 * Pairs base type and location together
 */
public class BaseLocationPair {

    private final Location location;
    private final BaseType baseType;

    /**
     * Creates a pair of location and base type
     * @param location
     * @param baseType
     */
    public BaseLocationPair(Location location, BaseType baseType) {
        this.location = location;
        this.baseType = baseType;
    }

    /**
     * Gets the location
     * @return location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Gets the base type
     * @return base type
     */
    public BaseType getBaseType() {
        return this.baseType;
    }
}
