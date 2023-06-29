package com.i0dev.bosschallenges.cmd;

import com.i0dev.bosschallenges.cmd.type.TypeSession;
import com.i0dev.bosschallenges.entity.Session;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;

public class CmdBossChallengeStopSession extends BossChallengeCommand {

    public CmdBossChallengeStopSession() {
        this.addParameter(TypeSession.get(), "session");
        this.setVisibility(Visibility.INVISIBLE);
    }

    @Override
    public void perform() throws MassiveException {
        Session session = this.readArg();
        if (session.getPortal() != null) {
            session.getPortal().delete();
        }
        session.stop();
        msg("Session " + session.getId() + " has been stopped");
    }
}
