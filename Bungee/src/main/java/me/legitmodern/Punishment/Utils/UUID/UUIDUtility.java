package me.legitmodern.Punishment.Utils.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDUtility implements Listener {

    /**
     * Get a player's UUID
     *
     * @param player Player to get UUID from
     * @return Player's UUID
     */
    public static UUID getUUID(String player) {
        if(getUUIDs().containsKey(player)) {
            return getUUIDs().get(player);
        } else {
            UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(player));
            try {
                return fetcher.call().get(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static Map<String, UUID> PLAYER_UUIDS = new HashMap<>();
    public static Map<String, UUID> getUUIDs() {
        return PLAYER_UUIDS;
    }


    @EventHandler
    public void join(PostLoginEvent e) throws Exception {
        ProxiedPlayer player = e.getPlayer();
        UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(player.getName()));
        getUUIDs().put(player.getName(), fetcher.call().get(player.getName()));
    }

    @EventHandler
    public void leave(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        getUUIDs().remove(player.getName());
    }

}
