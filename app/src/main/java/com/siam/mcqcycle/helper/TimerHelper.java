package com.siam.mcqcycle.helper;

import android.content.Context;
import android.content.SharedPreferences;

public final class TimerHelper {
    public static final long CYCLE_MS = 20L * 60L * 1000L;
    private static SharedPreferences p(Context c) { return c.getSharedPreferences("mcq-cycle", Context.MODE_PRIVATE); }
    public static boolean running(Context c) { return p(c).getBoolean("running", false); }
    public static long remaining(Context c) {
        SharedPreferences s = p(c);
        return running(c) ? Math.max(0, s.getLong("endAt", 0) - System.currentTimeMillis()) : s.getLong("remaining", CYCLE_MS);
    }
    public static void start(Context c) {
        SharedPreferences s = p(c);
        long left = s.getLong("remaining", CYCLE_MS);
        s.edit().putBoolean("running", true).putLong("endAt", System.currentTimeMillis() + (left > 0 ? left : CYCLE_MS)).apply();
    }
    public static void pause(Context c) {
        long left = remaining(c);
        p(c).edit().putBoolean("running", false).putLong("remaining", left).apply();
    }
    public static void stop(Context c) { p(c).edit().putBoolean("running", false).putLong("remaining", CYCLE_MS).putLong("endAt", 0).apply(); }
    private TimerHelper() {}
}
