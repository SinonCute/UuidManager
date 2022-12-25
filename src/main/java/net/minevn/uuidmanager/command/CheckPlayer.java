package net.minevn.uuidmanager.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.minevn.uuidmanager.MySQL;
import net.minevn.uuidmanager.UuidManager;

public class CheckPlayer extends Command {

    public CheckPlayer(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    private final static MySQL sql = MySQL.getInstance();
    private final static UuidManager plugin = UuidManager.getInstance();
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            String username = args[0];
            switch (sql.getPlayerData(username)) {
                case "0" -> plugin.getLogger().info(("§cPlayer {0} không tồn tại").replace("{0}", username));
                case "1" -> plugin.getLogger().info(("§aPlayer {0} đã chơi trên Bedrock").replace("{0}", username));
                case "2" -> plugin.getLogger().info(("§aPlayer {0} đã chơi trên Java").replace("{0}", username));
            }
            plugin.getLogger().info(plugin.color("&cSai cú pháp /checkplayer {name}"));
        }
    }
}
