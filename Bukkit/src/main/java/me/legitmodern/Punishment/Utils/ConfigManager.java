package me.legitmodern.Punishment.Utils;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private Plugin plugin;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private List<File> files = new ArrayList<>();
    private Map<File, YamlConfiguration> configurationMap = new HashMap<>();

    /**
     * Add a file to the configuration manager
     *
     * @param file File to add
     */
    public void addFile(File file) {
        files.add(file);
        configurationMap.put(file, new YamlConfiguration());
    }

    /**
     * Get the files inside of the configuration manager
     *
     * @return Files inside of config manager
     */
    public List<File> getFiles() {
        return this.files;
    }

    /**
     * Get the map of files and configuration files
     *
     * @return Map of configuration files and files
     */
    public Map<File, YamlConfiguration> getConfigurationMap() {
        return this.configurationMap;
    }

    /**
     * Test for the first run
     *
     * @throws Exception
     */
    public void firstRun() throws Exception {
        for (File f : getFiles()) {
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                copyStreamToFile(plugin.getResource(f.getName()), f);
            }
        }
    }

    /**
     * Get a file inside of the configuration list
     *
     * @param name File name
     * @return File
     */
    public File getFile(String name) {
        for (File f : files) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }

        return null;
    }

    /**
     * Get a config file from the configuration
     *
     * @param name Name of file
     * @return ConfigFile
     */
    public FileConfiguration getConfigFile(String name) {
        for (File f : getConfigurationMap().keySet()) {
            if (f.getName().equalsIgnoreCase(name)) {
                return getConfigurationMap().get(f);
            }
        }

        return null;
    }

    /**
     * Save all of the YAML files
     */
    public void save() {
        for (File f : getFiles()) {
            try {
                getConfigFile(f.getName()).save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Load all of the YAML files
     */
    public void load() {
        for (File f : getFiles()) {
            try {
                getConfigFile(f.getName()).load(f);
            } catch (InvalidConfigurationException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copy an input stream to a file
     *
     * @param inputStream Input stream to copy
     * @param destination Destination file
     */
    public void copyStreamToFile(InputStream inputStream, File destination) {
        Preconditions.checkNotNull(inputStream, "Input Stream is null");
        Preconditions.checkNotNull(destination, "Destination is null");

        try {
            OutputStream out = new FileOutputStream(destination);
            byte[] buf = new byte[1024];
            int len;

            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            out.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}