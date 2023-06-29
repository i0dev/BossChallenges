package com.i0dev.bosschallenges.util;

import com.i0dev.bosschallenges.entity.MLang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Utils {

    public static String prefixAndColor(String message, Pair<String, String>... pairs) {
        return color(pair(message.replace("%prefix%", MLang.get().prefix), pairs));
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String[] prefixColorFormat(List<String> in, Pair<String, String>... pairs) {

        String[] msg = new String[in.size()];
        for (int i = 0; i < in.size(); i++) {
            msg[i] = prefixAndColor(in.get(i));
        }

        return msg;
    }

    @SafeVarargs
    public static String pair(String msg, Pair<String, String>... pairs) {
        for (Pair<String, String> pair : pairs) {
            msg = msg.replace(pair.getKey(), pair.getValue());
        }
        return msg;
    }

    /**
     * Run a list of commands as console
     *
     * @param commands list of commands
     * @param player   player to replace %player% with
     */
    public static void runCommands(List<String> commands, Player player) {
        commands.forEach(command -> runCommand(command, player));
    }

    /**
     * Run a command as console
     *
     * @param command command to run
     * @param player  player to replace %player% with
     */
    public static void runCommand(String command, Player player) {
        command = command.replace("%player%", player == null ? "unknown" : player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }


}
