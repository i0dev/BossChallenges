package com.i0dev.bosschallenges;

import com.i0dev.bosschallenges.Integration.PlaceholderAPI;
import com.i0dev.bosschallenges.entity.*;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class BossChallengesPlugin extends MassivePlugin {

    private static BossChallengesPlugin i;

    public BossChallengesPlugin() {
        BossChallengesPlugin.i = this;
    }

    public static BossChallengesPlugin get() {
        return i;
    }

    @Override
    public void onEnableInner() {
        this.activateAuto();
    }


    @Override
    public List<Class<?>> getClassesActiveColls() {
        return new MassiveList<>(
                MConfColl.class,
                MLangColl.class,

                MDataColl.class,
                ActivePortalColl.class,
                SessionColl.class
        );
    }

    @Override
    public void onEnablePost() {
        super.onEnablePost();
        ActivePortalColl.get().getAll().forEach(ActivePortal::initialize);
        SessionColl.get().getAll().forEach(Session::initialize);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI().register();
        }
        File schematicsDirectory = new File(getDataFolder() + "/schematics");
        if (!schematicsDirectory.exists()) schematicsDirectory.mkdirs();
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        return p instanceof WorldEditPlugin ? (WorldEditPlugin) p : null;
    }

    public HolographicDisplaysAPI getHolographicDisplays() {
        return HolographicDisplaysAPI.get(this);
    }
}