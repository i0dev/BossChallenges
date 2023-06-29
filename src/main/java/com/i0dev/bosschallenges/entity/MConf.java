package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.*;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

@EditorName("config")
public class MConf extends Entity<MConf> {

    protected static transient MConf i;

    public static MConf get() {
        return i;
    }

    public List<String> aliasesGrindTools = MUtil.list("grindtools");

    public List<FishingCuboid> fishingRegions = MUtil.list();

    public List<LootTable> lootTables = MUtil.list(
            new LootTable("fishing1", MUtil.list(
                    new AdvancedItemConfig(
                            Material.AIR,
                            "money",
                            MUtil.list(),
                            false,
                            20,
                            1,
                            3,
                            false,
                            MUtil.list("eco give %player% 100")
                    ),
                    new AdvancedItemConfig(
                            Material.DIAMOND,
                            "&bDiamond",
                            MUtil.list(),
                            false,
                            20,
                            1,
                            3,
                            true,
                            MUtil.list()
                    ),
                    new AdvancedItemConfig(
                            Material.GOLD_INGOT,
                            "&6Gold Ingot",
                            MUtil.list(),
                            false,
                            20,
                            1,
                            3,
                            true,
                            MUtil.list()
                    )
            ))
    );

    public HoeConfig hoeConfig = new HoeConfig();
    public SwordConfig swordConfig = new SwordConfig();
    public PickaxeConfig pickaxeConfig = new PickaxeConfig();
    public RodConfig rodConfig = new RodConfig();

    public TechChipConfig techChipConfig = new TechChipConfig();

    public UpgradeConfig upgradeConfig = new UpgradeConfig();

    public TierUpgradeConfig tierUpgradeConfig = new TierUpgradeConfig();


    public LootTable getLootTable(String id) {
        for (LootTable lootTable : lootTables) {
            if (lootTable.getId().equalsIgnoreCase(id)) {
                return lootTable;
            }
        }
        return null;
    }


    @Override
    public MConf load(MConf that) {
        this.fishingRegions = that.fishingRegions;
        super.load(that);
        return this;
    }

}
