package com.i0dev.bosschallenges.entity;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import com.i0dev.bosschallenges.entity.config.ChallengeItem;
import com.i0dev.bosschallenges.util.Cuboid;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import io.lumine.mythic.bukkit.utils.holograms.individual.HologramLine;
import lombok.Getter;
import lombok.Setter;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.PlaceholderSetting;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class ActivePortal extends Entity<ActivePortal> {

    public static ActivePortal get(Object oid) {
        return ActivePortalColl.get().get(oid);
    }

    @Setter
    public UUID uuid;
    @Setter
    public PS poralBlockPS;
    @Setter
    public Material originalBlockMaterial; // TODO: change this to sending block packets for the portal to players instead of changing the block
    @Setter
    public String challengeItemId;
    @Setter
    public long duration = -100;
    @Setter
    public UUID ownerUUID;

    public transient Location portalBlockLocation;
    public transient Hologram hologram;
    public transient Cuboid teleportRegion;
    public transient ChallengeItem challengeItem;

    @Override
    public ActivePortal load(@NotNull ActivePortal that) {
        super.load(that);
        this.uuid = that.uuid;
        this.duration = that.duration;
        this.poralBlockPS = that.poralBlockPS;
        this.originalBlockMaterial = that.originalBlockMaterial;
        return this;
    }

    /**
     * Initialize the portal, this will create the hologram
     */
    public void initialize() {
        this.challengeItem = ChallengeItem.getChallengeItemById(challengeItemId);
        this.duration = this.duration == -100 ? challengeItem.getDuration() : this.duration;
        this.portalBlockLocation = poralBlockPS.asBukkitLocation();
        this.hologram = BossChallengesPlugin.get().getHolographicDisplays().createHologram(portalBlockLocation.clone().add(0.5, 3.5, 0.5));
        this.hologram.setPlaceholderSetting(PlaceholderSetting.ENABLE_ALL);
        this.hologram.getLines().appendItem(new ItemStack(challengeItem.getHologramMaterial()));
        for (String line : challengeItem.getHologramLines()) {
            this.hologram.getLines().appendText(Utils.color(line
                    .replace("%player%", Bukkit.getPlayer(ownerUUID).getName())
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
        UUID uuid = UUID.randomUUID();
        ActivePortal activePortal = ActivePortalColl.get().create(uuid.toString());

        activePortal.setUuid(uuid);
        activePortal.setPoralBlockPS(PS.valueOf(location));
        activePortal.setOriginalBlockMaterial(location.getBlock().getType());
        activePortal.setChallengeItemId(challengeItemId);
        activePortal.setOwnerUUID(owner.getUniqueId());

        activePortal.initialize();

        Utils.runCommands(activePortal.getChallengeItem().getCommandsToRunOnPortalPlace(), owner);

        location.getBlock().setType(MConf.get().getPortalBlockMaterial());
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1);
        location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
        location.getWorld().playSound(location, Sound.BLOCK_PORTAL_AMBIENT, SoundCategory.AMBIENT, 1f, 1);
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
