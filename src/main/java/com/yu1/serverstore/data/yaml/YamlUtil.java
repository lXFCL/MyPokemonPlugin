package com.yu1.serverstore.data.yaml;

import com.yu1.serverstore.Main;
import com.yu1.serverstore.data.DataHandle;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class YamlUtil implements DataHandle {

    public static final Main INSTANCE = Main.getInstance();

    File getExistFile(UUID playerUUID)
    {
        String baseDir = INSTANCE.getDataFolder()
                + File.separator  + "data" + File.separator;
        File dir = new File(baseDir);
        if (!dir.exists()) {
            // 如果目录不存在，则创建目录
            if (!dir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + baseDir);
            }
        }
        File file = new File(baseDir + playerUUID.toString() + ".yml");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Failed to create file: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error creating file: " + file.getAbsolutePath(), e);
            }
        }
        return file;
    }

    File getServerFile()
    {
        String baseDir = INSTANCE.getDataFolder()
                + File.separator  + "serverData" + File.separator;
        File dir = new File(baseDir);
        if (!dir.exists()) {
            // 如果目录不存在，则创建目录
            if (!dir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + baseDir);
            }
        }
        File file = new File(baseDir + "server.yml");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Failed to create file: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error creating file: " + file.getAbsolutePath(), e);
            }
        }
        return file;
    }

    static File serverFile = new YamlUtil().getServerFile();

    @Override
    public int getPlayerLimitData(UUID playerUUID, String shopPosition) {
        File file = getExistFile(playerUUID);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return yaml.getInt(shopPosition);
    }

    @Override
    public Object getServerLimitData(String shopPosition) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(serverFile);
        if (yaml.contains(shopPosition))
        {
            return yaml.getInt(shopPosition);
        }
        return null;
    }

    @Override
    public void dayReset() {
        File file1 = new File(INSTANCE.getDataFolder(), "data");
        File[] files = file1.listFiles();
        if (files != null) {
            for (File file : files) {
                if(file == null) {
                    continue;
                }
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                for (String key : yamlConfiguration.getConfigurationSection("").getKeys(false)) {
                    yamlConfiguration.set(key, 0);
                }
                try {
                    yamlConfiguration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(serverFile);
        for (String key : yamlConfiguration.getConfigurationSection("").getKeys(false)) {
            yamlConfiguration.set(key, 0);
        }
    }

    @Override
    public void updatePersonLimitData(UUID playerUUID, String shopPosition) {
        File file = getExistFile(playerUUID);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set(shopPosition, yaml.getInt(shopPosition) + 1);
        try {
            yaml.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateServerLimitData(String shopPosition) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(serverFile);
        yaml.set(shopPosition, yaml.getInt(shopPosition) + 1);
        try {
            yaml.save(serverFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
