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

public class JavaJoinEvent implements Listener {

    private final MySQL sql = MySQL.getInstance();
    private final UuidManager plugin;
    public JavaJoinEvent(UuidManager plugin) {
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        String playerName = proxiedPlayer.getName();
        switch (sql.getPlayerData(playerName)) {
            case "0" -> {
                sql.setPlayerData(playerName, false);
            }
            case "1" -> {
                BaseComponent text = new TextComponent();
                text.addExtra(plugin.color(Config.prefix));
                text.addExtra("\n");
                text.addExtra(plugin.color(Config.messages_kick).replace("{0}", "Bedrock"));
                proxiedPlayer.disconnect(text);
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
