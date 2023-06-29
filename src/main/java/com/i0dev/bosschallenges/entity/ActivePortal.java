package com.i0dev.bosschallenges.entity;

import com.i0dev.bosschallenges.entity.object.ActivePortal;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Location;

import java.util.List;

@Getter
public class ActivePortals extends Entity<ActivePortals> {

    public static ActivePortals get(Object oid) {
        return ActivePortalsColl.get().get(oid);
    }

    /**
     * Add an active portal to the list
     *
     * @param activePortal the active portal to add
     */
    public void addActivePortal(ActivePortal activePortal) {
        activePortals.add(activePortal);
        this.changed();
    }

    /**
     * Get the active portal by the location of the portal block
     *
     * @param location the location of the portal block
     * @return the active portal or null if not found
     */
    public static ActivePortal getActivePortalByLocation(Location location) {
        return ActivePortals.get().getActivePortals().stream()
                .filter(activePortal -> activePortal.getPortalBlockLocation().equals(location))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ActivePortals load(ActivePortals that) {
        super.load(that);
        this.activePortals = that.activePortals;
        this.activePortals.forEach(ActivePortal::initialize);
        return this;
    }

}
