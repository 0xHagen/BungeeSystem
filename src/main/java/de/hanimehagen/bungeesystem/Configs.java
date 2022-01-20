package de.hanimehagen.bungeesystem;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Configs {

    private final File configFile = new File(getDataFolder(), "config.yml");
    private final File messageFile = new File(getDataFolder(), "Messages.yml");
    private final File mysqlFile = new File(getDataFolder(), "MySQL.yml");
    public static Configuration config;
    public static Configuration messages;
    public static Configuration mysql;

    public void loadConfigs() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            if (!configFile.exists()) {
                Files.copy(getPlugin().getResourceAsStream("config.yml"), configFile.toPath());
            }
            if (!messageFile.exists()) {
                Files.copy(getPlugin().getResourceAsStream("Messages.yml"), messageFile.toPath());
            }
            if (!mysqlFile.exists()) {
                Files.copy(getPlugin().getResourceAsStream("MySQL.yml"), mysqlFile.toPath());
            }

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(messageFile);
            mysql = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mysqlFile);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Plugin getPlugin() {
        return Bungeesystem.getInstance();
    }

    public File getDataFolder() {
        return getPlugin().getDataFolder();
    }

    public static Configuration getConfig() {
        return config;
    }

    public static Configuration getMessages() {
        return messages;
    }

    public static Configuration getMysql() {
        return mysql;
    }

    public boolean getBoolean(Object file, String path, boolean def) {
        return ((Configuration) file).getBoolean(path, def);
    }

    public static String getString(Object file, String path, String def) {
        return ((Configuration) file).getString(path, def);
    }

    public static long getLong(Object file, String path, long def) {
        return ((Configuration) file).getLong(path, def);
    }

    public static long getInteger(Object file, String path, int def) {
        return ((Configuration) file).getInt(path, def);
    }

}
