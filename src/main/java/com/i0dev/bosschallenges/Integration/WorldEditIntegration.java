package com.i0dev.bosschallenges.Integration;

import com.i0dev.bosschallenges.BossChallengesPlugin;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import lombok.SneakyThrows;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;

public class WorldEditIntegration {

    /**
     * Paste a schematic at a location
     *
     * @param location      the location to paste the schematic at
     * @param schematicName the name of the schematic to paste as it is in the schematics folder
     */
    @SneakyThrows
    public static void pasteSchematicAtLocation(Location location, String schematicName) {
        File file = new File(BossChallengesPlugin.get().getDataFolder() + File.separator + "schematics" + File.separator + schematicName);

        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);
        Clipboard clipboard;

        BlockVector3 blockVector3 = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if (clipboardFormat != null) {
            ClipboardReader clipboardReader = clipboardFormat.getReader(new FileInputStream(file));

            if (location.getWorld() == null)
                throw new NullPointerException("Failed to paste schematic due to world being null");

            World world = BukkitAdapter.adapt(location.getWorld());

            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();

            clipboard = clipboardReader.read();

            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(blockVector3)
                    .ignoreAirBlocks(false)
                    .build();


            Operations.complete(operation);
            editSession.close();
        }
    }
}
