package com.xfcl.pokestore.commands.command;

import com.aystudio.core.bukkit.util.inventory.GuiModel;
import com.xfcl.pokestore.Main;
import com.xfcl.pokestore.commands.putitem.OpenUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 */
public class Open  {
    public static final Main INSTANCE = Main.getInstance();
    public static File file = new File(INSTANCE.getDataFolder(), "store.yml");
    public static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    public Open(CommandSender sender, String[] args) throws IOException {
        if (sender instanceof Player)
        {
            if (sender.hasPermission(INSTANCE.getConfig().getString("Permission.openSelf"))) {
                Player player = (Player) sender;
                openIt(player,1);
            }else {
                sender.sendMessage(INSTANCE.getConfig().getString("Messages.noPermission").replace("&", "§"));
            }
            return;
        }
        if (sender.hasPermission(INSTANCE.getConfig().getString("Permission.open"))) {
            if(args.length != 3)
            {
                sender.sendMessage(INSTANCE.getConfig().getString("Messages.noCommand").replace("&", "§"));
                sender.sendMessage(INSTANCE.getConfig().getString("Commands.open").replace("&","§"));
                return;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null)
            {
                sender.sendMessage(INSTANCE.getConfig().getString("Messages.noPlayer").replace("&", "§"));
                return;
            }
            int page = Integer.parseInt(args[2]);
            openIt(player,page);
        } else {
            sender.sendMessage(INSTANCE.getConfig().getString("Messages.noPermission").replace("&", "§"));
        }
    }
    void openIt(Player player,int page) throws IOException {
        if(page < 1)
        {
            player.sendMessage(INSTANCE.getConfig().getString("Messages.noPage").replace("&", "§"));
            return;
        }
        GuiModel inv = new GuiModel(data.getString("title").replace("%page%",page + "").replace("&", "§"), data.getInt("size"));
        // 放置图标
        OpenUtil.putItem(inv);
        OpenUtil.putPokemon(inv,player,page);
        OpenUtil.listener(inv, player, page);
    }

}
