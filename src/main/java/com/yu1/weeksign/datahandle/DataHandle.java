package com.yu1.weeksign.datahandle;

import java.util.UUID;

public interface DataHandle {

    boolean isSign(UUID playerUUID, int day, String tableName);

    void setSign(UUID playerUUID, int day, String tableName);

    void dayReset();
}
