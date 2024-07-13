package com.yu1.serverstore.command.commands;

import com.yu1.serverstore.gui.StoreGui;
import com.yu1.serverstore.util.SendMsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Open {
    public Open(CommandSender sender, String[] args){
        if (args.length == 2) {
            if (sender.hasPermission(SendMsgUtil.getConfig("Permission.open"))) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(SendMsgUtil.getConfig("Messages.noPlayer"));
                    return;
                }
                new StoreGui(player, 1);
            } else {
                sender.sendMessage(SendMsgUtil.getConfig("Messages.noPermission"));
            }
        } else {
            sender.sendMessage(SendMsgUtil.getConfig("Messages.inputWrong"));
            sender.sendMessage(SendMsgUtil.getConfig("Commands.open"));
        }
    }
}
