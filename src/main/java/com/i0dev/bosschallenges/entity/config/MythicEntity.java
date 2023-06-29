package com.i0dev.bosschallenges.entity.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
public class MythicEntity {

    String mythicMobName;
    int amount;
    long delaySeconds;
    ConfigLocation locationOffset;

}
