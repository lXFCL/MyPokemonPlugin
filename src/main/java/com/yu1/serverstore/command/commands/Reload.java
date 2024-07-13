package com.yu1.serverstore.command.commands;

import com.yu1.serverstore.gui.StoreGuiRead;
import com.yu1.serverstore.util.PluginLoad;
import com.yu1.serverstore.util.SendMsgUtil;
import org.bukkit.command.CommandSender;

public class Reload {
    public Reload(CommandSender sender)
    {
        if (sender.hasPermission(SendMsgUtil.getConfig("Permission.reload"))) {
            new PluginLoad();
            StoreGuiRead.reload();
            sender.sendMessage(SendMsgUtil.getConfig("Messages.reload"));
        } else {
            sender.sendMessage(SendMsgUtil.getConfig("Messages.noPermission"));
        }
    }
}
