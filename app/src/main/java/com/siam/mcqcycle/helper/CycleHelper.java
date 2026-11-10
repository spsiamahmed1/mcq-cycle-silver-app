package com.siam.mcqcycle.helper;

import android.content.Context;
import android.content.SharedPreferences;

public final class CycleHelper {
    public static SharedPreferences prefs(Context c) { return c.getSharedPreferences("mcq-cycle", Context.MODE_PRIVATE); }
    public static int cycle(Context c) { return prefs(c).getInt("cycle", 1); }
    public static int index(Context c) { return prefs(c).getInt("index", 0); }
    public static int total(Context c) { return prefs(c).getInt("total", 0); }
    public static void setIndex(Context c, int i) { prefs(c).edit().putInt("index", Math.max(0, Math.min(99, i))).apply(); }
    public static int answered(Context c) { return prefs(c).getInt("answered", 0); }
    public static void setAnswer(Context c, int id, String answer) {
        prefs(c).edit().putString("answer_" + cycle(c) + "_" + id, answer).apply();
    }
    public static String answer(Context c, int id) { return prefs(c).getString("answer_" + cycle(c) + "_" + id, ""); }
    public static void addSolved(Context c, boolean correct) {
        SharedPreferences p = prefs(c);
        p.edit().putInt("total", p.getInt("total",0)+1).putInt("answered", Math.min(100,p.getInt("answered",0)+1))
                .putInt(correct ? "right" : "wrong", p.getInt(correct ? "right" : "wrong",0)+1).apply();
    }
    public static void skip(Context c) { prefs(c).edit().putInt("skipped", prefs(c).getInt("skipped",0)+1).apply(); }
    private CycleHelper() {}
}
