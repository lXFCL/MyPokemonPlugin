package com.yu1.weeksign.datahandle.sql;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.util.TimeDateUtils;
import cc.carm.lib.easysql.hikari.HikariConfig;
import cc.carm.lib.easysql.hikari.HikariDataSource;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import com.yu1.weeksign.Main;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Administrator
 */
public class CreateDatabase {
    public static final Main INSTANCE = Main.getInstance();

    String[] dataColumn = new String[]{"uuid","day","sign"};

    static
    {
        String dbName = INSTANCE.getConfig().getString("MySQL.database");
        String driver = INSTANCE.getConfig().getString("MySQL.driver");
        String username = INSTANCE.getConfig().getString("MySQL.username");
        String password = INSTANCE.getConfig().getString("MySQL.password");
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(driver);
        config.setUsername(username);
        config.setPassword(password);
        config.setJdbcUrl("jdbc:mysql://" + INSTANCE.getConfig().getString("MySQL.ip") + ":" + INSTANCE.getConfig().getString("MySQL.port") + "/?useSSL=false");

        SQLManagerImpl si = createManager(config);
        // 建库语句
        String databaseSql = "CREATE DATABASE IF NOT EXISTS " + dbName;
        si.executeSQL(databaseSql);
        shutdownManager(si);
    }
    public static SQLManagerImpl sqlManager = createManager();
    public static void cd(){
        File folder = new File(INSTANCE.getDataFolder(), "gui");
        // 检查文件夹是否存在
        if (folder.exists() && folder.isDirectory()) {
            // 获取文件夹中的所有文件和子文件夹
            File[] files = folder.listFiles();

            // 遍历文件和子文件夹
            if (files != null) {
                for (File file : files) {
                    String tableName = removeExtension(file.getName());
                    if(!tableIsExist(tableName)) {
                        sqlManager.createTable(tableName)
                                .addColumn("id", "INT PRIMARY KEY AUTO_INCREMENT","目标id")
                                .addColumn("uuid","VARCHAR(80) NOT NULL","玩家id")
                                .addColumn("day", "int NOT NULL", "第几天")
                                .addColumn("sign", "int NOT NULL", "是否已经签到")
                                .build().executeAsync();
                        INSTANCE.getLogger().info("§a-> §f" + tableName + "建表成功!");
                    }
                }
            }
        }
    }
    public static String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, dotIndex);
    }
    public static boolean tableIsExist(String tableName) {
        try (Connection connection = sqlManager.getConnection(); ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static SQLManagerImpl createManager()
    {
        String dbName = INSTANCE.getConfig().getString("MySQL.database");
        String driver = INSTANCE.getConfig().getString("MySQL.driver");
        String url = "jdbc:mysql://" + INSTANCE.getConfig().getString("MySQL.ip") + ":" + INSTANCE.getConfig().getString("MySQL.port") + "/" + dbName +  "?useSSL=false";
        String username = INSTANCE.getConfig().getString("MySQL.username");
        String password = INSTANCE.getConfig().getString("MySQL.password");
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        return createManager(config);
    }


    public static SQLManagerImpl createManager(HikariConfig config) {
        return new SQLManagerImpl(new HikariDataSource(config));
    }

    public static void shutdownManager(SQLManager manager, boolean forceClose, boolean outputActiveQuery) {
        if (!manager.getActiveQuery().isEmpty()) {
            manager.getLogger().warn("There are " + manager.getActiveQuery().size() + " connections still running");
            for (SQLQuery value : manager.getActiveQuery().values()) {
                if (outputActiveQuery) {
                    manager.getLogger().warn(String.format("#%s -> %s", value.getAction().getShortID(), value.getSQLContent()));
                    manager.getLogger().warn(String.format("- execute at %s", TimeDateUtils.getTimeString(value.getExecuteTime())));
                }
                if (forceClose) {
                    value.close();
                }
            }
        }
        if (manager.getDataSource() instanceof HikariDataSource) {
            //Close hikari pool
            ((HikariDataSource) manager.getDataSource()).close();
        }
    }

    public static void shutdownManager(SQLManager manager) {
        shutdownManager(manager, true, manager.isDebugMode());
    }
}
