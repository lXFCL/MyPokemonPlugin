package com.yu1.serverstore.gui;

import com.aystudio.core.bukkit.util.inventory.GuiModel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.yu1.serverstore.gui.StoreGuiRead.*;
import static com.yu1.serverstore.util.PluginLoad.dataHandle;

public class StorePutItem {
    public StorePutItem(GuiModel inv, int page, Player player)
    {
        putOtherItem(inv);
        putStoreItem(inv,page,player);
    }
    void putOtherItem(GuiModel inv)
    {
        inv.setCloseRemove(true);
        for (String key : storeGui.getConfigurationSection("items").getKeys(false)) {
            String newKey = "items." + key + ".";
            ItemStack itemStack = new ItemStack(Material.valueOf(storeGui.getString(newKey + "type")),
                    storeGui.getInt(newKey + "amount") == 0 ? 1 : storeGui.getInt(newKey + "amount"),
                    (short) storeGui.getInt(newKey + "storeGui"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(storeGui.getString(newKey + "name").replace("&", "§"));
            List<String> lore = new ArrayList<>();
            storeGui.getStringList(newKey + "lore").forEach((l) -> lore.add(l
                    .replace("&", "§")));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            if (storeGui.getStringList(newKey + "slots").isEmpty()) {
                inv.setItem(storeGui.getInt(newKey + "slot"), itemStack);
            } else {
                storeGui.getIntegerList(newKey + "slots").forEach((slot) -> inv.setItem(slot, itemStack));
            }
        }
    }
    void putStoreItem(GuiModel inv,int page, Player player)
    {
        int canPutSlots = storeGui.getInt("size") - 18;
        List<String> keyArray;
        if(storeArray().size() < canPutSlots * page)
            keyArray = storeArray().subList(canPutSlots * (page - 1), storeArray().size());
        else
            keyArray = storeArray().subList(canPutSlots * (page - 1), canPutSlots * page);

        int index = 0;
        for (String shopPosition : keyArray) {
            String newKey = "store." + shopPosition + ".";
            ItemStack itemStack = new ItemStack(Material.valueOf(storeGui.getString(newKey + "type")),
                    storeGui.getInt(newKey + "amount") == 0 ? 1 : storeGui.getInt(newKey + "amount"),
                    (short) storeGui.getInt(newKey + "storeGui"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(storeGui.getString(newKey + "name").replace("&", "§"));
            List<String> lore = new ArrayList<>();
            double needEco = storeGui.getDouble(newKey + "needEco");
            int needPoints = storeGui.getInt(newKey + "needPoints");
            int personLimit = storeGui.getInt(newKey + "personLimit");
            int serverLimit = storeGui.getInt(newKey + "serverLimit");
            int playerLimit = personLimit - dataHandle.getPlayerLimitData(player.getUniqueId(), shopPosition);
            int severLimitData = serverLimit -
                    (dataHandle.getServerLimitData(shopPosition) == null ? 0 : (int) dataHandle.getServerLimitData(shopPosition));
            storeGui.getStringList(newKey + "lore").forEach((l) -> lore.add(l
                    .replace("%needEco%", String.valueOf(needEco))
                    .replace("%needPoints%", String.valueOf(needPoints))
                    .replace("%personLimit%", String.valueOf(playerLimit))
                    .replace("%serverLimit%", String.valueOf(severLimitData))
                    .replace("&", "§")));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inv.setItem(index, itemStack);
            index++;
        }
    }
}
