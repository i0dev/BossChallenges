package com.i0dev.bosschallenges.task;

import com.i0dev.bosschallenges.entity.ActivePortal;
import com.i0dev.bosschallenges.entity.ActivePortalColl;
import com.massivecraft.massivecore.ModuloRepeatTask;

import java.util.ArrayList;
import java.util.List;

public class TaskActivePortal extends ModuloRepeatTask {

    private static TaskActivePortal i = new TaskActivePortal();

    public static TaskActivePortal get() {
        return i;
    }

    @Override
    public long getDelayMillis() {
        return 1000L;
    }

    @Override
    public void invoke(long l) {
        List<ActivePortal> toRemove = new ArrayList<>();
        for (ActivePortal activePortal : ActivePortalColl.get().getAll()) {
            if (activePortal.getDuration() <= 0) {
                activePortal.getHologram().delete();
                activePortal.getPortalBlockLocation().getBlock().setType(activePortal.getOriginalBlockMaterial());
                toRemove.add(activePortal);
                continue;
            }
            activePortal.tick();
            activePortal.setDuration(activePortal.getDuration() - 1);
            activePortal.changed();
        }
        toRemove.forEach(ActivePortal::detach);
    }
}
