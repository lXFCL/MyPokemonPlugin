package com.yu1.weeksign.datahandle.sql;

import cc.carm.lib.easysql.api.SQLQuery;
import com.yu1.weeksign.datahandle.DataHandle;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.ResultSet;
import java.util.UUID;

import static com.yu1.weeksign.util.PluginLoad.dataHandle;

public class SqlUtil extends CreateDatabase implements DataHandle {
    @Override
    public boolean isSign(UUID playerUUID, int day, String tableName) {
        int sign = -1;
        String[] conditions = new String[]{"uuid","day"};
        Object[] objects = new Object[]{playerUUID.toString(),day};
        // 执行查询（同步方式）
        try (SQLQuery query = sqlManager.createQuery()
                .inTable(tableName)
                .selectColumns(dataColumn)
                .addCondition(conditions, objects)
                .build()
                .execute()) {
            // SQLQuery 关闭时，ResultSet 会一同关闭
            ResultSet set = query.getResultSet();
            if (set.next()) {
                sign = set.getInt("sign");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if(sign == -1)
        {
            sqlManager.createInsert(tableName)
                    .setColumnNames(dataColumn)
                    .setParams(playerUUID.toString(), day, 0)
                    .executeAsync();
            return false;
        }
        else if(sign == 0)
            return false;
        else return sign == 1;
    }

    @Override
    public void setSign(UUID playerUUID, int day, String tableName) {
        String[] conditions = new String[]{"uuid","day"};
        Object[] objects = new Object[]{playerUUID.toString(),day};
        sqlManager.createUpdate(tableName)
                .addCondition(conditions, objects)
                .setColumnValues("sign",1)
                .build()
                .executeAsync();
    }

    @Override
    public void dayReset() {
        File folder = new File(INSTANCE.getDataFolder(), "gui");
        // 检查文件夹是否存在
        if (folder.exists() && folder.isDirectory()) {
            // 获取文件夹中的所有文件和子文件夹
            File[] files = folder.listFiles();
            // 遍历文件和子文件夹
            if (files != null) {
                for (File file : files) {
                    String tableName = removeExtension(file.getName());
                    YamlConfiguration weekGuiLoad = YamlConfiguration.loadConfiguration(file);
                    int finalDay = weekGuiLoad.getInt("Settings.final");
                    boolean next = weekGuiLoad.getBoolean("Settings.next");
                    if(next)
                    {
                        next(tableName,finalDay);
                    }
                    boolean continuous = weekGuiLoad.getBoolean("Settings.continuous");
                    if(continuous)
                    {
                        todayIsSign(tableName,finalDay);
                    }
                    sqlManager.createUpdate(tableName)
                            .addCondition("day", "=", 666)
                            .setColumnValues("sign",0)
                            .build()
                            .executeAsync();
                }
            }
        }
    }

    // 查询一下这个表中今天没有签到的人，然后把他的签到状态设置为0
    void todayIsSign(String tableName, int finalDay)
    {
        String[] conditions = new String[]{"day","sign"};
        Object[] objects = new Object[]{666,0};
        try (SQLQuery query = sqlManager.createQuery()
                .inTable(tableName)
                .selectColumns(dataColumn)
                .addCondition(conditions, objects)
                .build()
                .execute()) {
            ResultSet set = query.getResultSet();
            while (set.next()) {
                String uuid = set.getString("uuid");
                if(isSign(UUID.fromString(uuid), finalDay,tableName)) continue;
                reset(tableName, finalDay, uuid);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // 查询表中已经签到到最终天数的人，然后把签到状态设置为0
    void next(String tableName, int finalDay)
    {
        String[] conditions = new String[]{"day","sign"};
        Object[] objects = new Object[]{finalDay,1};
        try (SQLQuery query = sqlManager.createQuery()
                .inTable(tableName)
                .selectColumns(dataColumn)
                .addCondition(conditions, objects)
                .build()
                .execute()) {
            ResultSet set = query.getResultSet();
            while (set.next()) {
                String uuid = set.getString("uuid");
                reset(tableName, finalDay, uuid);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void reset(String tableName, int finalDay, String uuid) {
        for (int i = 1; i <= finalDay; i++)
        {
            String[] c1 = new String[]{"uuid","day"};
            Object[] o1 = new Object[]{uuid,i};
            // 更新数据处理
            Bukkit.getScheduler().runTaskLaterAsynchronously(INSTANCE, () ->
                            sqlManager.createUpdate(tableName)
                                    .addCondition(c1, o1)
                                    .setColumnValues("sign",0)
                                    .build()
                                    .executeAsync()
                    ,8L + i);
        }
    }
}
