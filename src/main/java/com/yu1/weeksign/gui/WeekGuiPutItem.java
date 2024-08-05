package com.yu1.weeksign.gui;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.yu1.weeksign.util.PluginLoad.dataHandle;

public class WeekGuiPutItem extends WeekGui{
    public WeekGuiPutItem(Player player, String fileName) {
        super(player, fileName);
    }

    public static void putItem(YamlConfiguration data)
    {
        inv.setCloseRemove(true);
        for (String key : data.getConfigurationSection("items").getKeys(false)) {
            String newKey = "items." + key + ".";
            // 这个物品是指代第几天的？
            int day = data.getInt(newKey + "day");
            // 获取这个玩家这一天是否已经签到了
            boolean isSign = dataHandle.isSign(player.getUniqueId(), day, tableName);
            // 已经签到的物品type
            String aType = data.getString("SignItem.type");
            // 已经签到的物品data
            short aData = (short) data.getInt("SignItem.data");

            // 未签到的物品type
            String nType = data.getString(newKey + "type");
            short nData = (short) data.getInt(newKey + "data");
            ItemStack itemStack = new ItemStack(Material.valueOf(isSign ? aType : nType),
                    data.getInt(newKey + "amount") == 0 ? 1 : data.getInt(newKey + "amount"),
                    isSign ? aData : nData);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(data.getString(newKey + "name").replace("&", "§"));
            List<String> lore = new ArrayList<>();
            data.getStringList(newKey + "lore").forEach((l) -> lore.add(l
                    .replace("%isSign%", isSign ? data.getString("show.alreadySign") : data.getString("show.notSign"))
                    .replace("&", "§")));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            if (data.getStringList(newKey + "slots").isEmpty()) {
                inv.setItem(data.getInt(newKey + "slot"), itemStack);
            } else {
                data.getIntegerList(newKey + "slots").forEach((slot) -> inv.setItem(slot, itemStack));
            }
        }
    }
}
