package com.siam.mcqcycle.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import com.siam.mcqcycle.MainActivity;
import com.siam.mcqcycle.helper.TimerHelper;

public class TimerService extends Service {
    private static final String CHANNEL_ID = "mcq-cycle-timer";
    private Handler handler;
    private Runnable runnable;
    @Override public void onCreate() {
        super.onCreate();
        NotificationManager nm=getSystemService(NotificationManager.class);
        if(nm!=null)nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"MCQ Timer",NotificationManager.IMPORTANCE_LOW));
        handler=new Handler(Looper.getMainLooper());
        runnable=new Runnable(){@Override public void run(){
            if(!TimerHelper.running(TimerService.this)){stopSelf();return;}
            long left=TimerHelper.remaining(TimerService.this);
            if(left<=0){TimerHelper.stop(TimerService.this);Intent done=new Intent(TimerService.this,com.siam.mcqcycle.receiver.CycleAlarmReceiver.class);done.setAction("MCQ_CYCLE_END");sendBroadcast(done);
                if(TimerHelper.running(TimerService.this))handler.postDelayed(this,1000);else stopSelf();return;}
            updateNotification(left);handler.postDelayed(this,1000);
        }};
    }
    @Override public int onStartCommand(Intent intent,int flags,int startId){
        startForeground(1,notification("Timer active"));handler.removeCallbacks(runnable);handler.post(runnable);return START_STICKY;
    }
    @Override public void onDestroy(){if(handler!=null)handler.removeCallbacks(runnable);super.onDestroy();}
    @Override public IBinder onBind(Intent intent){return null;}
    private android.app.Notification notification(String text){
        PendingIntent pi=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(this,CHANNEL_ID).setContentTitle("MCQ Cycle").setContentText(text)
                .setSmallIcon(android.R.drawable.ic_dialog_info).setContentIntent(pi).setOngoing(true).build();
    }
    private void updateNotification(long ms){NotificationManager nm=getSystemService(NotificationManager.class);if(nm!=null)nm.notify(1,notification(String.format(java.util.Locale.getDefault(),"বাকি: %02d:%02d",ms/60000,(ms%60000)/1000)));}
}
