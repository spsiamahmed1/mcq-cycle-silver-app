package com.siam.mcqcycle.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;
import com.siam.mcqcycle.ResultActivity;
import com.siam.mcqcycle.helper.CycleHelper;
import com.siam.mcqcycle.helper.TimerHelper;

public class CycleAlarmReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context,Intent intent){
        int completed=CycleHelper.cycle(context);
        int right=CycleHelper.prefs(context).getInt("right",0),wrong=CycleHelper.prefs(context).getInt("wrong",0),skipped=CycleHelper.prefs(context).getInt("skipped",0);
        String old=CycleHelper.prefs(context).getString("history","");
        String row=completed+","+right+","+wrong+","+skipped+","+System.currentTimeMillis();
        CycleHelper.prefs(context).edit().putString("history",old.isEmpty()?row:old+";"+row)
                .putInt("last_cycle",completed).putInt("last_right",right).putInt("last_wrong",wrong).putInt("last_skipped",skipped)
                .putInt("cycle",completed+1).putInt("index",0).putInt("answered",0).putInt("right",0).putInt("wrong",0).putInt("skipped",0).apply();
        int total=CycleHelper.total(context),announced=CycleHelper.prefs(context).getInt("last_milestone",0);
        String alert="Cycle "+completed+" পূর্ণ! 100টা MCQ শেষ! Cycle "+(completed+1)+" শুরু হয়েছে।";
        int[] marks={1000,2000,3500,5000};String[] levels={"Silver","Gold","Platinum","Diamond"};
        for(int i=0;i<marks.length;i++)if(total>=marks[i]&&announced<marks[i]){alert="অভিনন্দন! "+marks[i]+"টা শেষ! তুমি এখন "+levels[i]+"!";announced=marks[i];}
        CycleHelper.prefs(context).edit().putInt("last_milestone",announced).apply();
        notifyCompletion(context,completed,alert);
        TimerHelper.start(context);
    }
    private void notifyCompletion(Context c,int completed,String alert){
        boolean sound=CycleHelper.prefs(c).getBoolean("sound_enabled",true),vibration=CycleHelper.prefs(c).getBoolean("vibration_enabled",true);
        Vibrator v=(Vibrator)c.getSystemService(Context.VIBRATOR_SERVICE);
        if(vibration&&v!=null){if(Build.VERSION.SDK_INT>=26)v.vibrate(VibrationEffect.createOneShot(800,VibrationEffect.DEFAULT_AMPLITUDE));else v.vibrate(800);}
        String channel="cycle-alert-"+(sound?"s":"n")+(vibration?"v":"n");NotificationManager nm=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
        if(nm==null)return;
        if(Build.VERSION.SDK_INT>=26){NotificationChannel ch=new NotificationChannel(channel,"Cycle completion",NotificationManager.IMPORTANCE_HIGH);
            ch.enableVibration(vibration);ch.setSound(sound?RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM):null,sound?new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build():null);nm.createNotificationChannel(ch);}
        PendingIntent pi=PendingIntent.getActivity(c,3,new Intent(c,ResultActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        nm.notify(200+completed,new NotificationCompat.Builder(c,channel).setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(alert.startsWith("অভিনন্দন!")?"অভিনন্দন!":("Cycle "+completed+" পূর্ণ!" )).setContentText(alert)
                .setContentIntent(pi).setAutoCancel(true).build());
    }
}
