package com.i0dev.bosschallenges.entity.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.util.List;

@Data
@AllArgsConstructor
public class Challenge {

    String id;

    //Item
    Material material;
    String displayName;
    List<String> lore;

    //Extra
    List<String> commandsToRunOnPortalPlace;
    List<String> commandsToRunOnPortalBreak;
    List<String> commandsToRunOnEntry;

}
