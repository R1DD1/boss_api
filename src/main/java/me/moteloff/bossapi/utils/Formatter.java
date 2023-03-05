package me.moteloff.bossapi.utils;

import org.bukkit.ChatColor;

public class Formatter {
    public static String translate(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
