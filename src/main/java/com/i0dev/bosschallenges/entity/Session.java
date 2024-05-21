package com.i0dev.bosschallenges.entity;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import com.i0dev.bosschallenges.Integration.WorldEditIntegration;
import com.i0dev.bosschallenges.entity.config.ConfigLocation;
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

    @Setter
    public Set<Integer> toTitleAnnounce = new HashSet<>();
    @Setter
    public Set<Integer> toChatAnnounce = new HashSet<>();

    public transient Location spawnLocation;
    @Setter
    public transient ActivePortal portal;
    public transient ChallengeItemConf challengeItem;

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
        this.toTitleAnnounce = that.toTitleAnnounce;
        this.toChatAnnounce = that.toChatAnnounce;
        return this;
    }

    public void initialize() {
        this.spawnLocation = spawnPS.asBukkitLocation();
        this.portal = ActivePortal.get(portalUUID.toString());
        this.challengeItem = ChallengeItemConf.get(challengeItemId);
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
        session.setToSpawn(new ArrayList<>(ChallengeItemConf.get(portal.getChallengeItemId()).getMythicEntities()));
        session.setToChatAnnounce(new HashSet<>(MConf.get().getChatAnnouncementTimeForAllPlayersInArena()));
        session.setToTitleAnnounce(new HashSet<>(MConf.get().getTitleAnnouncementTimeForPlayersInArena()));

        session.initialize();
        session.changed();

        portal.setSession(session);

        WorldEditIntegration.pasteSchematicAtLocation(session.getSpawnLocation(), portal.getChallengeItem().getSchematicName());
    }

    public List<Player> getPlayersInArena() {
        ConfigLocation loc = challengeItem.getRadiusToCheckForPlayersInArena();
        return new ArrayList<>(spawnLocation.getWorld().getNearbyEntities(spawnLocation, loc.getX(), loc.getY(), loc.getZ(), entity -> entity instanceof Player).stream().map(entity -> (Player) entity).toList());
    }

    public boolean isLocationInArena(Location location) {
        ConfigLocation loc = challengeItem.getRadiusToCheckForPlayersInArena();

        // Go through each axis and check if the location is within the arena
        return location.getX() >= spawnLocation.getX() - loc.getX() && location.getX() <= spawnLocation.getX() + loc.getX()
                && location.getY() >= spawnLocation.getY() - loc.getY() && location.getY() <= spawnLocation.getY() + loc.getY()
                && location.getZ() >= spawnLocation.getZ() - loc.getZ() && location.getZ() <= spawnLocation.getZ() + loc.getZ();
    }

    /**
     * Starts the session
     */
    public void start() {
        players.addAll(getPlayersInArena().stream().map(Player::getUniqueId).toList());

        if (players.isEmpty()) {
            stop();
        }

        getPlayersInArena().forEach(player -> player.sendTitle(
                Utils.color(MConf.get().getTitleAnnouncementMessageForPlayersInArena()
                        .replace("%challengeName%", challengeItem.getDisplayName())),
                Utils.color(MConf.get().getSubTitleAnnouncementStart()),
                10, 40, 10));

        this.startTime = System.currentTimeMillis();
        this.changed();
    }

    /**
     * Stops the session
     */
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

    // Loop over all players in the session and check if they are within the radius of the session, if they are not remove them from the session.
    public void checkForLeftPlayers() {
        List<UUID> toRemove = new ArrayList<>();
        this.getPlayersInArena().stream().map(org.bukkit.entity.Entity::getUniqueId).forEach(uuid -> {
            if (!this.players.contains(uuid)) {
                toRemove.add(uuid);
            }
        });
        toRemove.forEach(uuid -> this.players.remove(uuid));
    }
}
