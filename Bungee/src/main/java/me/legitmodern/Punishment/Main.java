package me.legitmodern.Punishment;

import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;
import me.legitmodern.Punishment.Utils.XMLUtils;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.logging.Level;

public class Main extends Plugin {
    private static Main instance;
    public static Main getInstance() {
        return instance;
    }

    public File configFile;

    @Override
    public void onEnable() {
        instance = this;

        System.out.println("--------------------------------------------");
        System.out.println("Welcome to Punishment-Bungee v" + getDescription().getName() + "!");
        System.out.println("--------------------------------------------");

        configFile = new File(getDataFolder(), "settings.xml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            XMLUtils.copyStreamToFile(getResourceAsStream("settings.xml"), configFile);
            getLogger().log(Level.INFO, "Settings file created!");
        }
        getProxy().registerChannel("Disconnect");
        getProxy().registerChannel("TellStaff");
        getProxy().registerChannel("WarnStaff");

        DatabaseManager.getInstance().checkDatabase();

        getProxy().getPluginManager().registerListener(this, new EventListener());

        System.out.println("--------------------------------------------");
        System.out.println("The Punishment-Bukkit plugin has successfully enabled!");
        System.out.println("--------------------------------------------");
    }
}
