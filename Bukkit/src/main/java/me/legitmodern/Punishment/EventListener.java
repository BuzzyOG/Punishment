package me.legitmodern.Punishment;

import me.legitmodern.Punishment.API.Punishment;
import me.legitmodern.Punishment.API.PunishmentType;
import me.legitmodern.Punishment.Utils.Chat.MessageUtils;
import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;
import me.legitmodern.Punishment.Utils.TimeUtils;
import me.legitmodern.Punishment.Utils.UUID.UUIDFetcher;
import me.legitmodern.Punishment.Utils.UUID.UUIDUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        UUID uuid = UUIDUtility.getUUID(e.getPlayer().getName());
        if (DatabaseManager.getInstance().hasMuteActive(uuid)) {
            Punishment punishment = DatabaseManager.getInstance().getActiveMute(uuid);
            if (punishment.getType() == PunishmentType.PERMANENT_MUTE) {
                e.setCancelled(true);

                MessageUtils.message(
                        e.getPlayer(),
                        "&4&m" + MessageUtils.StringType.CHAT_BAR.getString(),
                        " &c&lYOU ARE PERMANENTLY MUTED!",
                        " &7\"" + punishment.getReason() + "\"",
                        "&4&m" + MessageUtils.StringType.CHAT_BAR.getString()
                );
            } else if (punishment.getType() == PunishmentType.TEMPORARY_MUTE) {
                long diff = punishment.getEnd() - System.currentTimeMillis();
                if (diff <= 0L) {
                    punishment.setActive(false);
                    punishment.save();
                } else {
                    e.setCancelled(true);

                    MessageUtils.message(
                            e.getPlayer(),
                            "&4&m" + MessageUtils.StringType.CHAT_BAR.getString(),
                            " &c&lYOU ARE CURRENTLY MUTED!",
                            " &c&lEXPIRES: &6&o" + TimeUtils.dateFormat.format(new Date(punishment.getEnd())),
                            " &7\"" + punishment.getReason() + "\"",
                            "&4&m" + MessageUtils.StringType.CHAT_BAR.getString()
                    );
                }
            }
        }
    }

    /* UUID CACHE */

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws Exception {
        Player player = e.getPlayer();
        UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(player.getName()));
        UUIDUtility.getUUIDs().put(player.getName(), fetcher.call().get(player.getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUIDUtility.getUUIDs().remove(e.getPlayer().getName());
    }
}
