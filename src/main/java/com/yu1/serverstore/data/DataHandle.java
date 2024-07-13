package com.yu1.serverstore.data;

import java.util.UUID;

public interface DataHandle {

    int getPlayerLimitData(UUID playerUUID, String shopPosition);

    Object getServerLimitData(String shopPosition);

    void dayReset();

    void updatePersonLimitData(UUID playerUUID, String shopPosition);

    void updateServerLimitData(String shopPosition);
}
