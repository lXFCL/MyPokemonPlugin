package com.yu1.serverstore.task;


import com.yu1.serverstore.Main;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.yu1.serverstore.util.PluginLoad.dataHandle;


public class DayReset{
    public final Main INSTANCE = Main.getInstance();
    public DayReset()
    {
        String times = INSTANCE.getConfig().getString("Settings.times");
        String[] timeArray = times.split(":");
        int targetHour = Integer.parseInt(timeArray[0]);
        int targetMinute = Integer.parseInt(timeArray[1]);

        scheduleDailyTask(() -> {
            dataHandle.dayReset();
            INSTANCE.getLogger().info("§b-> 已重置今日限制次数！");
        }, targetHour, targetMinute);
    }
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * 定时执行任务。
     * @param task 要执行的任务
     * @param targetHour 目标执行小时数
     * @param targetMinute 目标执行分钟数
     */
    public static void scheduleDailyTask(Runnable task, int targetHour, int targetMinute) {
        long initialDelay = calculateInitialDelay(targetHour, targetMinute);
        scheduler.scheduleAtFixedRate(task, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
    }

    /**
     * 计算首次执行的延迟时间。
     * @param targetHour 目标执行小时数
     * @param targetMinute 目标执行分钟数
     * @return 首次执行的延迟时间（秒）
     */
    private static long calculateInitialDelay(int targetHour, int targetMinute) {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, targetHour);
        calendar.set(Calendar.MINUTE, targetMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < now) {
            // 如果目标时间已经过去，那么等待到明天同一时间
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return (calendar.getTimeInMillis() - now) / 1000;
    }
}
