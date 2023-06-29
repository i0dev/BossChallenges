package com.i0dev.bosschallenges.cmd.type;

import com.i0dev.bosschallenges.entity.Session;
import com.i0dev.bosschallenges.entity.SessionColl;
import com.i0dev.bosschallenges.entity.TimeDecreaseConf;
import com.i0dev.bosschallenges.entity.TimeDecreaseConfColl;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class TypeTimeDecrease extends TypeAbstractChoice<TimeDecreaseConf> {

    private static final TypeTimeDecrease i = new TypeTimeDecrease();

    public static TypeTimeDecrease get() {
        return i;
    }

    public TypeTimeDecrease() {
        super(TimeDecreaseConf.class);
    }

    public String getName() {
        return "text";
    }

    public TimeDecreaseConf read(String arg, CommandSender sender) {
        return TimeDecreaseConf.get(arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return TimeDecreaseConfColl.get().getIds();
    }
}

