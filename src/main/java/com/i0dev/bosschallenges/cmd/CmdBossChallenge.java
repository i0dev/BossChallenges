package com.i0dev.bosschallenges.cmd;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import com.i0dev.bosschallenges.Perm;
import com.i0dev.bosschallenges.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

public class CmdBossChallenges extends BossChallengesCommand {

    private static CmdBossChallenges i = new CmdBossChallenges();

    public static CmdBossChallenges get() {
        return i;
    }

    public MassiveCommandVersion cmdFactionsVersion = new MassiveCommandVersion(BossChallengesPlugin.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(Perm.VERSION));

    @Override
    public List<String> getAliases() {
        return MConf.get().aliasesGrindTools;
    }

}
