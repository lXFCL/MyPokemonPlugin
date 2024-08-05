package com.yu1.weeksign.util;

import com.yu1.weeksign.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.Date;

import static com.yu1.weeksign.util.PluginLoad.dataHandle;

public class BukkitRun extends BukkitRunnable {
    public final Main INSTANCE = Main.getInstance();
    @Override
    public void run() {
        String times = INSTANCE.getConfig().getString("Settings.times");
        String[] timeArray = times.split(":");
        int targetHour = Integer.parseInt(timeArray[0]);
        int targetMinute = Integer.parseInt(timeArray[1]);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date());
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(new Date());
        compareCalendar.set(Calendar.HOUR_OF_DAY, targetHour);
        compareCalendar.set(Calendar.MINUTE, targetMinute);
        if (currentCalendar.get(Calendar.HOUR_OF_DAY) == compareCalendar.get(Calendar.HOUR_OF_DAY)
                && currentCalendar.get(Calendar.MINUTE) == compareCalendar.get(Calendar.MINUTE)) {
            dataHandle.dayReset();
            INSTANCE.getLogger().info("§a-> §f已重置签到数据");
        }
    }
}
