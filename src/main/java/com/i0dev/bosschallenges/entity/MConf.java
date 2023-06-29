package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@EditorName("config")
public class MConf extends Entity<MConf> {

    protected static transient MConf i;

    public static MConf get() {
        return i;
    }

    public List<String> aliasesGrindTools = MUtil.list("challenges", "bosschallenges");

    public boolean placingNewPortalsEnabled = true;
    public List<String> allowedPortalPlacementWorlds = MUtil.list("world", "world_nether", "world_the_end");
    public Material portalBlockMaterial = Material.END_PORTAL_FRAME;
    public String sessionWorldName = "boss_sessions";
    public List<Material> blacklistedBlocksToOpenPortalAt = MUtil.list(Material.WATER, Material.LAVA, Material.AIR, Material.CHEST, Material.TRAPPED_CHEST, Material.BARRIER, Material.END_PORTAL_FRAME, Material.END_PORTAL, Material.END_GATEWAY, Material.END_ROD, Material.END_CRYSTAL, Material.DRAGON_EGG, Material.BEACON, Material.BEDROCK, Material.BARRIER, Material.COMMAND_BLOCK, Material.COMMAND_BLOCK_MINECART, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID, Material.JIGSAW, Material.MOVING_PISTON, Material.PISTON_HEAD, Material.MOVING_PISTON, Material.PISTON, Material.SPAWNER);
    public List<String> blockCommandsWhilstSessionActive = MUtil.list("spawn", "tpa", "tpahere", "warp");

    public List<Integer> titleAnnouncementTimeForPlayersInArena = MUtil.list(60, 30, 15, 10, 5, 4, 3, 2, 1);
    public List<Integer> chatAnnouncementTimeForAllPlayersInArena = MUtil.list(60, 30);
    public String titleAnnouncementMessageForPlayersInArena = "&c%challengeName%";
    public String subTitleAnnouncementMessageForPlayersInArena = "&7will start in &c%time% &7seconds!";
    public String subTitleAnnouncementStart = "&a&lStarted!";
    public String chatAnnouncementMessageForAllPlayersOnline = "&7[&c§l!&7] &c%challengeName% &7will start in &c%time% &7seconds!";

    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
