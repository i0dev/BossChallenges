package com.i0dev.bosschallenges.engine;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import com.i0dev.bosschallenges.entity.Session;
import com.massivecraft.massivecore.Engine;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class EngineSession extends Engine {

    private static final EngineSession instance = new EngineSession();

    public static EngineSession get() {
        return instance;
    }

    /**
     * If entity that dies is a player, remove them from their session.
     * If entity that dies is a mythic mob, remove it from its session.
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player player) {
            Session session = Session.getSessionByPlayer(player);
            if (session == null) return;
            if (!session.isStarted()) return;
            session.removePlayer(player.getUniqueId());
        } else {
            String sessionID = entity.getPersistentDataContainer().get(new NamespacedKey(BossChallengesPlugin.get(), "session-id"), PersistentDataType.STRING);
            if (sessionID != null) {
                Session session = Session.get(sessionID);
                if (session == null) return;
                if (!session.isStarted()) return;
                session.removeEntity(entity.getUniqueId());
            }
        }
    }


}
