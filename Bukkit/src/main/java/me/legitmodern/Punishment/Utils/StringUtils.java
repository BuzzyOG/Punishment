package me.legitmodern.Punishment.Utils;

import org.bukkit.ChatColor;

public class StringUtils {

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
