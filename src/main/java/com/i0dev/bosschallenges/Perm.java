package com.i0dev.grindtools;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified {

    BASECOMMAND,

    DEBUG,

    FISHING_REGION,
    FISHING_REGION_CREATE,
    FISHING_REGION_REMOVE,
    FISHING_REGION_LIST,
    FISHING_REGION_TELEPORT,
    GIVE,
    GIVE_TOOL,
    GIVE_TECH_CHIP,
    GIVE_UPGRADE,
    GIVE_UPGRADE_ANY,
    GIVE_UPGRADE_NEXT,
    UPGRADE,

    VERSION;

    private final String id;

    Perm() {
        this.id = PermissionUtil.createPermissionId(GrindToolsPlugin.get(), this);
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
