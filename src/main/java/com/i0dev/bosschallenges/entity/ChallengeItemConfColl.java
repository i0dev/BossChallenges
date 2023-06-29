package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.store.Coll;

public class ChallengeItemConfColl extends Coll<ChallengeItemConf> {

    private static final ChallengeItemConfColl i = new ChallengeItemConfColl();

    public static ChallengeItemConfColl get() {
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