package com.i0dev.bosschallenges.cmd;

import com.i0dev.bosschallenges.cmd.type.TypeActivePortal;
import com.i0dev.bosschallenges.entity.ActivePortal;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;

public class CmdBossChallengeStopPortal extends BossChallengeCommand {

    public CmdBossChallengeStopPortal() {
        this.addParameter(TypeActivePortal.get(), "portal");
        this.setVisibility(Visibility.INVISIBLE);
    }

    @Override
    public void perform() throws MassiveException {
        ActivePortal activePortal = this.readArg();
        activePortal.getSession().stop();
        activePortal.delete();
        msg("Portal " + activePortal.getId() + " has been stopped");
    }
}
