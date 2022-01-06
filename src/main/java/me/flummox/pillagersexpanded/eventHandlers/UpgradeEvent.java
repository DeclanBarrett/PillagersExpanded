package me.flummox.pillagersexpanded.eventHandlers;

import me.flummox.pillagersexpanded.Outpost;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpgradeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Outpost outpost;

    public UpgradeEvent(Outpost outpost) {
        this.outpost = outpost;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Outpost getOutpost() {
        return this.outpost;
    }
}
