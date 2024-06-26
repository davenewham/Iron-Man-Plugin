package com.n3wham.ironman.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.n3wham.ironman.Main;

public class EnableIronManEvent extends Event {

    public Main plugin;
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private boolean muted;

    public EnableIronManEvent(Main plugin, Player player, boolean muted) {
        this.player = player;
        this.muted = muted;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isMuted() {
        return muted;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}