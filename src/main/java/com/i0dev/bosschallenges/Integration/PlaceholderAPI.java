package com.i0dev.bosschallenges.Integration;

import com.i0dev.bosschallenges.entity.ActivePortal;
import com.i0dev.bosschallenges.entity.ActivePortalColl;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return "i01";
    }

    @Override
    public String getIdentifier() {
        return "bosschallenges";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * %bosschallenges_portal_countdown_{ID}%
     */
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.startsWith("portal_countdown_")) {
            String id = params.replace("portal_countdown_", "");
            ActivePortal activePortal = ActivePortalColl.get().get(id);
            if (activePortal == null) return "ERROR";
            long duration = activePortal.getDuration();
            return String.valueOf(duration);
        }
        return null;
    }

}
