package com.i0dev.bosschallenges.entity;

import com.i0dev.bosschallenges.entity.config.ConfigLocation;
import com.i0dev.bosschallenges.entity.config.MythicEntity;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@Setter
public class ChallengeItemConf extends Entity<ChallengeItemConf> {

    public static ChallengeItemConf get(Object oid) {
        return ChallengeItemConfColl.get().get(oid);
    }

    // Primary
    String schematicName;
    long duration; //seconds
    long maxRunTime; //seconds

    // Hologram
    Material hologramMaterial;
    double blocksAbovePortalForHologram;
    List<String> hologramLines;

    // Item
    Material material;
    String displayName;
    List<String> lore;
    boolean glow;

    // mobs
    List<MythicEntity> mythicEntities;

    // Extra
    List<String> commandsToRunOnPortalPlace;
    List<String> commandsToRunOnPortalBreak;
    List<String> commandsToRunOnEntry;
    ConfigLocation radiusToCheckForPlayersInArena;


    @Override
    public ChallengeItemConf load(@NotNull ChallengeItemConf that) {
        super.load(that);
        this.schematicName = that.schematicName;
        this.duration = that.duration;
        this.maxRunTime = that.maxRunTime;
        this.hologramMaterial = that.hologramMaterial;
        this.blocksAbovePortalForHologram = that.blocksAbovePortalForHologram;
        this.hologramLines = that.hologramLines;
        this.material = that.material;
        this.displayName = that.displayName;
        this.lore = that.lore;
        this.glow = that.glow;
        this.mythicEntities = that.mythicEntities;
        this.commandsToRunOnPortalPlace = that.commandsToRunOnPortalPlace;
        this.commandsToRunOnPortalBreak = that.commandsToRunOnPortalBreak;
        this.commandsToRunOnEntry = that.commandsToRunOnEntry;
        this.radiusToCheckForPlayersInArena = that.radiusToCheckForPlayersInArena;
        return this;
    }


    public static void example() {
        if (ChallengeItemConfColl.get().containsId("example")) return;
        ChallengeItemConf challengeItemConf = ChallengeItemConfColl.get().create("example");
        challengeItemConf.setSchematicName("leviathan.schem");
        challengeItemConf.setDuration(60);
        challengeItemConf.setMaxRunTime(60 * 60 * 12); // 12 hours
        challengeItemConf.setHologramMaterial(Material.INK_SAC);
        challengeItemConf.setBlocksAbovePortalForHologram(3.5);
        challengeItemConf.setHologramLines(MUtil.list(
                "&bLeviathan Boss Challenge",
                "&7Placed by &b%player%",
                "&7Closing in &c%time% &7seconds"
        ));
        challengeItemConf.setMaterial(Material.HEART_OF_THE_SEA);
        challengeItemConf.setDisplayName("&bLeviathan Boss Challenge");
        challengeItemConf.setLore(MUtil.list(
                "",
                "&7&oThe Leviathan is a boss challenge",
                "&7&othat is a giant squid that is",
                "&7&ofound in the ocean. It is a",
                "&7&ochallenge that requires a lot of",
                "&7&oteamwork and skill to complete.",
                "",
                "&aClick this item on the ground at spawn to start the challenge."
        ));
        challengeItemConf.setGlow(true);
        challengeItemConf.setMythicEntities(MUtil.list(
                new MythicEntity("GIANT", 1, 1, new ConfigLocation(-11, 0, -11)),
                new MythicEntity("GIANT", 1, 30, new ConfigLocation(-11, 0, -11))
        ));
        challengeItemConf.setCommandsToRunOnPortalPlace(MUtil.list("broadcast &a&l%player% has opened a Leviathan portal has opened at spawn!"));
        challengeItemConf.setCommandsToRunOnPortalBreak(MUtil.list("broadcast &a&lThe Leviathan portal has been closed!"));
        challengeItemConf.setCommandsToRunOnEntry(MUtil.list("broadcast &a&l%player% has entered the Leviathan challenge!"));
        challengeItemConf.setRadiusToCheckForPlayersInArena(new ConfigLocation(100, 100, 100));
        challengeItemConf.changed();
    }

}
