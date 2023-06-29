package com.i0dev.bosschallenges.cmd;

import com.i0dev.bosschallenges.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;

public class CmdBossChallengeToggle extends BossChallengeCommand {

    public CmdBossChallengeToggle() {
        this.setVisibility(Visibility.SECRET);
    }


    @Override
    public void perform() {
        if (MConf.get().placingNewPortalsEnabled) {
            MConf.get().placingNewPortalsEnabled = false;
            msg("<i>Disabled placing new portals.");
        } else {
            MConf.get().placingNewPortalsEnabled = true;
            msg("<i>Enabled placing new portals.");
        }
        MConf.get().changed();
    }
}
