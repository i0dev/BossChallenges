package com.i0dev.bosschallenges.entity;

import com.i0dev.bosschallenges.entity.config.ChallengeItem;
import com.i0dev.bosschallenges.entity.config.ConfigLocationOffset;
import com.i0dev.bosschallenges.entity.config.MythicEntity;
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

    List<ChallengeItem> challengeItems = MUtil.list(
            new ChallengeItem(
                    "leviathan",
                    "leviathan.schem",
                    60,
                    Material.INK_SAC,
                    3.5,
                    MUtil.list(
                            "&bLeviathan Boss Challenge",
                            "&7Placed by &b%player%",
                            "&7Closing in &c%time% &7seconds"
                    ),
                    Material.HEART_OF_THE_SEA,
                    "&bLeviathan Boss Challenge",
                    MUtil.list(
                            "",
                            "&7&oThe Leviathan is a boss challenge",
                            "&7&othat is a giant squid that is",
                            "&7&ofound in the ocean. It is a",
                            "&7&ochallenge that requires a lot of",
                            "&7&oteamwork and skill to complete.",
                            "",
                            "&aClick this item on the ground at spawn to start the challenge."
                    ),
                    true,
                    MUtil.list(
                            new MythicEntity("GIANT", 1, 1, new ConfigLocationOffset(-11, 0, -11)),
                            new MythicEntity("GIANT", 1, 30, new ConfigLocationOffset(-11, 0, -11))
                    ),
                    MUtil.list("broadcast &a&l%player% has opened a Leviathan portal has opened at spawn!"),
                    MUtil.list("broadcast &a&lThe Leviathan portal has been closed!"),
                    MUtil.list("broadcast &a&l%player% has entered the Leviathan challenge!")
            )
    );


    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
