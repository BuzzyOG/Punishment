package me.legitmodern.Punishment.Commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.legitmodern.Punishment.API.Punishment;
import me.legitmodern.Punishment.API.PunishmentType;
import me.legitmodern.Punishment.Utils.*;
import me.legitmodern.Punishment.Utils.Chat.MessageUtils;
import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;
import me.legitmodern.Punishment.Utils.UUID.NameUtility;
import me.legitmodern.Punishment.Utils.UUID.UUIDUtility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class TemporaryBan {
    @Command(
            aliases = {"tempban", "tban", "tb"},
            desc = "Temporarily ban a player.",
            min = 4,
            usage = "[player] [time-amount] [time-unit] [reason]"
    )
    @CommandPermissions({"punishment.staff.tempban"})
    public static void permanentBan(CommandContext args, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            UUID target = UUIDUtility.getUUID(args.getString(0));
            StringBuilder reason = new StringBuilder();
            for (int x = 3; x < args.argsLength(); x++) {
                reason.append(args.getString(x)).append(x == args.argsLength() - 1 ? "" : " ");
            }

            if (target == null) {
                MessageUtils.messagePrefix(player, "&cCan't find player &6" + args.getString(0) + "&c!");
                return;
            } else if (DatabaseManager.getInstance().hasBanActive(target)) {
                MessageUtils.messagePrefix(player, "&cThat player already has an active ban!");
                return;
            } else if (!(args.getString(2).equalsIgnoreCase("minutes") || args.getString(2).equalsIgnoreCase("hours") || args.getString(2).equalsIgnoreCase("days") || args.getString(2).equalsIgnoreCase("months") || args.getString(2).equalsIgnoreCase("years"))) {
                MessageUtils.messagePrefix(player, "&cYou inputted a wrong time unit!");
                return;
            } else if (Bukkit.getPlayer(target).hasPermission("punishment.staff")) {
                BungeeUtils.warnStaff(player, NameUtility.getName(target));
                MessageUtils.messagePrefix(player, "&c&lYou may not ban other staff! All staff online have been notified and this attempt has been logged.");
                return;
            }

            int integer;
            try {
                integer = Integer.parseInt(args.getString(1));
            } catch (NumberFormatException e) {
                MessageUtils.messagePrefix(player, "&cYou need to input a number! Exiting...");
                return;
            }

            Punishment punishment = new Punishment(
                    target,
                    UUIDUtility.getUUID(player.getName()),
                    PunishmentType.TEMPORARY_BAN,
                    reason.toString(),
                    TimeUtils.dateFormat.format(new Date()),
                    System.currentTimeMillis() + TimeUtils.getTime(integer, args.getString(2))
            );
            punishment.save();
            Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer != null) {
                BungeeUtils.kickPlayer(targetPlayer, BanUtils.getKickMessage(punishment));
            }

            BungeeUtils.tellStaff(player, punishment.getId());
            MessageUtils.messagePrefix(player, "&aSuccessfully temp-banned &6" + args.getString(0) + "&a!");
        }
    }
}
