package com.yu1.weeksign.command.commands;

import com.yu1.weeksign.command.MainCommand;
import com.yu1.weeksign.gui.WeekGui;
import com.yu1.weeksign.util.FileUtil;
import com.yu1.weeksign.util.SendMsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Open extends MainCommand {
    public Open(CommandSender sender, String[] args)
    {
        if (args.length == 3) {
            if (sender.hasPermission("wsn.open")) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(SendMsgUtil.getConfig("Messages.noPlayer"));
                    return;
                }
                String chooseName = args[2];
                boolean checkIsExist = FileUtil.checkFileExists(chooseName);
                if(checkIsExist)
                {
                    new WeekGui(player,chooseName);
                }
                else
                {
                    sender.sendMessage(SendMsgUtil.getConfig("Messages.noFile"));
                }
            } else {
                sender.sendMessage(SendMsgUtil.getConfig("Messages.noPermission"));
            }
        } else {
            sender.sendMessage(SendMsgUtil.getConfig("Messages.inputWrong"));
            sender.sendMessage(SendMsgUtil.getConfig("Commands.open"));
        }
    }
}
