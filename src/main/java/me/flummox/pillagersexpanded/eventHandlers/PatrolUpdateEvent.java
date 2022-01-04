package me.flummox.pillagersexpanded.eventHandlers;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PatrolUpdateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private int interval;

    public PatrolUpdateEvent(int interval) {
        this.interval = interval;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public int getInterval() {
        return this.interval;
    }
}
