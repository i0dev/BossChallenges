package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.store.Coll;

public class TimeDecreaseConfColl extends Coll<TimeDecreaseConf> {

    private static final TimeDecreaseConfColl i = new TimeDecreaseConfColl();

    public static TimeDecreaseConfColl get() {
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