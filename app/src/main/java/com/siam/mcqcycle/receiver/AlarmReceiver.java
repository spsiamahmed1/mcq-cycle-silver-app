package com.siam.mcqcycle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.siam.mcqcycle.helper.CycleHelper;
import com.siam.mcqcycle.helper.TimerHelper;
import com.siam.mcqcycle.service.TimerService;
import com.siam.mcqcycle.ResultActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (TimerHelper.running(context)) context.startForegroundService(new Intent(context, TimerService.class));
        } else if ("MCQ_CYCLE_END".equals(intent.getAction())) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (v != null) v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            CycleHelper.prefs(context).edit().putInt("cycle", CycleHelper.cycle(context) + 1).putInt("index", 0).putInt("answered", 0).apply();
            Intent act = new Intent(context, ResultActivity.class);
            act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(act);
        }
    }
}
