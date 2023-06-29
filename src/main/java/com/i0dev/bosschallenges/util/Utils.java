package com.i0dev.grindtools.util;

import com.i0dev.grindtools.entity.MLang;
import org.bukkit.ChatColor;

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

}
