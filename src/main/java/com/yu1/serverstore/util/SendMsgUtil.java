package com.yu1.serverstore.util;


import com.yu1.serverstore.Main;

public interface SendMsgUtil {
    Main INSTANCE = Main.getInstance();
    static String getConfig(String path) {
        return INSTANCE.getConfig().getString(path).replace("&", "§");
    }

}
