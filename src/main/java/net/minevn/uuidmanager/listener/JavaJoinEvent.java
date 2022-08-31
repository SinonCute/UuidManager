package net.minevn.uuidmanager.listener;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ClientConnectEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        proxiedPlayer.sendMessage(new TextComponent("xin chao"));
        String playerName = proxiedPlayer.getName();
        switch (sql.getData(playerName)) {
            case "0" -> {
                sql.setData(playerName, false);
            }
            case "1" -> {
                BaseComponent text = new TextComponent();
                text.addExtra(plugin.color(Config.prefix));
                text.addExtra(plugin.color(Config.messages_kick));
                proxiedPlayer.disconnect(text);
                plugin.getLogger().info("1");
            }
            case "2" -> {
                plugin.getLogger().info("2");
            }
        }
    }
}
