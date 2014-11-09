package me.legitmodern.Punishment.Utils;

import me.legitmodern.Punishment.API.Punishment;
import me.legitmodern.Punishment.Utils.UUID.NameUtility;

import java.util.Date;

public class BanUtils {
    /**
     * Get the kick message of a ban
     *
     * @param punishment Punishment to get message for
     * @return Kick message for ban
     */
    public static String getKickMessage(Punishment punishment) {
        return StringUtils.colorizeString("&4&l-*X*- " + punishment.getType().getName() + " -*X*-" + "\n" + "&6Issuer: &e" + NameUtility.getName(punishment.getIssuer()) + "\n" + "&e\"" + punishment.getReason() + "\"" + "\n" + (punishment.getEnd() == 0L ? "&c&lNO EXPIRATION!" : "&c&lEXPIRATION : " + (TimeUtils.dateFormat.format(new Date(punishment.getEnd())))));
    }
}
