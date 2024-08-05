package com.yu1.weeksign.datahandle.yaml;

import com.yu1.weeksign.datahandle.DataHandle;

import java.util.UUID;

public class YamlUtil implements DataHandle {

    @Override
    public boolean isSign(UUID playerUUID, int day, String tableName) {
        return false;
    }

    @Override
    public void setSign(UUID playerUUID, int day, String tableName) {

    }

    @Override
    public void dayReset() {

    }
}
