package com.xfcl.pokestore;

import com.aystudio.core.common.auth.PluginAuth;
import com.xfcl.pokestore.commands.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.*;

/**
 * @author Administrator
 */
public class Main extends JavaPlugin {
    private static Main inst;
    public static Main INSTANCE;

    public static Main getInstance() {
        return inst;
    }
    public boolean connectState = false;
    public Main() {
        INSTANCE = getInstance();
    }
    private final String ip = "yu1.email";
    private final int port = 10803;
    public void connect(){
        try {
            new Socket(ip,port);
            connectState = true;
        }catch (Exception e){
            e.printStackTrace();
            connectState = false;
        }
    }
    public void reconnect(){
        for (int i = 0; i < 3; i++){
            connect();
            if(connectState) {
                break;
            }
            System.out.println("§b§l[宝可梦仓库] §a-> §f连接失败正在重连");
            try {
                Thread.sleep(1200);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onLoad() {
        System.out.println("§b§l[宝可梦仓库] §a-> §f插件已被载入");
    }
    public static String getMac(){
        byte[] mac = new byte[0];
        try {
            mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        StringBuilder mx = new StringBuilder();
        for (byte b: mac){
            int tmp = b & 0xFF;
            String str = Integer.toHexString(tmp);
            if (str.length() == 1){
                mx.append("0").append(str);
            }else{
                mx.append(str);
            }
        }
        return mx.toString().toUpperCase();
    }
    @Override
    public void onEnable() {
        inst = this;
//        File gui = new File(getDataFolder(), "key.yml");
//        if (!gui.exists()) {
//            saveResource("key.yml", true);
//        }
//        YamlConfiguration filec = YamlConfiguration.loadConfiguration(gui);
        File store = new File(getDataFolder(), "store.yml");
        if (!store.exists()) {
            saveResource("store.yml", true);
        }
        System.out.println("§b __   __   ____    ____     __        ");
        System.out.println("§b/\\ \\ /\\ \\ /\\  _`\\ /\\  _`\\  /\\ \\       ");
        System.out.println("§b\\ `\\`\\/'/'\\ \\ \\L\\_\\ \\ \\/\\_\\\\ \\ \\      ");
        System.out.println("§b `\\/ > <   \\ \\  _\\/\\ \\ \\/_/_\\ \\ \\  __ ");
        System.out.println("§b    \\/'/\\`\\ \\ \\ \\/  \\ \\ \\L\\ \\\\ \\ \\L\\ \\");
        System.out.println("§b    /\\_\\\\ \\_\\\\ \\_\\   \\ \\____/ \\ \\____/");
        System.out.println("§b    \\/_/ \\/_/ \\/_/    \\/___/   \\/___/ ");
        System.out.println("§b§l[宝可梦仓库] §a-> §f插件已被加载");
        System.out.println("§b§l[宝可梦仓库] §a-> §f插件作者§a§lYu1");
        System.out.println("§b§l[宝可梦仓库] §a-> §f§c§l支持正版，请在九域下载本插件");
        System.out.println("§b§l[宝可梦仓库] §a-> §f插件售后交流群:928752729");
        System.out.println("§b§l[宝可梦仓库] §a-> §f需要定制插件可以进群加我");
        System.out.println("§b§l[宝可梦仓库] §a-> §f感谢你的使用");
        System.out.println("§b§l[宝可梦仓库] §a-> §f插件版本:1.0");
//        System.out.println("§b§l[宝可梦仓库] §a-> §f请在key.yml处填写授权码");
//        System.out.println("§b§l[宝可梦仓库] §a-> §f请联系作者添加授权");
//        System.out.println("§b§l[宝可梦仓库] §a-> §f如验证失败则返回报错");
//        System.out.println("§b§l[宝可梦仓库] §a-> §f正在开启验证...");
        saveDefaultConfig();
        reloadConfig();
        Bukkit.getPluginCommand("ps").setExecutor(new MainCommand());
//        connect();
//        if(!connectState) {
//            reconnect();
//        }
//        if(filec.getString("mode") == null ||filec.getString("mode").equals("mac"))
//        {
//            new PluginAuth(this,filec.getString("key") + "_" + getMac() , ip,
//                    port,"com.xfcl.PokemonRankPvp.PokemonRankPvp_load");
//        }
//        else
//        {
//            new PluginAuth(this,filec.getString("key"), ip,
//                    port,"com.xfcl.PokemonRankPvp.PokemonRankPvp_load");
//        }
    }

    @Override
    public void onDisable() {
        System.out.println("§b§l[宝可梦仓库] §a-> §f感谢使用，插件卸载");
    }
}
