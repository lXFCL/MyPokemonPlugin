package com.yu1.serverstore.util;

import com.yu1.serverstore.Main;
import com.yu1.serverstore.command.MainCommand;
import com.yu1.serverstore.data.DataHandle;
import com.yu1.serverstore.data.sql.CreateDatabase;
import com.yu1.serverstore.data.sql.SqlUtil;
import com.yu1.serverstore.data.yaml.YamlUtil;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;

public class PluginLoad {
    static Main INSTANCE = Main.getInstance();

    public static DataHandle dataHandle;

    private static RegisteredServiceProvider<Economy> economyProvider = null;
    public static Economy economy = null;
    private static PlayerPoints playerPoints = null;
    public static PlayerPointsAPI playerPointsAPI = null;



    public PluginLoad()
    {
        File gui = new File(INSTANCE.getDataFolder(), "gui.yml");
        if (!gui.exists()) {
            INSTANCE.saveResource("gui.yml", true);
        }
        INSTANCE.saveDefaultConfig();
        INSTANCE.reloadConfig();
        Bukkit.getPluginCommand("yss").setExecutor(new MainCommand());
        boolean isYaml = INSTANCE.getConfig().getBoolean("Settings.yaml");
        if(isYaml)
        {
            dataHandle = new YamlUtil();
        }
        else
        {
            dataHandle = new SqlUtil();
            CreateDatabase.cd();
        }
        if(!initVault()){
            INSTANCE.getLogger().info("§b-> vault插件挂钩失败，请检查是否安装了vault插件。");
        }
        else
        {
            economy = economyProvider.getProvider();
        }
        if(!initPlayerPoints()){
            INSTANCE.getLogger().info("§b-> PlayerPoints插件挂钩失败，请检查是否安装了PlayerPoints插件。");
        }else
        {
            playerPointsAPI = playerPoints.getAPI();
        }
    }

    public static boolean initVault(){
        economyProvider = INSTANCE.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        return economyProvider != null;
    }

    public static boolean initPlayerPoints() {
        try {
            Class.forName("org.black_ixx.playerpoints.PlayerPointsAPI");
        } catch (ClassNotFoundException e) {
            return false;
        }
        Plugin plugin = Main.getInstance().getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = (PlayerPoints)plugin;
        playerPointsAPI = playerPoints.getAPI();
        return true;
    }
}
