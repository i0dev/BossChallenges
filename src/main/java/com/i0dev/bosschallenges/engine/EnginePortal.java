package com.i0dev.bosschallenges.engine;

import com.i0dev.bosschallenges.Perm;
import com.i0dev.bosschallenges.entity.ActivePortal;
import com.i0dev.bosschallenges.entity.ActivePortalColl;
import com.i0dev.bosschallenges.entity.MConf;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EngineChallengePlace extends Engine {

    private static final EngineChallengePlace i = new EngineChallengePlace();

    public static EngineChallengePlace get() {
        return i;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack itemInHand = e.getItem();
        Block block = e.getClickedBlock();
        BlockFace blockFace = e.getBlockFace();
        Action action = e.getAction();
        MConf mConf = MConf.get();
        if (block == null) return;
        if (block.getType().equals(Material.AIR)) return;
        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (itemInHand == null) return;
        if (!blockFace.equals(BlockFace.UP)) {
            e.getPlayer().sendMessage("You can only place portals on the top of a block!");
            return;
        }

        if (!itemInHand.getType().equals(Material.DIAMOND)) return; //TODO: remove this
        //TODO: check if it has PDC value

        e.setCancelled(true);
        if (!mConf.isPlacingNewPortalsEnabled()) {
            e.getPlayer().sendMessage("Portal placement is disabled!");
            return;
        }
        Location location = block.getLocation();
        World world = location.getWorld();
        if (world == null) return;
        if (!mConf.getAllowedPortalPlacementWorlds().contains(location.getWorld().getName())) {
            e.getPlayer().sendMessage("You cannot place a portal in this world!");
            return;
        }
        if (ActivePortal.getActivePortalByLocation(location) != null) {
            e.getPlayer().sendMessage("There is already a portal at this location!");
            return;
        }
        if (!Perm.PLACE_PORTAL.has(e.getPlayer(), true)) return;

        if (!location.clone().add(0, 1, 0).getBlock().getType().equals(Material.AIR) || !location.clone().add(0, 2, 0).getBlock().getType().equals(Material.AIR)) {
            e.getPlayer().sendMessage("Please ensure there is 2 air blocks above the block you clicked!");
            return;
        }

        createNewActivePortal(location, 60);
        //TODO: decrement item in hand
    }

    /**
     * Creates a new active portal at the given location with the given duration.
     *
     * @param location the location to create the portal at
     * @param duration the duration of the portal in seconds
     */
    public void createNewActivePortal(Location location, long duration) {
        UUID uuid = UUID.randomUUID();
        ActivePortal activePortal = ActivePortalColl.get().create(uuid.toString());

        activePortal.setUuid(uuid);
        activePortal.setDuration(duration);
        activePortal.setPoralBlockPS(PS.valueOf(location));
        activePortal.setOriginalBlockMaterial(location.getBlock().getType());

        activePortal.initialize();

        location.getBlock().setType(Material.END_PORTAL_FRAME); //TODO: make this configurable
    }

}
