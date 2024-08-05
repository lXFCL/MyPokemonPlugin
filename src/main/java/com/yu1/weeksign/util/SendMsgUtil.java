package com.yu1.weeksign.util;

import com.yu1.weeksign.Main;

public interface SendMsgUtil {
    Main INSTANCE = Main.getInstance();
    static String getConfig(String path) {
        return INSTANCE.getConfig().getString(path).replace("&", "§");
    }
}
