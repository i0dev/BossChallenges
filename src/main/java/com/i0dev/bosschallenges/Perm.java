package com.i0dev.bosschallenges;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified {

    BASECOMMAND,

    GIVE,
    GIVE_CHALLENGE,
    GIVE_DECREASE,

    PLACE_PORTAL,
    USE_TIME_DECREASE,
    TOGGLE,

    STOP,
    STOP_SESSION,
    STOP_PORTAL,

    VERSION;

    private final String id;

    Perm() {
        this.id = PermissionUtil.createPermissionId(BossChallengesPlugin.get(), this);
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean has(Permissible permissible, boolean verboose) {
        return PermissionUtil.hasPermission(permissible, this, verboose);
    }

    public boolean has(Permissible permissible) {
        return PermissionUtil.hasPermission(permissible, this);
    }

}
