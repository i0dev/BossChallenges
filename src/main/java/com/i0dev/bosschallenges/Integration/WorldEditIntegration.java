package com.i0dev.bosschallenges.Integration;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import com.massivecraft.massivecore.MassiveCore;
import org.bukkit.Location;

import java.io.File;

public class WorldEdit {


    public void pasteSchemAtLocation(Location location, String schematicName) {
        File file = new File(BossChallengesPlugin.get().getDataFolder() + File.separator + "schematics" + File.separator + schematicName);


        ClipboardFormat format = ClipboardFormats.findByFile(file);
        ClipboardReader reader = format.getReader(new FileInputStream(file));
        Clipboard clipboard = reader.read();
    }


}
