package com.i0dev.bosschallenges.cmd.type;

import com.i0dev.bosschallenges.entity.ActivePortal;
import com.i0dev.bosschallenges.entity.ActivePortalColl;
import com.i0dev.bosschallenges.entity.ChallengeItemConf;
import com.i0dev.bosschallenges.entity.ChallengeItemConfColl;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class TypeActivePortal extends TypeAbstractChoice<ActivePortal> {

    private static final TypeActivePortal i = new TypeActivePortal();

    public static TypeActivePortal get() {
        return i;
    }

    public TypeActivePortal() {
        super(ActivePortal.class);
    }

    public String getName() {
        return "text";
    }

    public ActivePortal read(String arg, CommandSender sender) {
        return ActivePortal.get(arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return ActivePortalColl.get().getIds();
    }
}

