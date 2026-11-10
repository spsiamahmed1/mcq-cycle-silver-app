package com.siam.mcqcycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.siam.mcqcycle.helper.CycleHelper;
import com.siam.mcqcycle.helper.SkillHelper;
import com.siam.mcqcycle.helper.PrivacyHelper;

public class ResultActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state){
        super.onCreate(state);PrivacyHelper.apply(this);setContentView(R.layout.activity_result);
        int cycle=CycleHelper.prefs(this).getInt("last_cycle",0);
        int right=CycleHelper.prefs(this).getInt("last_right",CycleHelper.prefs(this).getInt("right",0));
        int wrong=CycleHelper.prefs(this).getInt("last_wrong",CycleHelper.prefs(this).getInt("wrong",0));
        int skipped=CycleHelper.prefs(this).getInt("last_skipped",CycleHelper.prefs(this).getInt("skipped",0));
        ((TextView)findViewById(R.id.resTitle)).setText(cycle>0?"Cycle "+cycle+" পূর্ণ! 100টা MCQ শেষ!":"সর্বশেষ ফলাফল");
        ((TextView)findViewById(R.id.resRight)).setText("সঠিক: "+right);
        ((TextView)findViewById(R.id.resWrong)).setText("ভুল: "+wrong);
        ((TextView)findViewById(R.id.resSkip)).setText("Skip: "+skipped);
        int total=CycleHelper.total(this);
        TextView celebrate=findViewById(R.id.resCelebration);
        if(total>=1000){celebrate.setVisibility(android.view.View.VISIBLE);celebrate.setText("অভিনন্দন! "+total+" শেষ! তুমি এখন "+SkillHelper.level(total)+"!");
            LinearLayout root=findViewById(R.id.resultRoot);root.addView(new ConfettiView(this),0,new LinearLayout.LayoutParams(-1,150));}
        ((TextView)findViewById(R.id.resSkip)).post(()->addHistory());
        findViewById(R.id.btnNextCycle).setOnClickListener(v->{startActivity(new Intent(this,ExamActivity.class));finish();});
        findViewById(R.id.btnHome).setOnClickListener(v->{startActivity(new Intent(this,MainActivity.class));finish();});
    }
    private void addHistory(){
        String history=CycleHelper.prefs(this).getString("history","");
        LinearLayout root=findViewById(R.id.resultRoot);
        TextView title=new TextView(this);title.setText("ইতিহাস");title.setTextColor(0xff1a1a1a);title.setTextSize(18);title.setTypeface(null,1);title.setPadding(0,24,0,8);root.addView(title);
        if(history.isEmpty()){TextView empty=new TextView(this);empty.setText("এখনও কোনো Cycle সম্পন্ন হয়নি");empty.setTextColor(0xff8a8a8a);root.addView(empty);return;}
        String[] rows=history.split(";");
        for(String row:rows){String[] p=row.split(",");if(p.length<4)continue;TextView item=new TextView(this);item.setText("Cycle "+p[0]+"     সঠিক "+p[1]+"  ·  ভুল "+p[2]+"  ·  Skip "+p[3]);item.setTextColor(0xff1a1a1a);item.setPadding(0,8,0,8);root.addView(item);
            android.widget.ProgressBar bar=new android.widget.ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);bar.setMax(100);bar.setProgress(Math.min(100,Integer.parseInt(p[1])));root.addView(bar,new LinearLayout.LayoutParams(-1,12));}
    }
}
