package com.xfcl.pokestore.util;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 */
public class FileConut {
    public static int getCount(String filePath){
        // 创建File对象
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // 检查路径是否为目录
        if (directory.isDirectory()) {
            // 使用listFiles()获取目录中的文件和子目录
            File[] files = directory.listFiles();

            // 如果files不为空，计算文件数量（不包括子目录）
            if (files != null) {
                int fileCount = 0;
                for (File file : files) {
                    if (!file.isDirectory()) {
                        fileCount++;
                    }
                }
                return fileCount;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
