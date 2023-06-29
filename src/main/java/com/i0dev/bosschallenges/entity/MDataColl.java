package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MDataColl extends Coll<MData> {

    private static MDataColl i = new MDataColl();

    public static MDataColl get() {
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
        MData.i = this.get(MassiveCore.INSTANCE, true);
    }

}
