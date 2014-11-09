package me.legitmodern.Punishment.Utils.Chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    private MessageUtils() {

    }

    /**
     * Colorize a string
     *
     * @param string String to colorize
     * @return Colorized string
     */
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Get a chat prefix for Permissions plugin
     * @return General prefix with color
     */
    public static String getPrefix() {
        return colorize("&c[&6Punishments&c] &8" + Character.DOT + " &7");
    }

    /**
     * Message a player
     *
     * @param player   Player to message
     * @param messages Messages to send
     */
    public static void messagePrefix(Player player, String... messages) {
        for (String message : messages) {
            player.sendMessage(colorize(getPrefix() + message));
        }
    }

    /**
     * Message a player without prefix
     *
     * @param player   Player to message
     * @param messages Messages to send
     */
    public static void message(Player player, String... messages) {
        for (String message : messages) {
            player.sendMessage(colorize(message));
        }
    }


    public static enum Character {
        DOT('●'),
        RAQUO('»');

        private char character;

        Character(char character) {
            this.character = character;
        }

        /**
         * Get the character of the enum constant
         *
         * @return Enum constant's character
         */
        public char getCharacter() {
            return this.character;
        }

        @Override
        public String toString() {
            return String.valueOf(this.getCharacter());
        }
    }

    public static enum StringType {
        CHAT_BAR("----------------------------------------------------");

        private String string;

        StringType(String string) {
            this.string = string;
        }

        /**
         * Get the string value of the string type
         *
         * @return String Type
         */
        public String getString() {
            return this.string;
        }
    }
}