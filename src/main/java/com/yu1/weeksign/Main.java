package com.yu1.weeksign;

import com.yu1.weeksign.util.PluginLoad;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main inst;
    public static Main INSTANCE;
    public static Main getInstance() {
        return inst;
    }
    public Main() {
        INSTANCE = getInstance();
    }
    @Override
    public void onEnable()
    {
        inst = this;
        getLogger().info("§b§l.--.   ,--.        ,--. ");
        getLogger().info("§b§l \\  `.'  /,--.,--./   | ");
        getLogger().info("§b§l  '.    / |  ||  |`|  | ");
        getLogger().info("§b§l    |  |  '  ''  ' |  | ");
        getLogger().info("§b§l    |  |  | \\_/ |  |  |");
        getLogger().info("§b§l    `--'   `----'  `--' ");
        getLogger().info("§a-> §f插件已被加载");
        getLogger().info("§a-> §f插件作者§a§lYu1");
        getLogger().info("§a-> §c§l支持正版，请在九域下载本插件");
        getLogger().info("§a-> §f插件售后交流群:928752729");
        getLogger().info("§a-> §f需要定制插件可以进群加我");
        getLogger().info("§a-> §f感谢你的使用");
        getLogger().info("§a-> §f插件版本:1.0");
        new PluginLoad();
    }


    @Override
    public void onDisable()
    {
        getLogger().info("§b-> 插件关闭感谢使用");
    }
}
