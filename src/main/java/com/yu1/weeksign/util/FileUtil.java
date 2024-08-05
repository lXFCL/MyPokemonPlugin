package com.yu1.weeksign.util;

import com.yu1.weeksign.Main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    static Main INSTANCE = Main.getInstance();
    /**
     * 检查指定的文件是否存在。
     *
     * @param fileName 要检查的文件名
     * @return 如果文件存在返回true，否则返回false
     */
    public static boolean checkFileExists(String fileName) {
        try {
            String dir = INSTANCE.getDataFolder().getAbsolutePath();
            Path path = Paths.get(dir,"gui" + File.separator + fileName + ".yml");
            return Files.exists(path);
        } catch (Exception e) {
            // 可以在这里记录错误日志或进行其他错误处理
            INSTANCE.getLogger().info("Error checking file: " + e.getMessage());
            return false;
        }
    }
}
