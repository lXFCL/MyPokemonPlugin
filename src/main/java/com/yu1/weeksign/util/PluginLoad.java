package com.yu1.weeksign.util;

import com.yu1.weeksign.Main;
import com.yu1.weeksign.command.MainCommand;
import com.yu1.weeksign.datahandle.DataHandle;
import com.yu1.weeksign.datahandle.sql.CreateDatabase;
import com.yu1.weeksign.datahandle.sql.SqlUtil;
import com.yu1.weeksign.datahandle.yaml.YamlUtil;
import org.bukkit.Bukkit;

import java.io.File;

public class PluginLoad {
    Main INSTANCE = Main.getInstance();

    public static DataHandle dataHandle;
    public PluginLoad()
    {
        INSTANCE.saveDefaultConfig();
        INSTANCE.reloadConfig();
        boolean testCreate = INSTANCE.getConfig().getBoolean("Settings.testCreate");
        if(testCreate)
        {
            File days7 = new File(INSTANCE.getDataFolder() + File.separator + "gui" + File.separator + "7days.yml");
            if (!days7.exists()){
                INSTANCE.saveResource("gui" + File.separator + "7days.yml",true);
            }
            File days14 = new File(INSTANCE.getDataFolder() + File.separator + "gui" + File.separator + "14days.yml");
            if (!days14.exists()){
                INSTANCE.saveResource("gui" + File.separator + "14days.yml",true);
            }
            INSTANCE.getLogger().info(SendMsgUtil.getConfig("Messages.testCreate"));
        }
        boolean yaml = INSTANCE.getConfig().getBoolean("Settings.yaml");
        if(yaml)
        {
            dataHandle = new YamlUtil();
        }
        else
        {
            dataHandle = new SqlUtil();
            CreateDatabase.cd();
        }
        Bukkit.getPluginCommand("wsn").setExecutor(new MainCommand());
        new BukkitRun().runTaskTimerAsynchronously(INSTANCE,0,19L * 60);
    }
}

