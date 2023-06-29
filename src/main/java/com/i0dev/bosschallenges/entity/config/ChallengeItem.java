package com.i0dev.bosschallenges.entity.config;

import com.i0dev.bosschallenges.entity.MConf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.util.List;

@Data
@AllArgsConstructor
public class ChallengeItem {

    // Primary
    String id;
    String schematicName;
    long duration;

    // Hologram
    Material hologramMaterial;
    double blocksAbovePortalForHologram;
    List<String> hologramLines;

    // Item
    Material material;
    String displayName;
    List<String> lore;
    boolean glow;

    // mobs
    List<MythicEntity> mythicEntities;

    // Extra
    List<String> commandsToRunOnPortalPlace;
    List<String> commandsToRunOnPortalBreak;
    List<String> commandsToRunOnEntry;


    /**
     * Get a challenge item by its id
     *
     * @param id the id of the challenge item
     * @return the challenge item if found, null otherwise
     */
    public static ChallengeItem getChallengeItemById(String id) {
        return MConf.get().getChallengeItems().stream()
                .filter(challengeItem -> challengeItem.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }


}
