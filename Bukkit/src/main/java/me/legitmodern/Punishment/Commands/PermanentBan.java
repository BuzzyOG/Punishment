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

public class PermanentBan {
    @Command(
            aliases = {"permban", "pban", "pb"},
            desc = "Permanently ban a player.",
            min = 2,
            usage = "[player] [reason]"
    )
    @CommandPermissions({"punishment.staff.permban"})
    public static void permanentBan(CommandContext args, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            UUID target = UUIDUtility.getUUID(args.getString(0));
            StringBuilder reason = new StringBuilder();
            for (int x = 1; x < args.argsLength(); x++) {
                reason.append(args.getString(x)).append(x == args.argsLength() - 1 ? "" : " ");
            }

            if (target == null) {
                MessageUtils.messagePrefix(player, "&cCan't find player &6" + args.getString(0) + "&c!");
                return;
            } else if (DatabaseManager.getInstance().hasBanActive(target)) {
                MessageUtils.messagePrefix(player, "&cThat player already has an active ban!");
                return;
            } else if (Bukkit.getPlayer(target).hasPermission("punishment.staff")) {
                BungeeUtils.warnStaff(player, NameUtility.getName(target));
                MessageUtils.messagePrefix(player, "&c&lYou may not ban other staff! All staff online have been notified and this attempt has been logged.");
                return;
            }

            Punishment punishment = new Punishment(
                    target,
                    UUIDUtility.getUUID(player.getName()),
                    PunishmentType.PERMANENT_BAN,
                    reason.toString(),
                    TimeUtils.dateFormat.format(new Date()),
                    0L
            );
            punishment.save();
            Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer != null) {
                BungeeUtils.kickPlayer(targetPlayer, BanUtils.getKickMessage(punishment));
            }

            BungeeUtils.tellStaff(player, punishment.getId());
            MessageUtils.messagePrefix(player, "&aSuccessfully banned &6" + args.getString(0) + "&a!");
        }
    }
}
