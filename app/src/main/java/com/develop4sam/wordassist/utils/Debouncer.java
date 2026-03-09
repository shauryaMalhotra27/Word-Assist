package com.develop4sam.wordassist.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Debouncer {

    private static long last = 0;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static boolean shouldProcess(long delay) {
        long now = System.currentTimeMillis();
        if (now - last < delay) return false;
        last = now;
        return true;
    }

    public void debounce(Runnable action, long delay) {
        executor.schedule(() -> {
            if (shouldProcess(delay)) {
                action.run();
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
