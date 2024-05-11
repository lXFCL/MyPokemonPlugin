package com.xfcl.pokestore.commands;

import com.xfcl.pokestore.Main;
import com.xfcl.pokestore.commands.command.Open;
import com.xfcl.pokestore.commands.command.Reload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

/**
 * @author Administrator
 */
public class MainCommand implements CommandExecutor {

    public static final Main INSTANCE = Main.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            for(String msg : INSTANCE.getConfig().getConfigurationSection("Commands").getKeys(false))
            {
                sender.sendMessage(INSTANCE.getConfig().getString("Commands." + msg).replace("&", "§"));
            }
        }else {
            switch (args[0]) {
                case "reload":
                    new Reload(sender);
                    break;
                case "open":
                    try {
                        new Open(sender, args);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
