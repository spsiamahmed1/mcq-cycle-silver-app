package com.siam.mcqcycle.helper;
import android.app.Activity;
import android.view.WindowManager;
public final class PrivacyHelper {
    public static void apply(Activity a){if(CycleHelper.prefs(a).getBoolean("secure_screen",true))a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);else a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);}
    private PrivacyHelper(){}
}
