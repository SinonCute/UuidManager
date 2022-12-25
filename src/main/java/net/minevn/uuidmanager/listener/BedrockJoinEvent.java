package net.minevn.uuidmanager.listener;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
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
            switch (sql.getPlayerData(playerName)) {
                case "0" -> {
                    sql.setPlayerData(playerName, true);
                }
                case "2" -> {
                    BaseComponent text = new TextComponent();
                    text.addExtra(plugin.color(Config.prefix_space + Config.prefix));
                    text.addExtra("\n" + "§f");
                    text.addExtra(plugin.color(Config.messages_kick).replace("{0}", "Java"));
                    proxiedPlayer.disconnect(text);
                }
            }
        }
    }

    @EventHandler
    public void onPingEvent(ProxyPingEvent e) {
        int total = UuidManager.getPlayers_count();
        var respone = e.getResponse();

        respone.setPlayers(new ServerPing.Players(respone.getPlayers().getMax(), total, respone.getPlayers().getSample()));

        e.setResponse(respone);
    }
}
