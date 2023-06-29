package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public class TimeDecreaseConf extends Entity<TimeDecreaseConf> {

    public static TimeDecreaseConf get(Object oid) {
        return TimeDecreaseConfColl.get().get(oid);
    }

    long duration; //seconds

    // Item
    Material material;
    String displayName;
    List<String> lore;
    boolean glow;

    @Override
    public TimeDecreaseConf load(@NotNull TimeDecreaseConf that) {
        super.load(that);
        this.duration = that.duration;
        this.material = that.material;
        this.displayName = that.displayName;
        this.lore = that.lore;
        this.glow = that.glow;
        return this;
    }


    public static void example() {
        if (TimeDecreaseConfColl.get().containsId("example")) return;
        TimeDecreaseConf timeDecreaseConf = TimeDecreaseConfColl.get().create("example");
        timeDecreaseConf.setDuration(20);
        timeDecreaseConf.setMaterial(Material.DIAMOND);
        timeDecreaseConf.setDisplayName("&c&lTime Decrease Item &7(20 seconds)");
        timeDecreaseConf.setLore(MUtil.list("&7Decreases the time by &c20 seconds&7.", "&7Can be used multiple times."));
        timeDecreaseConf.setGlow(true);
        timeDecreaseConf.changed();
    }

}
