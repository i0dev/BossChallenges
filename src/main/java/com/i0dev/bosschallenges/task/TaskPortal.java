package com.i0dev.bosschallenges.task;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import com.i0dev.bosschallenges.entity.ActivePortal;
import com.i0dev.bosschallenges.entity.ActivePortalColl;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.ModuloRepeatTask;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

public class TaskPortal extends ModuloRepeatTask {

    private static TaskPortal i = new TaskPortal();

    public static TaskPortal get() {
        return i;
    }

    // 1000L = 1 second
    @Override
    public long getDelayMillis() {
        return 1000L;
    }

    /**
     * Remove portals that have no duration left
     *
     * @param l The current time
     */
    @Override
    public void invoke(long l) {
        List<ActivePortal> toRemove = new ArrayList<>();
        for (ActivePortal activePortal : ActivePortalColl.get().getAll()) {
            if (activePortal.getDuration() <= 0) {
                toRemove.add(activePortal);
                continue;
            }
            activePortal.setDuration(activePortal.getDuration() - 1);
            activePortal.changed();
        }
        toRemove.forEach(ActivePortal::closePortal);
    }
}
