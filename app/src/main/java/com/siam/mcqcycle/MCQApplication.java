package com.siam.mcqcycle;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class MCQApplication extends Application {
    @Override public void onCreate(){super.onCreate();boolean dark=getSharedPreferences("mcq-cycle",MODE_PRIVATE).getBoolean("dark_mode",false);
        AppCompatDelegate.setDefaultNightMode(dark?AppCompatDelegate.MODE_NIGHT_YES:AppCompatDelegate.MODE_NIGHT_NO);}
}
