package me.flummox.pillagersexpanded.eventHandlers;

import me.flummox.pillagersexpanded.bases.Base;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UpgradeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Base pillagerBase;

    public UpgradeEvent(Base pillagerBase) {
        this.pillagerBase = pillagerBase;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() { return HANDLERS; }

    public Base getBase() {
        return this.pillagerBase;
    }

}
