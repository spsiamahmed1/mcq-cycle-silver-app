package com.siam.mcqcycle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.siam.mcqcycle.database.QuestionSeeder;
import com.siam.mcqcycle.helper.CycleHelper;
import com.siam.mcqcycle.helper.SkillHelper;
import com.siam.mcqcycle.helper.TimerHelper;
import com.siam.mcqcycle.helper.PrivacyHelper;
import com.siam.mcqcycle.service.TimerService;
import com.siam.mcqcycle.database.QuestionSeedWorker;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView timerText, progressText, cycleText, skillStatus;
    private ProgressBar ring;
    private final Runnable refresh = new Runnable() { @Override public void run() { render(); handler.postDelayed(this,1000); } };
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrivacyHelper.apply(this);
        getWindow().setStatusBarColor(android.graphics.Color.WHITE);
        QuestionSeeder.seedIfEmpty(this);
        PeriodicWorkRequest maintenance = new PeriodicWorkRequest.Builder(QuestionSeedWorker.class, 1, TimeUnit.DAYS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("question-bank-maintenance", ExistingPeriodicWorkPolicy.KEEP, maintenance);
        setContentView(R.layout.activity_main);
        timerText=findViewById(R.id.timerText); progressText=findViewById(R.id.progressText); cycleText=findViewById(R.id.cycleText);
        skillStatus=findViewById(R.id.skillStatus); ring=findViewById(R.id.timerRing);
        findViewById(R.id.btnStart).setOnClickListener(v -> start());
        findViewById(R.id.btnPause).setOnClickListener(v -> pause());
        findViewById(R.id.btnOff).setOnClickListener(v -> off());
        addNavigation();
        requestNotificationPermission();
        if (TimerHelper.running(this)) startTimerService();
        render();
    }
    @Override protected void onResume() { super.onResume(); handler.post(refresh); }
    @Override protected void onPause() { handler.removeCallbacks(refresh); super.onPause(); }
    private void start() {
        if (!TimerHelper.running(this)) TimerHelper.start(this);
        startTimerService();
        startActivity(new Intent(this, ExamActivity.class));
    }
    private void pause() { TimerHelper.pause(this); stopService(new Intent(this, TimerService.class)); render(); }
    private void off() {
        TimerHelper.stop(this);
        CycleHelper.prefs(this).edit().putInt("index",0).putInt("answered",0).putInt("right",0).putInt("wrong",0).putInt("skipped",0).apply();
        stopService(new Intent(this, TimerService.class)); render();
        Toast.makeText(this,"Cycle বন্ধ করা হয়েছে",Toast.LENGTH_SHORT).show();
    }
    private void startTimerService() {
        Intent i=new Intent(this, TimerService.class);
        if (Build.VERSION.SDK_INT>=26) ContextCompat.startForegroundService(this,i); else startService(i);
    }
    private void render() {
        if (timerText==null) return;
        long left=TimerHelper.remaining(this); long seconds=left/1000;
        timerText.setText(String.format(java.util.Locale.getDefault(),"%02d:%02d",seconds/60,seconds%60));
        int answered=CycleHelper.answered(this);
        progressText.setText("প্রশ্ন: "+answered+"/100");
        cycleText.setText("Cycle: "+CycleHelper.cycle(this));
        ring.setProgress(answered);
        int total=CycleHelper.total(this);
        skillStatus.setText("মোট MCQ: "+total+"/1000 - "+(total<1000?"Silver হতে আর বাকি: "+(1000-total)+"টা":SkillHelper.remainingLabel(total)));
    }
    private void addNavigation() {
        android.widget.LinearLayout nav=new android.widget.LinearLayout(this);
        nav.setOrientation(android.widget.LinearLayout.HORIZONTAL); nav.setGravity(android.view.Gravity.CENTER);
        nav.setBackgroundColor(android.graphics.Color.WHITE); nav.setPadding(8,8,8,8);
        Button exam=navButton("Exam"), result=navButton("Result"), profile=navButton("Profile");
        nav.addView(exam,new android.widget.LinearLayout.LayoutParams(0,56,1));
        nav.addView(result,new android.widget.LinearLayout.LayoutParams(0,56,1));
        nav.addView(profile,new android.widget.LinearLayout.LayoutParams(0,56,1));
        android.widget.RelativeLayout root=(android.widget.RelativeLayout)findViewById(R.id.topBar).getParent();
        android.widget.RelativeLayout.LayoutParams lp=new android.widget.RelativeLayout.LayoutParams(-1,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM); root.addView(nav,lp);
        exam.setOnClickListener(v->startActivity(new Intent(this,ExamActivity.class)));
        result.setOnClickListener(v->startActivity(new Intent(this,ResultActivity.class)));
        profile.setOnClickListener(v->startActivity(new Intent(this,ProfileActivity.class)));
    }
    private Button navButton(String text) { Button b=new Button(this); b.setText(text); b.setTextColor(0xff8a8a8a); b.setBackgroundColor(android.graphics.Color.TRANSPARENT); b.setAllCaps(false); return b; }
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT>=33 && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.POST_NOTIFICATIONS},90);
    }
}
