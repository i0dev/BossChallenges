package com.i0dev.bosschallenges.Integration;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public class MythicMobsIntegration {

    /**
     * Spawn a mythic mob at a location
     *
     * @param location  the location to spawn the mob at
     * @param mobName   the name of the mob to spawn
     * @param sessionId the id of the session
     */
    public static Entity spawnMythicMob(Location location, String mobName, String sessionId) {
        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(mobName).orElse(null);
        if (mob != null) {
            // spawns mob
            ActiveMob knight = mob.spawn(BukkitAdapter.adapt(location), 1);

            // get mob as bukkit entity
            Entity entity = knight.getEntity().getBukkitEntity();
            entity.getPersistentDataContainer().set(new NamespacedKey(BossChallengesPlugin.get(), "session-id"), PersistentDataType.STRING, sessionId);
            return entity;
        }
        return null;
    }

}
