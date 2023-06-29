package com.i0dev.bosschallenges.engine;

import com.i0dev.bosschallenges.Perm;
import com.i0dev.bosschallenges.entity.ActivePortal;
import com.i0dev.bosschallenges.entity.MConf;
import com.i0dev.bosschallenges.util.ItemBuilder;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class EnginePortal extends Engine {

    private static final EnginePortal i = new EnginePortal();

    public static EnginePortal get() {
        return i;
    }

    /**
     * Handles when a player places a boss portal
     */
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

        String challengeId = ItemBuilder.getPDCValue(itemInHand, "challenge-id");
        if (challengeId == null) return;

        e.setCancelled(true);

        if (!blockFace.equals(BlockFace.UP)) {
            e.getPlayer().sendMessage("You can only place portals on the top of a block!");
            return;
        }

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

        ActivePortal.createNewActivePortal(location, e.getPlayer(), challengeId);
        e.getPlayer().sendMessage("You have placed a portal!");
        itemInHand.setAmount(itemInHand.getAmount() - 1);
        e.getPlayer().updateInventory();
    }

    /**
     * Prevents players from breaking active portals
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        Location location = e.getBlock().getLocation();
        if (ActivePortal.getActivePortalByLocation(location) != null) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("There is an active portal here! If it should not be here, please contact an admin.");
        }
    }

    /**
     * Teleports the player to the boss arena if they move into an active portal
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getTo() == null) return;
        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ() || e.getFrom().getY() != e.getTo().getY()) {
            ActivePortal activePortal = ActivePortal.getActiveCuboidByCuboid(e.getTo());
            if (activePortal == null) return;
            e.getPlayer().teleport(activePortal.getSession().getSpawnLocation());
            e.getPlayer().sendMessage("teleported to: " + activePortal.getSession().getSpawnLocation().toString());
            activePortal.getPortalBlockLocation().getWorld().playSound(activePortal.getPortalBlockLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1);
            Utils.runCommands(activePortal.getChallengeItem().getCommandsToRunOnEntry(), e.getPlayer());
        }
    }
}
