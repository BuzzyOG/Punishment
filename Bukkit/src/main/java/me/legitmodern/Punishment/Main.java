package me.legitmodern.Punishment;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import me.legitmodern.Punishment.Commands.*;
import me.legitmodern.Punishment.Commands.Warning;
import me.legitmodern.Punishment.Utils.ConfigManager;
import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;
import me.legitmodern.Punishment.Utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    private static Main instance;
    public static Main getInstance() {
        return instance;
    }

    private ConfigManager configManager;
    public File configFile;
    public FileConfiguration Config;

    private CommandsManager commandsManager;
    private CommandsManagerRegistration commandsRegistration;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.commandsManager = new BukkitCommandsManager();
        this.commandsRegistration = new CommandsManagerRegistration(this, this.commandsManager);

        System.out.println("--------------------------------------------");
        System.out.println("Welcome to Punishment-Bukkit v" + getDescription().getName() + "!");
        System.out.println("--------------------------------------------");

        configFile = new File(getDataFolder(), "config.yml");
        configManager.addFile(configFile);
        try {
            configManager.firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Config = configManager.getConfigFile("config.yml");
        configManager.load();
        configManager.save();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getLogger().log(Level.INFO, "Attempting to connect to the Punishment database...");
        DatabaseManager.getInstance().checkDatabase();

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        commandsRegistration.register(ListPunishments.class);
        commandsRegistration.register(ShowPunishment.class);
        commandsRegistration.register(PermanentBan.class);
        commandsRegistration.register(TemporaryBan.class);
        commandsRegistration.register(Kick.class);
        commandsRegistration.register(Warning.class);
        commandsRegistration.register(DeactivatePunishment.class);

        System.out.println("--------------------------------------------");
        System.out.println("The Punishment-Bukkit plugin has successfully enabled!");
        System.out.println("--------------------------------------------");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commandsManager.execute(cmd.getName(), args, sender, sender);
            return true;
        } catch (CommandPermissionsException e) {
            sender.sendMessage(StringUtils.colorizeString("&c&lERROR: &cYou do not have permission to use that command!"));
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(StringUtils.colorizeString(e.getUsage()));
        } catch (CommandUsageException e) {
            sender.sendMessage(StringUtils.colorizeString("&c&lERROR: &cIncorrect arguments! Usage: &6" + e.getUsage()));
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(StringUtils.colorizeString("&c&lERROR: &cA number was expected!"));
            } else {
                sender.sendMessage(StringUtils.colorizeString("&c&lERROR: &cAn unknown error has occurred!"));
            }
        } catch (CommandException e) {
            sender.sendMessage(StringUtils.colorizeString(e.getMessage()));
        }

        return true;
    }
}
