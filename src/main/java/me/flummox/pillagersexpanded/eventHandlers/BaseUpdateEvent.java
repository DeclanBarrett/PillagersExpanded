package me.flummox.pillagersexpanded.eventHandlers;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BaseUpdateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final int interval;

    public BaseUpdateEvent(int interval) {
        this.interval = interval;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() { return HANDLERS; }

    public int getInterval() {
        return this.interval;
    }
}
