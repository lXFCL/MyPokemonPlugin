package com.yu1.serverstore.gui;

import com.yu1.serverstore.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StoreGuiRead {
    static final Main INSTANCE = Main.getInstance();
    public static File storeGuiFile = new File(INSTANCE.getDataFolder(), "gui.yml");
    public static YamlConfiguration storeGui = YamlConfiguration.loadConfiguration(storeGuiFile);
    public static void reload()
    {
        storeGui = YamlConfiguration.loadConfiguration(storeGuiFile);
    }

    public static List<String> storeArray()
    {
        return new ArrayList<>(storeGui.getConfigurationSection("store").getKeys(false));
    }

}
