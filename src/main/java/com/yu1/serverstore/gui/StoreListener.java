package com.yu1.serverstore.gui;

import com.aystudio.core.bukkit.util.inventory.GuiModel;
import com.yu1.serverstore.Main;
import com.yu1.serverstore.util.PluginLoad;
import com.yu1.serverstore.util.SendMsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

import static com.yu1.serverstore.gui.StoreGuiRead.*;
import static com.yu1.serverstore.util.PluginLoad.*;

public class StoreListener {
    Main INSTANCE = Main.getInstance();
    public StoreListener(GuiModel inv, Player player,int page)
    {
        // 打开界面
        inv.openInventory(player);
        //注册监听器
        inv.registerListener(INSTANCE);
        //点击监听事件
        inv.execute(e -> {
            e.setCancelled(true);
            if (e.getClickedInventory() == e.getInventory()) {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    return;
                }
                String itemName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
                if(e.isLeftClick())
                {
                    upPage(itemName,page,player);
                    downPage(itemName,page,player);
                }
                if(e.isRightClick())
                {
                    shopItem(itemName,player);
                }
            }
        });
    }

    void shopItem(String itemName,Player player)
    {
        for(String shopPosition : storeArray())
        {
            String newKey = "store." + shopPosition + ".";
            String storeName = storeGui.getString(newKey + "name").replace("&", "§");
            if(itemName.equals(storeName))
            {
                double needEco = storeGui.getDouble(newKey + "needEco");
                if(economy == null)
                {
                    PluginLoad.initVault();
                    if(economy == null)
                    {
                        player.sendMessage(SendMsgUtil.getConfig("Messages.noEconomy"));
                        return;
                    }
                }
                double playerHasEco = economy.getBalance(player);
                if(playerHasEco < needEco)
                {
                    player.sendMessage(SendMsgUtil.getConfig("Messages.noEco"));
                    return;
                }

                int needPoints = storeGui.getInt(newKey + "needPoints");
                if(playerPointsAPI == null)
                {
                    PluginLoad.initPlayerPoints();
                    if(playerPointsAPI == null)
                    {
                        player.sendMessage(SendMsgUtil.getConfig("Messages.noPlayerPointsAPI"));
                        return;
                    }
                }
                int playerHasPoints = playerPointsAPI.look(player.getUniqueId());
                if(playerHasPoints < needPoints)
                {
                    player.sendMessage(SendMsgUtil.getConfig("Messages.noPoints"));
                    return;
                }

                int personLimit = storeGui.getInt(newKey + "personLimit");
                int playerLimit = personLimit - dataHandle.getPlayerLimitData(player.getUniqueId(), shopPosition);
                if(playerLimit <= 0)
                {
                    player.sendMessage(SendMsgUtil.getConfig("Messages.noPersonLimit"));
                    return;
                }
                int serverLimit = storeGui.getInt(newKey + "serverLimit");
                int severLimitData = serverLimit -
                        (dataHandle.getServerLimitData(shopPosition) == null ? 0 : (int) dataHandle.getServerLimitData(shopPosition));
                if(severLimitData <= 0)
                {
                    player.sendMessage(SendMsgUtil.getConfig("Messages.noServerLimit"));
                    return;
                }

                // 更新数据处理
                Bukkit.getScheduler().runTaskLaterAsynchronously(INSTANCE, () ->
                        dataHandle.updatePersonLimitData(player.getUniqueId(), shopPosition)
                ,20L);
                dataHandle.updateServerLimitData(shopPosition);
                economy.withdrawPlayer(player, needEco);
                playerPointsAPI.take(player.getUniqueId(), needPoints);
                List<String> commands = storeGui.getStringList(newKey + "commands");
                for(String command : commands)
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                            .replace("%player%", player.getName())
                            .replace("&", "§")
                    );
                }
                player.closeInventory();
                break;
            }
        }
    }
    void upPage(String itemName,int page,Player player)
    {
        String upPage = storeGui.getString("items.up.name").replace("&", "§");
        if(!itemName.equals(upPage)) return;
        if(page != 1)
        {
            new StoreGui(player, page - 1);
        }
    }
    void downPage(String itemName,int page,Player player)
    {
        String downPage = storeGui.getString("items.down.name").replace("&", "§");
        if(!itemName.equals(downPage)) return;
        if(storeArray().size() < ((storeGui.getInt("size") - 18) * page)) return;
        new StoreGui(player, page + 1);
    }
}
