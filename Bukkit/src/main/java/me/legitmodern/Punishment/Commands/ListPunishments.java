package me.legitmodern.Punishment.Commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.legitmodern.Punishment.Utils.Chat.MessageUtils;
import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;
import me.legitmodern.Punishment.API.Punishment;
import me.legitmodern.Punishment.Utils.UUID.UUIDUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ListPunishments {
    @Command(
            aliases = {"listpunishments", "lpunishments", "lp"},
            desc = "List a player's punishments.",
            min = 1,
            max = 1,
            usage = "[player]"
    )
    @CommandPermissions({"punishment.staff.list"})
    public static void listPunishments(CommandContext args, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            MessageUtils.messagePrefix(player, "&a&oLoading punishments for &6&o" + args.getString(0) + "&a&o...");

            UUID target = UUIDUtility.getUUID(args.getString(0));
            if (target == null) {
                MessageUtils.messagePrefix(player, "&cThat player could not be found!");
                return;
            }

            Punishment[] punishments = DatabaseManager.getInstance().getPunishments(target);
            if (punishments.length == 0) {
                MessageUtils.messagePrefix(player, "&cThat player does not have any punishments!");
            } else {
                StringBuilder buffer = new StringBuilder();
                for (Punishment punishment : punishments) {
                    buffer.append(punishment.isActive() ? "&a" : "&c").append(punishment.getId()).append(" ").append("&7(&f").append(punishment.getType().getName()).append("&7)").append(punishment == punishments[punishments.length - 1] ? "" : "&7, ");
                }

                MessageUtils.message(player, "&aShowing a list of this player's punishment IDs. Use &6/spunishments [id] &ato show a punishment's details.", "&6&lPUNISHMENTS (&e&l" + punishments.length + "&6&l) : " + buffer.toString());
            }
        }
    }
}
