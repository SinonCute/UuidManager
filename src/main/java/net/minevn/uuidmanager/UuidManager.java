package net.minevn.uuidmanager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import net.minevn.uuidmanager.command.CheckPlayer;
import net.minevn.uuidmanager.listener.BedrockJoinEvent;
import net.minevn.uuidmanager.listener.JavaJoinEvent;


public final class UuidManager extends Plugin implements Listener {

    private static UuidManager instance;
    private static MySQL sql;
    private static int players_count;
    public static UuidManager getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Config.saveDefaultConfig();
        Config.reloadConfig();
        Config.loadConfig(this);

        sql = new MySQL(this, Config.mysql_host, Config.mysql_database, Config.mysql_user, Config.mysql_pass, Config.mysql_port);
        if (Config.proxy_bedrock) {
            new BedrockJoinEvent(this);
            getLogger().info(ChatColor.GREEN +"Loaded module for Bedrock");
        } else {
            new JavaJoinEvent(this);
            getLogger().info(ChatColor.GREEN + "Loaded module for Java");
        }
        getProxy().getPluginManager().registerListener(this,this);
        getProxy().getPluginManager().registerCommand(this, new CheckPlayer("checkPlayer", "UuidManager.admin", "cp"));
        players_count = getProxy().getOnlineCount();
        new UpdateTask(this, sql);
    }

    @Override
    public void onDisable() {
    }

    public static MySQL getSql() { return sql; }

    public static int getPlayers_count() {
        return players_count;
    }

    public static void setPlayers_count(int players_count) {
        UuidManager.players_count = players_count;
    }

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
