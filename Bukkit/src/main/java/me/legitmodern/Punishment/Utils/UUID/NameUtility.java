package me.legitmodern.Punishment.Utils.UUID;

import java.util.Arrays;
import java.util.UUID;

public class NameUtility {

    /**
     * Get a player's name from a UUID
     *
     * @param uuid UUID to grab name for
     * @return Player's Name
     */
    public static String getName(UUID uuid) {
        NameFetcher fetcher = new NameFetcher(Arrays.asList(uuid));
        try {
            return fetcher.call().get(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
