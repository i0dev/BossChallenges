package com.i0dev.bosschallenges.entity;

import com.i0dev.bosschallenges.Integration.WorldEditIntegration;
import com.i0dev.bosschallenges.entity.config.ChallengeItem;
import com.i0dev.bosschallenges.entity.config.MythicEntity;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class Session extends Entity<Session> {

    public transient static final int SESSION_X_AXIS_OFFSET = 2048;
    public transient static final int SESSION_Z_AXIS_OFFSET = 2048;

    public static Session get(Object oid) {
        return SessionColl.get().get(oid);
    }

    @Setter
    public UUID uuid;
    @Setter
    public UUID portalUUID;
    @Setter
    public PS spawnPS;
    @Setter
    public String challengeItemId;
    @Setter
    public List<MythicEntity> toSpawn = new ArrayList<>();

    public Set<UUID> players = new HashSet<>();
    public Set<UUID> mobs = new HashSet<>();
    public long startTime;

    public transient Location spawnLocation;
    public transient ActivePortal portal;
    public transient ChallengeItem challengeItem;

    @Override
    public Session load(@NotNull Session that) {
        super.load(that);
        this.uuid = that.uuid;
        this.portalUUID = that.portalUUID;
        this.spawnPS = that.spawnPS;
        this.players = that.players;
        this.mobs = that.mobs;
        this.startTime = that.startTime;
        this.challengeItemId = that.challengeItemId;
        this.toSpawn = that.toSpawn;
        return this;
    }

    public void initialize() {
        this.spawnLocation = spawnPS.asBukkitLocation();
        this.portal = ActivePortal.get(portalUUID);
        this.challengeItem = ChallengeItem.getChallengeItemById(challengeItemId);
    }

    /**
     * @return if the session has started
     */
    public boolean isStarted() {
        return startTime != 0;
    }

    /**
     * Remove a player from the session
     *
     * @param uuid The UUID of the player to remove
     */
    public void removePlayer(UUID uuid) {
        players.remove(uuid);
        this.changed();
    }

    /**
     * Add a mob to the session
     *
     * @param uuid The UUID of the mob to add
     */
    public void addEntity(UUID uuid) {
        mobs.add(uuid);
        this.changed();
    }

    /**
     * Remove a mob from the session
     *
     * @param uuid The UUID of the mob to remove
     */
    public void removeEntity(UUID uuid) {
        mobs.remove(uuid);
        this.changed();
    }

    /**
     * Create a new session for a portal
     *
     * @param portal The portal to create a session for
     */
    public static void createNewSession(ActivePortal portal) {
        Session session = SessionColl.get().create(portal.getSessionUUID().toString());

        session.setUuid(portal.getSessionUUID());
        session.setPortalUUID(portal.getUuid());
        session.setSpawnPS(PS.valueOf(getNewSchemLocation(new Location(Bukkit.getWorld(MConf.get().getSessionWorldName()), 0, 0, 0))));
        session.setChallengeItemId(portal.getChallengeItemId());
        session.setToSpawn(new ArrayList<>(ChallengeItem.getChallengeItemById(portal.getChallengeItemId()).getMythicEntities()));

        session.initialize();

        portal.setSession(session);

        WorldEditIntegration.pasteSchematicAtLocation(session.getSpawnLocation(), portal.getChallengeItem().getSchematicName());
    }

    public void start() {
        players.addAll(spawnLocation.getWorld().getNearbyEntities(spawnLocation, 100, 100, 100, entity -> entity instanceof Player).stream().map(entity -> ((Player) entity).getUniqueId()).toList());

        if (players.isEmpty()) {
            stop();
        }

        this.startTime = System.currentTimeMillis();
        this.changed();
    }

    public void stop() {
        players.forEach(playerUUID -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                Utils.runCommand("spawn %player%", player);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.75f, 1);
            } else {
                MData.get().addPlayerToSpawnOnLogin(playerUUID);
            }
        });

        mobs.forEach(entityUUID -> {
            org.bukkit.entity.Entity entity = Bukkit.getEntity(entityUUID);
            if (entity != null) {
                entity.remove();
            }
        });

        Utils.runCommand("bc Stopped session " + uuid.toString(), null);

        if (portal != null) portal.detach();
        detach();
    }


    /**
     * Get a session by the spawn location
     *
     * @param location The location to get the session from
     * @return The session if found, null otherwise
     */
    public static Session getSessionBySpawnLocation(Location location) {
        return SessionColl.get().getAll().stream()
                .filter(session -> location.equals(session.getSpawnLocation()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a session by a player
     *
     * @param player The player to get the session from
     * @return The session if found, null otherwise
     */
    public static Session getSessionByPlayer(Player player) {
        return SessionColl.get().getAll().stream()
                .filter(session -> session.getPlayers().contains(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a new schematic location for a session
     *
     * @param originalLocation The original location to get a new location from
     * @return The new location
     */
    public static Location getNewSchemLocation(Location originalLocation) {
        Location location = originalLocation.clone();
        if (getSessionBySpawnLocation(location) != null)
            return getNewSchemLocation(location.add(SESSION_X_AXIS_OFFSET, 0, SESSION_Z_AXIS_OFFSET));
        return location;
    }


}
