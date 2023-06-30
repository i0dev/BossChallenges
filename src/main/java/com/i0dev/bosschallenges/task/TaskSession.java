package com.i0dev.bosschallenges.task;

import com.i0dev.bosschallenges.Integration.MythicMobsIntegration;
import com.i0dev.bosschallenges.entity.MConf;
import com.i0dev.bosschallenges.entity.Session;
import com.i0dev.bosschallenges.entity.SessionColl;
import com.i0dev.bosschallenges.entity.config.ConfigLocation;
import com.i0dev.bosschallenges.entity.config.MythicEntity;
import com.i0dev.bosschallenges.util.Utils;
import com.massivecraft.massivecore.ModuloRepeatTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TaskSession extends ModuloRepeatTask {

    private static TaskSession i = new TaskSession();

    public static TaskSession get() {
        return i;
    }

    // 1000L = 1 second
    @Override
    public long getDelayMillis() {
        return 1000L;
    }

    /**
     * Remove sessions that have no players
     * Spawn in mobs
     *
     * @param l The current time
     */
    @Override
    public void invoke(long l) {
        List<Session> toRemove = new ArrayList<>();
        for (Session session : SessionColl.get().getAll()) {


            if (!session.isStarted()) {
                long countdownTime = session.getPortal().getDuration();
                List<Integer> titleToRemove = new ArrayList<>();
                for (Integer time : session.getToTitleAnnounce()) {
                    if (countdownTime <= time) {
                        for (Player player : session.getPlayersInArena()) {
                            player.sendTitle(
                                    Utils.color(MConf.get().getTitleAnnouncementMessageForPlayersInArena()
                                            .replace("%challengeName%", session.getPortal().getChallengeItem().getDisplayName())),
                                    Utils.color(MConf.get().getSubTitleAnnouncementMessageForPlayersInArena()
                                            .replace("%time%", String.valueOf(time))),
                                    10, 40, 10);
                        }
                        titleToRemove.add(time);
                    }
                }
                titleToRemove.forEach(session.getToTitleAnnounce()::remove);
                List<Integer> chatToRemove = new ArrayList<>();
                for (Integer integer : session.getToChatAnnounce()) {
                    if (countdownTime <= integer) {
                        Bukkit.broadcastMessage(Utils.color(MConf.get().getChatAnnouncementMessageForAllPlayersOnline()
                                .replace("%challengeName%", session.getPortal().getChallengeItem().getDisplayName())
                                .replace("%time%", String.valueOf(integer))
                        ));
                        chatToRemove.add(integer);
                    }
                }
                chatToRemove.forEach(session.getToChatAnnounce()::remove);
                continue;
            }

            long time = System.currentTimeMillis() - session.getStartTime();

            if (time > session.getChallengeItem().getMaxRunTime() * 1000) {
                toRemove.add(session);
                continue;
            }

            if (session.getPlayers().isEmpty()) {
                toRemove.add(session);
                continue;
            }

            boolean allDead = session.getMobs().stream().noneMatch(uuid -> {
                Entity entity = Bukkit.getEntity(uuid);
                return entity != null && !entity.isDead();
            });

            if (session.getToSpawn().isEmpty() && allDead) {
                toRemove.add(session);
                continue;
            }

            List<MythicEntity> toRemoveMythicEntity = new ArrayList<>();
            for (MythicEntity mythicEntity : session.getToSpawn()) {
                long delayMilliseconds = mythicEntity.getDelaySeconds() * 1000;
                if (time >= delayMilliseconds) {
                    ConfigLocation locOffset = mythicEntity.getLocationOffset();
                    for (int i = 0; i < mythicEntity.getAmount(); i++) {
                        Entity entity = MythicMobsIntegration.spawnMythicMob(session.getSpawnLocation().clone().add(locOffset.getX(), locOffset.getY(), locOffset.getZ()), mythicEntity.getMythicMobName(), session.getId());
                        session.addEntity(entity.getUniqueId());
                    }
                    toRemoveMythicEntity.add(mythicEntity);
                }
            }
            toRemoveMythicEntity.forEach(mythicEntity -> session.getToSpawn().remove(mythicEntity));
        }
        toRemove.forEach(Session::stop);
    }
}