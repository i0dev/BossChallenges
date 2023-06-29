package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MConfColl extends Coll<MConf> {

    private static MConfColl i = new MConfColl();

    public static MConfColl get() {
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
        MConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
