package net.minevn.uuidmanager.listener;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.minevn.uuidmanager.Config;
import net.minevn.uuidmanager.MySQL;
import net.minevn.uuidmanager.UuidManager;
import org.geysermc.floodgate.api.FloodgateApi;

public class BedrockJoinEvent implements Listener {

    FloodgateApi instance = FloodgateApi.getInstance();
    private final MySQL sql = MySQL.getInstance();
    private final UuidManager plugin;
    public BedrockJoinEvent(UuidManager plugin) {
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        if (instance.isFloodgatePlayer(proxiedPlayer.getUniqueId())) {
            String playerName = proxiedPlayer.getName();
            switch (sql.getData(playerName)) {
                case "0" -> {
                    sql.setData(playerName, true);
                    plugin.getLogger().info("0");
                }
                case "1" -> {plugin.getLogger().info("1");}
                case "2" -> {
                    BaseComponent text = new TextComponent();
                    text.addExtra(plugin.color(Config.prefix));
                    text.addExtra(plugin.color(Config.messages_kick));
                    proxiedPlayer.disconnect(text);
                    plugin.getLogger().info("2");
                }
            }
        }
    }
}
