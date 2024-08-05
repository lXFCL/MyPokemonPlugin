package com.yu1.weeksign.gui;

import com.yu1.weeksign.util.SendMsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

import static com.yu1.weeksign.util.PluginLoad.dataHandle;

public class WeekGuiListener extends WeekGui{

    public WeekGuiListener(Player player, String fileName) {
        super(player, fileName);
    }

    private static YamlConfiguration data;

    // 被点击的物品名称
    private static String itemName;

    public static void open(YamlConfiguration weekGuiLoad)
    {
        data = weekGuiLoad;
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
                itemName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
                if(e.isLeftClick())
                {
                    day();
                }
            }
        });
    }

    private static void day()
    {
        for (String key : data.getConfigurationSection("items").getKeys(false)) {
            String newKey = "items." + key + ".";
            String name = data.getString( newKey + "name").replace("&", "§");
            if(itemName.equals(name))
            {
                // 获取这个玩家今天签到了没
                boolean todaySign = dataHandle.isSign(player.getUniqueId(), 666, tableName);
                if(todaySign)
                {
                    player.sendMessage(SendMsgUtil.getConfig("Messages.todaySign"));
                    return;
                }
                // 该物品对应的天数
                int day = data.getInt(newKey + "day");
                if(day == 0) return;
                // 获取该物品对应的天数是否签到
                boolean isSign = dataHandle.isSign(player.getUniqueId(), day, tableName);
                // 获取上一天是否签到
                boolean isSignBefore = false;
                if(day == 1) isSignBefore = true;
                else if(day > 1) isSignBefore = dataHandle.isSign(player.getUniqueId(), day - 1, tableName);
                // 假如上一天没签到
                if(!isSignBefore)
                {
                    player.sendMessage(SendMsgUtil.getConfig("Messages.notSignBefore"));
                    return;
                }
                // 假如上一天签到了，且这个没被签到
                if(!isSign)
                {
                    // 更新数据处理
                    Bukkit.getScheduler().runTaskLaterAsynchronously(INSTANCE, () ->
                            dataHandle.setSign(player.getUniqueId(), 666, tableName)
                    ,8L);
                    dataHandle.setSign(player.getUniqueId(), day, tableName);
                    List<String> commands = data.getStringList(newKey + "commands");
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
                break;
            }
        }
    }
}
