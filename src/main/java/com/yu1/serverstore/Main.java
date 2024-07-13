package com.yu1.serverstore;

import com.yu1.serverstore.task.DayReset;
import com.yu1.serverstore.util.PluginLoad;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main inst;
    public static Main INSTANCE;
    public static Main getInstance() {
        return inst;
    }
    public Main() {
        INSTANCE = getInstance();
    }
    public void onEnable()
    {
        inst = this;
        new PluginLoad();
        getLogger().info("§b.--.   ,--.        ,--. ");
        getLogger().info("§b \\  `.'  /,--.,--./   | ");
        getLogger().info("§b  '.    / |  ||  |`|  | ");
        getLogger().info("§b    |  |  '  ''  ' |  | ");
        getLogger().info("§b    |  |  | \\_/ |  |  |");
        getLogger().info("§b    `--'   `----'  `--' ");
        getLogger().info("§b-> 插件已被加载");
        getLogger().info("§b-> 插件作者§a§lYu1");
        getLogger().info("§b-> §c§l支持正版，请在九域下载本插件");
        getLogger().info("§b-> 插件售后交流群:928752729");
        getLogger().info("§b-> 需要定制插件可以进群加我");
        getLogger().info("§b-> 感谢你的使用");
        getLogger().info("§b-> 插件版本:1.0");
        new DayReset();
    }
    @Override
    public void onDisable()
    {
        getLogger().info("§b-> 插件关闭感谢使用");
    }
}
