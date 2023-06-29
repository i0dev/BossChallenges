package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.store.Coll;

public class ActivePortalColl extends Coll<ActivePortal> {

    private static final ActivePortalColl i = new ActivePortalColl();

    public static ActivePortalColl get() {
        return i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }
}