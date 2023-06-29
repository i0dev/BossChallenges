package com.i0dev.bosschallenges.cmd.type;

import com.i0dev.bosschallenges.entity.ActivePortal;
import com.i0dev.bosschallenges.entity.ActivePortalColl;
import com.i0dev.bosschallenges.entity.Session;
import com.i0dev.bosschallenges.entity.SessionColl;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class TypeSession extends TypeAbstractChoice<Session> {

    private static final TypeSession i = new TypeSession();

    public static TypeSession get() {
        return i;
    }

    public TypeSession() {
        super(Session.class);
    }

    public String getName() {
        return "text";
    }

    public Session read(String arg, CommandSender sender) {
        return Session.get(arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return SessionColl.get().getIds();
    }
}

