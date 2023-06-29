package com.i0dev.bosschallenges.engine;

import com.i0dev.bosschallenges.entity.MData;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class EngineData extends Engine {

    private static final EngineData instance = new EngineData();

    public static EngineData get() {
        return instance;
    }

    /**
     * If there are any players that need to be teleported to spawn on login, do it.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (MData.get().playersToSpawnOnLogin.contains(e.getPlayer().getUniqueId())) {
            Utils.runCommand("spawn %player%", e.getPlayer());
            MData.get().removePlayerToSpawnOnLogin(e.getPlayer().getUniqueId());
        }
    }
}
