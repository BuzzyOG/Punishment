package me.legitmodern.Punishment.Utils;

import net.md_5.bungee.api.ChatColor;

public class BungeeUtils {

    /**
     * Colorize a string
     *
     * @param c String to colorize
     * @return Colorized string
     */
    public static String colorizeString(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}

