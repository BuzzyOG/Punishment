package me.legitmodern.Punishment.Commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.legitmodern.Punishment.Utils.Chat.MessageUtils;
import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;
import me.legitmodern.Punishment.API.Punishment;
import me.legitmodern.Punishment.Utils.TimeUtils;
import me.legitmodern.Punishment.Utils.UUID.NameUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowPunishment {
    @Command(
            aliases = {"showpunishment", "spunishment", "sp"},
            desc = "Show a punishment's details.",
            min = 1,
            max = 1,
            usage = "[id]"
    )
    @CommandPermissions({"punishment.staff.show"})
    public static void showPunishment(CommandContext args, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            int integer;
            try {
                integer = Integer.parseInt(args.getString(0));
            } catch (NumberFormatException e) {
                MessageUtils.messagePrefix(player, "&cYou must input a number! Exiting...");
                return;
            }

            Punishment punishment = DatabaseManager.getInstance().getPunishment(integer);
            if (punishment == null) {
                MessageUtils.messagePrefix(player, "&cThat punishment ID doesn't exist!");
            } else {
                MessageUtils.message(
                        player,
                        "&e&m" + MessageUtils.StringType.CHAT_BAR.getString(),
                        " &6&lID : &7" + punishment.getId(),
                        " &6&lType : &7" + punishment.getType().getName(),
                        " &6&lTime Issued : &7" + punishment.getTime(),
                        " &6&lPunished : &7" + NameUtility.getName(punishment.getPunished()),
                        " &6&lIssuer : &7" + NameUtility.getName(punishment.getIssuer()),
                        " &6&lEnds In : &7" + (punishment.getEnd() == 0L ? "No end to this punishment!" : "&7" + TimeUtils.dateFormat.format(punishment.getEnd())),
                        " &6&lActive : &7" + (punishment.isActive() ? "Yes" : "No"),
                        " &6&lReason : &7" + (punishment.getReason()),
                        "&e&m" + MessageUtils.StringType.CHAT_BAR.getString()
                );
            }
        }
    }
}
