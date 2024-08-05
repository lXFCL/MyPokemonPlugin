package com.yu1.weeksign.command.commands;

import com.yu1.weeksign.command.MainCommand;
import com.yu1.weeksign.util.SendMsgUtil;
import org.bukkit.command.CommandSender;

public class Reload extends MainCommand {
    public Reload(CommandSender sender)
    {
        if (sender.hasPermission("po.reload")) {
            INSTANCE.saveDefaultConfig();
            INSTANCE.reloadConfig();
            sender.sendMessage(SendMsgUtil.getConfig("Messages.reload"));
        } else {
            sender.sendMessage(SendMsgUtil.getConfig("Messages.noPermission"));
        }
    }
}