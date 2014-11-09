package me.legitmodern.Punishment.Utils;

import java.text.SimpleDateFormat;

public class TimeUtils {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a - MMM d, yyyy");

    /**
     * Get the time unit for an amount
     *
     * @param amount Amount of time
     * @param unit   Unit of time
     * @return Amount of time
     */
    public static long getTime(long amount, String unit) {
        try {
            if (unit.equalsIgnoreCase("seconds")) {
                return amount * 1000L;
            } else if (unit.equalsIgnoreCase("minutes")) {
                return amount * 1000L * 60L;
            } else if (unit.equalsIgnoreCase("hours")) {
                return amount * 1000L * 60L * 60L;
            } else if (unit.equalsIgnoreCase("days")) {
                return amount * 1000L * 60L * 60L * 24L;
            } else if (unit.equalsIgnoreCase("months")) {
                return amount * 1000L * 60L * 60L * 24L * 31L;
            } else if (unit.equalsIgnoreCase("years")) {
                return amount * 1000L * 60L * 60L * 24L * 365L;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }
}
