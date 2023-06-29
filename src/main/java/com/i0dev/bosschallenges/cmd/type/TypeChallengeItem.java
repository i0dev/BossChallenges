package com.i0dev.bosschallenges.cmd.type;

import com.i0dev.bosschallenges.entity.ChallengeItemConf;
import com.i0dev.bosschallenges.entity.ChallengeItemConfColl;
import com.i0dev.bosschallenges.entity.MConf;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.stream.Collectors;

public class TypeChallengeItem extends TypeAbstractChoice<ChallengeItemConf> {

    private static final TypeChallengeItem i = new TypeChallengeItem();

    public static TypeChallengeItem get() {
        return i;
    }

    public TypeChallengeItem() {
        super(ChallengeItemConf.class);
    }

    public String getName() {
        return "text";
    }

    public ChallengeItemConf read(String arg, CommandSender sender) {
        return ChallengeItemConf.get(arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return ChallengeItemConfColl.get().getIds();
    }
}

