package me.legitmodern.Punishment.Utils;

import me.legitmodern.Punishment.Main;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeUtils {
    /**
     * Kick a player via BungeeCord
     *
     * @param player            Player to kick
     * @param disconnectMessage Disconnect message to kick them with
     */
    public static void kickPlayer(Player player, String disconnectMessage) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Disconnect");
            out.writeUTF(player.getName());
            out.writeUTF(disconnectMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
    }

    /**
     * Tell all staff on the server about a punishment being issued
     *
     * @param player Player to send plugin message
     * @param id     ID of punishment
     */
    public static void tellStaff(Player player, int id) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("TellStaff");
            out.writeInt(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
    }

    /**
     * Tell all staff on the server that someone attempted to ban a staff member
     *
     * @param player  Player to send plugin message that tried to ban the person
     * @param attempt Attempted player to ban
     */
    public static void warnStaff(Player player, String attempt) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("WarnStaff");
            out.writeUTF(player.getName());
            out.writeUTF(attempt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
    }
}
