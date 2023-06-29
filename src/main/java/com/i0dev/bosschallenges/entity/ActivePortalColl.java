package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class ActivePortalsColl extends Coll<ActivePortals> {

    private static ActivePortalsColl i = new ActivePortalsColl();

    public static ActivePortalsColl get() {
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
        ActivePortals.i = this.get(MassiveCore.INSTANCE, true);
    }
}