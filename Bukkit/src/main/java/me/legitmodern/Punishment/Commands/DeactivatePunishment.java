package me.legitmodern.Punishment.Commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.legitmodern.Punishment.Utils.Chat.MessageUtils;
import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;
import me.legitmodern.Punishment.API.Punishment;
import me.legitmodern.Punishment.Utils.UUID.NameUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeactivatePunishment {
    @Command(
            aliases = {"deactivatepunishment", "dpunishment", "dp"},
            desc = "Deactivates a punishment.",
            min = 1,
            max = 1,
            usage = "[id]"
    )
    @CommandPermissions({"punishment.staff.deactivate"})
    public static void deactivatePunishment(CommandContext args, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            int integer;
            try {
                integer = Integer.parseInt(args.getString(0));
            } catch (NumberFormatException e) {
                MessageUtils.messagePrefix(player, "&cYou must enter a number! Exiting...");
                return;
            }

            Punishment punishment = DatabaseManager.getInstance().getPunishment(integer);
            if (punishment == null) {
                MessageUtils.messagePrefix(player, "&cThat punishment ID doesn't exist!");
            } else {
                punishment.setActive(false);
                punishment.save();

                MessageUtils.messagePrefix(player, "&aDeactivated punishment ID &6#" + args.getInteger(0) + "&afor player &6" + NameUtility.getName(punishment.getPunished()) + "&a!");
            }
        }
    }
}
