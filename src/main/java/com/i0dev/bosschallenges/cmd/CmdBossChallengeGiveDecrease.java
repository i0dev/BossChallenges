package com.i0dev.bosschallenges.cmd;

import com.i0dev.bosschallenges.cmd.type.TypeTimeDecrease;
import com.i0dev.bosschallenges.entity.TimeDecreaseConf;
import com.i0dev.bosschallenges.util.ItemBuilder;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdBossChallengeGiveDecrease extends BossChallengeCommand {

    public CmdBossChallengeGiveDecrease() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeTimeDecrease.get(), "time-decrease");
        this.addParameter(TypeInteger.get(), "amount", "1");
        this.setVisibility(Visibility.SECRET);
    }


    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg();
        TimeDecreaseConf timeDecreaseItem = this.readArg();
        int amount = this.readArg(1);

        ItemBuilder itemBuilder = new ItemBuilder(timeDecreaseItem.getMaterial())
                .name(timeDecreaseItem.getDisplayName())
                .lore(timeDecreaseItem.getLore())
                .addGlow(timeDecreaseItem.isGlow())
                .amount(amount)
                .addPDCValue("prevent-stack", UUID.randomUUID().toString())
                .addPDCValue("decrease-id", timeDecreaseItem.getId());

        player.getInventory().addItem(itemBuilder);
        msg("You gave " + player.getName() + " " + amount + " " + timeDecreaseItem.getDisplayName() + "s");
        player.sendMessage(Utils.color("You received " + amount + " " + timeDecreaseItem.getDisplayName() + "s"));
    }
}
