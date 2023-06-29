package com.i0dev.bosschallenges.cmd.type;

import com.i0dev.bosschallenges.entity.MConf;
import com.i0dev.bosschallenges.entity.config.ChallengeItem;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.stream.Collectors;

public class TypeChallengeItem extends TypeAbstractChoice<ChallengeItem> {

    private static final TypeChallengeItem i = new TypeChallengeItem();

    public static TypeChallengeItem get() {
        return i;
    }

    public TypeChallengeItem() {
        super(ChallengeItem.class);
    }

    public String getName() {
        return "text";
    }

    public ChallengeItem read(String arg, CommandSender sender) {
        return ChallengeItem.getChallengeItemById(arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return MConf.get().getChallengeItems().stream()
                .map(ChallengeItem::getId)
                .collect(Collectors.toList());
    }
}

