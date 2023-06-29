package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.store.Coll;

public class SessionColl extends Coll<Session> {

    private static final SessionColl i = new SessionColl();

    public static SessionColl get() {
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