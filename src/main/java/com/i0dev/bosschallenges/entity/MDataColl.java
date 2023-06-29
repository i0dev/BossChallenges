package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MLangColl extends Coll<MLang> {

    private static MLangColl i = new MLangColl();

    public static MLangColl get() {
        return i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        MLang.i = this.get(MassiveCore.INSTANCE, true);
    }

}
