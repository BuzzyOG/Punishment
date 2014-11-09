package me.legitmodern.Punishment;

import me.legitmodern.Punishment.Utils.BanUtils;
import me.legitmodern.Punishment.Utils.BungeeUtils;
import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;
import me.legitmodern.Punishment.API.Punishment;
import me.legitmodern.Punishment.API.PunishmentType;
import me.legitmodern.Punishment.Utils.UUID.UUIDUtility;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class EventListener implements Listener {
    @EventHandler
    public void join(PreLoginEvent e) {
        UUID uuid = UUIDUtility.getUUID(e.getConnection().getName());
        if (DatabaseManager.getInstance().hasBanActive(uuid)) {
            Punishment punishment = DatabaseManager.getInstance().getActiveBan(uuid);
            if (punishment.getType() == PunishmentType.PERMANENT_BAN) {
                e.getConnection().disconnect(BanUtils.getKickMessage(punishment));
            } else if (punishment.getType() == PunishmentType.TEMPORARY_BAN) {
                long diff = punishment.getEnd() - System.currentTimeMillis();
                if (diff <= 0L) {
                    punishment.setActive(false);
                    punishment.save();
                } else {
                    e.getConnection().disconnect(BanUtils.getKickMessage(punishment));
                }
            }
        }
    }

    @EventHandler
    public void kickReceive(PluginMessageEvent e) throws IOException {
        if (e.getTag().equals("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            String subchannel = in.readUTF();
            if (subchannel.equals("Disconnect")) {
                Main.getInstance().getProxy().getPlayer(in.readUTF()).disconnect(BungeeUtils.colorizeString(in.readUTF()));
            } else if (subchannel.equals("TellStaff")) {
                int id = in.readInt();
                for (ProxiedPlayer player : Main.getInstance().getProxy().getPlayers()) {
                    if (player.hasPermission("punishment.staff")) {
                        player.sendMessage(BungeeUtils.colorizeString("&e&lPUNISHMENT &8» &aPunishment &b#" + id + " &awas issued!"));
                    }
                }
            } else if (subchannel.equals("WarnStaff")) {
                String attempt = in.readUTF(), ban = in.readUTF();
                for (ProxiedPlayer player : Main.getInstance().getProxy().getPlayers()) {
                    if (player.hasPermission("punishment.staff") && !player.getName().equals(attempt)) {
                        player.sendMessage(BungeeUtils.colorizeString("&e&lPUNISHMENT &8» &b" + attempt + " &atried to ban &b" + ban + "&a!"));
                    }
                }
            }
        }
    }
}
