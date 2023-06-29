package com.i0dev.bosschallenges.cmd;

import com.i0dev.bosschallenges.cmd.type.TypeChallengeItem;
import com.i0dev.bosschallenges.entity.config.ChallengeItem;
import com.i0dev.bosschallenges.util.ItemBuilder;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;

public class CmdBossChallengeGive extends BossChallengeCommand {

    public CmdBossChallengeGive() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeChallengeItem.get(), "challange-id");
        this.addParameter(TypeInteger.get(), "amount", "1");
        this.setVisibility(Visibility.SECRET);
    }


    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg();
        ChallengeItem challengeItem = this.readArg();
        int amount = this.readArg(1);

        ItemBuilder itemBuilder = new ItemBuilder(challengeItem.getMaterial())
                .name(challengeItem.getDisplayName())
                .lore(challengeItem.getLore())
                .addGlow(challengeItem.isGlow())
                .amount(amount)
                .addPDCValue("challenge-id", challengeItem.getId());

        player.getInventory().addItem(itemBuilder);
        msg("You gave " + player.getName() + " " + amount + " " + challengeItem.getDisplayName() + "s");
        player.sendMessage(Utils.color("You received " + amount + " " + challengeItem.getDisplayName() + "s"));
    }
}
