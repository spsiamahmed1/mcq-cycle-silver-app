package com.siam.mcqcycle.helper;

public final class SkillHelper {
    public static String level(int total) {
        if (total >= 5000) return "Diamond";
        if (total >= 3500) return "Platinum";
        if (total >= 2000) return "Gold";
        if (total >= 1000) return "Silver";
        return "Bronze";
    }
    public static int nextMilestone(int total) {
        if (total < 1000) return 1000;
        if (total < 2000) return 2000;
        if (total < 3500) return 3500;
        if (total < 5000) return 5000;
        return 5000;
    }
    public static String remainingLabel(int total) {
        int next = nextMilestone(total);
        return next == 5000 && total >= 5000 ? "সর্বোচ্চ স্তর অর্জিত" : level(next) + " হতে আর বাকি: " + Math.max(0, next-total) + "টা";
    }
    private SkillHelper() {}
}
