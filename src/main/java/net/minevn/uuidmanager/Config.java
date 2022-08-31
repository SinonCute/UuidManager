package net.minevn.uuidmanager;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {
    private static Configuration config;

    private static final UuidManager plugin = UuidManager.getInstance();

    public static boolean proxy_bedrock;
    public static String prefix;
    public static String mysql_host;
    public static int mysql_port;
    public static String mysql_database;
    public static String mysql_user;
    public static String mysql_pass;
    public static String messages_kick;

    public static void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("Can't load config.yml");
        }
    }

    public static void saveDefaultConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Configuration getConfig() {
        return config;
    }

    public static void loadConfig(UuidManager plugin) {

        Configuration config = getConfig();
        proxy_bedrock = config.getBoolean("BedrockProxy");
        prefix = config.getString("prefix");
        mysql_host = config.getString("MySQL.host");
        mysql_port = config.getInt("MySQL.port");
        mysql_database = config.getString("MySQL.database");
        mysql_user = config.getString("MySQL.user");
        mysql_pass = config.getString("MySQL.pass");
        messages_kick = config.getString("messages.kick");
    }
}
