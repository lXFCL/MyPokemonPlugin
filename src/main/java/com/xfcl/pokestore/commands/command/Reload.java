package com.xfcl.pokestore.commands.command;

import com.xfcl.pokestore.commands.MainCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Administrator
 */
public class Reload extends MainCommand {
    public Reload(CommandSender sender)
    {
        if (sender.hasPermission(INSTANCE.getConfig().getString("Permission.reload"))) {
            INSTANCE.reloadConfig();
            sender.sendMessage(INSTANCE.getConfig().getString("Messages.reload").replace("&", "§"));
        } else {
            sender.sendMessage(INSTANCE.getConfig().getString("Messages.noPermission").replace("&", "§"));
        }
    }
}
