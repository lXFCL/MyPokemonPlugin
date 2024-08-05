package com.yu1.weeksign.gui;

import com.aystudio.core.bukkit.util.inventory.GuiModel;
import com.yu1.weeksign.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;


public class WeekGui {
    public static final Main INSTANCE = Main.getInstance();
    public static GuiModel inv;

    public static Player player;

    public static String tableName;

    public WeekGui(Player player, String fileName)
    {
        tableName = fileName;
        WeekGui.player = player;
        File week = new File(INSTANCE.getDataFolder(), "gui" + File.separator + fileName + ".yml");
        YamlConfiguration weekGuiLoad = YamlConfiguration.loadConfiguration(week);
        inv = new GuiModel(weekGuiLoad.getString("title")
                .replace("&", "§"), weekGuiLoad.getInt("size"));
        WeekGuiPutItem.putItem(weekGuiLoad);
        WeekGuiListener.open(weekGuiLoad);
    }
}
