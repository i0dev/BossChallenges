package com.i0dev.bosschallenges.entity;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import com.i0dev.bosschallenges.util.Cuboid;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.PlaceholderSetting;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@ToString
public class ActivePortal extends Entity<ActivePortal> {

    public static ActivePortal get(Object oid) {
        return ActivePortalColl.get().get(oid);
    }

    @Setter
    public UUID uuid;
    @Setter
    public PS poralBlockPS;
    @Setter
    public String originalBlockDataString;
    @Setter
    public String challengeItemId;
    @Setter
    public long duration = -100;
    @Setter
    public UUID ownerUUID;
    @Setter
    public UUID sessionUUID;

    public transient Location portalBlockLocation;
    public transient Hologram hologram;
    public transient Cuboid teleportRegion;
    public transient ChallengeItemConf challengeItem;
    public transient BlockData originalBlockData;

    @Setter
    public transient Session session;

    @Override
    public ActivePortal load(@NotNull ActivePortal that) {
        super.load(that);
        this.uuid = that.uuid;
        this.duration = that.duration;
        this.poralBlockPS = that.poralBlockPS;
        this.originalBlockDataString = that.originalBlockDataString;
        this.challengeItemId = that.challengeItemId;
        this.ownerUUID = that.ownerUUID;
        this.sessionUUID = that.sessionUUID;
        return this;
    }

    /**
     * Initialize the portal, this will create the hologram
     */
    public void initialize() {
        this.challengeItem = ChallengeItemConf.get(challengeItemId);
        this.duration = this.duration == -100 ? challengeItem.getDuration() : this.duration;
        this.session = Session.get(sessionUUID.toString());
        this.originalBlockData = Bukkit.createBlockData(originalBlockDataString);
        this.portalBlockLocation = poralBlockPS.asBukkitLocation();
        this.hologram = BossChallengesPlugin.get().getHolographicDisplays().createHologram(portalBlockLocation.clone().add(0.5, 3.5, 0.5));
        this.hologram.setPlaceholderSetting(PlaceholderSetting.ENABLE_ALL);
        this.hologram.getLines().appendItem(new ItemStack(challengeItem.getHologramMaterial()));
        for (String line : challengeItem.getHologramLines()) {
            this.hologram.getLines().appendText(Utils.color(line
                    .replace("%player%", Bukkit.getOfflinePlayer(ownerUUID).getName())
                    .replace("%time%", "{papi: bosschallenges_portal_countdown_" + this.getId() + "}")
            ));
        }
        this.teleportRegion = new Cuboid(portalBlockLocation, portalBlockLocation.clone().add(0, challengeItem.getBlocksAbovePortalForHologram(), 0));
    }

    /**
     * Creates a new active portal at the given location with the given duration.
     *
     * @param location the location to create the portal at
     */
    public static void createNewActivePortal(Location location, Player owner, String challengeItemId) {
        UUID portalUUID = UUID.randomUUID();
        UUID sessionUUID = UUID.randomUUID();

        ActivePortal activePortal = ActivePortalColl.get().create(portalUUID.toString());

        activePortal.setUuid(portalUUID);
        activePortal.setPoralBlockPS(PS.valueOf(location));
        activePortal.setOriginalBlockDataString(location.getBlock().getBlockData().getAsString());
        activePortal.setChallengeItemId(challengeItemId);
        activePortal.setOwnerUUID(owner.getUniqueId());
        activePortal.setSessionUUID(sessionUUID);

        activePortal.initialize();
        activePortal.changed();

        Utils.runCommands(activePortal.getChallengeItem().getCommandsToRunOnPortalPlace(), owner);

        location.getBlock().setBlockData(MConf.get().getPortalBlockMaterial().createBlockData());
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1);
        location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
        location.getWorld().playSound(location, Sound.BLOCK_PORTAL_AMBIENT, SoundCategory.AMBIENT, 1f, 1);

        Session.createNewSession(activePortal);
    }

    public void decreaseTime(long amount) {
        long newTime = this.duration - amount;
        if (newTime <= 0) {
            this.closePortal();
            return;
        }

        this.duration -= amount;
        this.changed();
    }

    public void closePortal() {
        Utils.runCommands(this.challengeItem.getCommandsToRunOnPortalBreak(), null);
        if (this.session != null) session.start();

        delete();
    }

    public void delete() {
        this.hologram.delete();
        this.portalBlockLocation.getWorld().playSound(this.portalBlockLocation, Sound.BLOCK_GLASS_BREAK, 0.5F, 1.0F);
        this.portalBlockLocation.getBlock().setBlockData(this.originalBlockData);
        this.detach();
    }


    /**
     * Get the active portal by the location of the portal block
     *
     * @param location the location of the portal block
     * @return the active portal or null if not found
     */
    public static ActivePortal getActivePortalByLocation(Location location) {
        return ActivePortalColl.get().getAll().stream()
                .filter(activePortal -> activePortal.getPortalBlockLocation().equals(location))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the active portal by the location of the player
     *
     * @param location the location of the player
     * @return the active portal or null if not found
     */
    public static ActivePortal getActiveCuboidByCuboid(Location location) {
        return ActivePortalColl.get().getAll().stream()
                .filter(activePortal -> activePortal.getTeleportRegion().contains(location))
                .findFirst()
                .orElse(null);
    }

}
