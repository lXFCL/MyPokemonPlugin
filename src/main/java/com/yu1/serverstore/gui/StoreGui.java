package com.yu1.serverstore.gui;

import com.aystudio.core.bukkit.util.inventory.GuiModel;
import org.bukkit.entity.Player;

import static com.yu1.serverstore.gui.StoreGuiRead.storeGui;

public class StoreGui {
    public StoreGui(Player player, int page)
    {
        GuiModel inv = new GuiModel(storeGui.getString("title")
                .replace("&", "§"),
                storeGui.getInt("size"));
        new StorePutItem(inv, page, player);
        new StoreListener(inv, player,page);
    }
}
