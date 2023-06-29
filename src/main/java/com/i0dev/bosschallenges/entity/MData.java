package com.i0dev.bosschallenges.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@EditorName("config")
public class MData extends Entity<MData> {

    protected static transient MData i;

    public static MData get() {
        return i;
    }

    public Set<UUID> playersToSpawnOnLogin = MUtil.set();

    public void addPlayerToSpawnOnLogin(UUID uuid) {
        playersToSpawnOnLogin.add(uuid);
        this.changed();
    }

    public void removePlayerToSpawnOnLogin(UUID uuid) {
        playersToSpawnOnLogin.remove(uuid);
        this.changed();
    }

    @Override
    public MData load(MData that) {
        super.load(that);
        this.playersToSpawnOnLogin = that.playersToSpawnOnLogin;
        return this;
    }
}
