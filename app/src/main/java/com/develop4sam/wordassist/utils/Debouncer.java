package com.develop4sam.wordassist.utils;

public class Debouncer {

    private static long last = 0;

    public static boolean shouldProcess(long delay) {
        long now = System.currentTimeMillis();
        if (now - last < delay) return false;
        last = now;
        return true;
    }
}
