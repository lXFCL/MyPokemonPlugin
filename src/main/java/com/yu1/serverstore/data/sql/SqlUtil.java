package com.yu1.serverstore.data.sql;

import cc.carm.lib.easysql.api.SQLQuery;
import com.yu1.serverstore.data.DataHandle;

import java.sql.ResultSet;
import java.util.UUID;

public class SqlUtil extends CreateDatabase implements DataHandle {
    @Override
    public int getPlayerLimitData(UUID playerUUID, String shopPosition) {
        String[] condition = new String[]{"uuid","shopPosition"};
        Object[] params = new Object[]{playerUUID.toString(),shopPosition};
        // 执行查询（同步方式）
        try (SQLQuery query = sqlManager.createQuery()
                .inTable(tableName)
                .selectColumns(tableNameDataColumn)
                .addCondition(condition, params)
                .build()
                .execute()) {
            // SQLQuery 关闭时，ResultSet 会一同关闭
            ResultSet set = query.getResultSet();
            if (set.next()) {
                return set.getInt("shopLimit");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    @Override
    public Object getServerLimitData(String shopPosition) {
        // 执行查询（同步方式）
        try (SQLQuery query = sqlManager.createQuery()
                .inTable(serverLimitTableName)
                .selectColumns(serverLimitDataColumn)
                .addCondition("shopPosition", shopPosition)
                .build()
                .execute()) {
            // SQLQuery 关闭时，ResultSet 会一同关闭
            ResultSet set = query.getResultSet();
            if (set.next()) {
                return set.getObject("shopLimit");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }


    @Override
    public void updatePersonLimitData(UUID playerUUID, String shopPosition) {
        if(getPlayerLimitData(playerUUID,shopPosition)== -1)
        {
            sqlManager.createInsert(tableName)
                    .setColumnNames(tableNameDataColumn)
                    .setParams(playerUUID.toString(),shopPosition,1)
                    .executeAsync();
        }
        else
        {
            String[] condition = new String[]{"uuid","shopPosition"};
            Object[] params = new Object[]{playerUUID.toString(),shopPosition};
            sqlManager.createUpdate(tableName)
                    .addCondition(condition, params)
                    .setColumnValues("shopLimit",getPlayerLimitData(playerUUID,shopPosition)+1)
                    .build()
                    .executeAsync();
        }
    }

    @Override
    public void updateServerLimitData(String shopPosition) {
        if(getServerLimitData(shopPosition) == null)
        {
            sqlManager.createInsert(serverLimitTableName)
                    .setColumnNames(serverLimitDataColumn)
                    .setParams(shopPosition,1)
                    .executeAsync();
        }
        else
        {
            sqlManager.createUpdate(serverLimitTableName)
                    .addCondition("shopPosition", shopPosition)
                    .setColumnValues("shopLimit",(int)getServerLimitData(shopPosition)+1)
                    .build()
                    .executeAsync();
        }
    }

    @Override
    public void dayReset() {
        sqlManager.createUpdate(tableName)
                .addCondition("id", ">", 0)
                .setColumnValues("shopLimit",0)
                .build()
                .executeAsync();
        sqlManager.createUpdate(serverLimitTableName)
                .addCondition("id", ">", 0)
                .setColumnValues("shopLimit",0)
                .build()
                .executeAsync();
    }

}
