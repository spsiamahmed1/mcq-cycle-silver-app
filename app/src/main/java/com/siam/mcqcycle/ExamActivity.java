package com.siam.mcqcycle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.siam.mcqcycle.database.AppDatabase;
import com.siam.mcqcycle.helper.CycleHelper;
import com.siam.mcqcycle.helper.TimerHelper;
import com.siam.mcqcycle.helper.PrivacyHelper;
import com.siam.mcqcycle.model.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExamActivity extends AppCompatActivity {
    private final Handler handler=new Handler(Looper.getMainLooper());
    private TextView timer, progress, prompt, explanation;
    private TextView[] options;
    private List<Question> questions;
    private int index;
    private final Runnable tick=new Runnable(){@Override public void run(){ updateTimer(); handler.postDelayed(this,1000); }};
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); PrivacyHelper.apply(this); setContentView(R.layout.activity_exam);
        timer=findViewById(R.id.examTimer); progress=findViewById(R.id.examProgress); prompt=findViewById(R.id.qPrompt);
        options=new TextView[]{findViewById(R.id.optA),findViewById(R.id.optB),findViewById(R.id.optC),findViewById(R.id.optD)};
        explanation=new TextView(this); explanation.setTextColor(0xff8a8a8a); explanation.setTextSize(15); explanation.setPadding(8,20,8,8);
        ((android.widget.LinearLayout)((android.widget.ScrollView)findViewById(R.id.examScroll)).getChildAt(0)).addView(explanation);
        ensureQuestions(); index=CycleHelper.index(this); render();
        findViewById(R.id.btnPrev).setOnClickListener(v->{if(index>0){index--;saveAndRender();}});
        findViewById(R.id.btnSkip).setOnClickListener(v->skip());
        findViewById(R.id.btnNext).setOnClickListener(v->next());
        for(int i=0;i<4;i++){ final int choice=i; options[i].setOnClickListener(v->select(choice)); }
    }
    @Override protected void onResume(){super.onResume();handler.post(tick);}
    @Override protected void onPause(){handler.removeCallbacks(tick);super.onPause();}
    private void ensureQuestions(){
        int cycle=CycleHelper.cycle(this); String key="questions_cycle_"+cycle; String saved=CycleHelper.prefs(this).getString(key,"");
        List<Question> all=AppDatabase.get(this).questions().all();
        if(!saved.isEmpty()) {
            ArrayList<Question> loaded=new ArrayList<>();
            for(String part:saved.split(",")){try{int id=Integer.parseInt(part);for(Question q:all)if(q.id==id){loaded.add(q);break;}}catch(Exception ignored){}}
            if(loaded.size()==100){questions=loaded;return;}
        }
        Set<Integer> used=new HashSet<>(); String usedText=CycleHelper.prefs(this).getString("used_question_ids","");
        if(!usedText.isEmpty())for(String s:usedText.split(","))try{used.add(Integer.parseInt(s));}catch(Exception ignored){}
        Collections.shuffle(all);
        ArrayList<Question> picked=new ArrayList<>();
        for(Question q:all)if(!used.contains(q.id)&&picked.size()<100)picked.add(q);
        boolean wrapped=picked.size()<100;
        if(wrapped){Set<Integer> alreadyPicked=new HashSet<>();for(Question q:picked)alreadyPicked.add(q.id);used.clear();
            for(Question q:all)if(!alreadyPicked.contains(q.id)&&picked.size()<100){picked.add(q);alreadyPicked.add(q.id);}}
        StringBuilder queue=new StringBuilder(); for(Question q:picked){if(queue.length()>0)queue.append(',');queue.append(q.id);used.add(q.id);}
        questions=picked;StringBuilder usedOut=new StringBuilder();for(int id:used){if(usedOut.length()>0)usedOut.append(',');usedOut.append(id);}
        CycleHelper.prefs(this).edit().putString(key,queue.toString()).putString("used_question_ids",usedOut.toString()).apply();
    }
    private Question current(){return questions.get(Math.min(index,questions.size()-1));}
    private void render(){
        if(questions==null||questions.isEmpty())return;
        Question q=current(); prompt.setText(q.subject+"  ·  "+q.prompt); progress.setText((index+1)+"/100");
        for(int i=0;i<4;i++){options[i].setText(""+"ABCD".charAt(i)+". "+q.option(i));options[i].setBackgroundResource(R.drawable.bg_option);options[i].setTextColor(0xff1a1a1a);}
        String saved=CycleHelper.answer(this,q.id);
        if(!saved.isEmpty()){
            int chosen="ABCD".indexOf(saved);
            if(chosen>=0){options[chosen].setBackgroundColor(saved.equals(q.correct)?0xffe7f6eb:0xffffeeee);options[chosen].setTextColor(saved.equals(q.correct)?0xff16833d:0xffb3261e);}
            explanation.setText("সঠিক উত্তর: "+q.correct+". "+q.option("ABCD".indexOf(q.correct))+"\n"+q.explanation);
        }else explanation.setText("");
        updateTimer();
    }
    private void select(int selected){
        Question q=current();String prior=CycleHelper.answer(this,q.id);String value=""+"ABCD".charAt(selected);
        if(prior.isEmpty()) {
            String skipKey="skipped_ids_"+CycleHelper.cycle(this);String skipped=CycleHelper.prefs(this).getString(skipKey,"");
            if(contains(skipped,q.id)){CycleHelper.prefs(this).edit().putInt("skipped",Math.max(0,CycleHelper.prefs(this).getInt("skipped",0)-1)).putString(skipKey,removeId(skipped,q.id)).apply();}
            CycleHelper.addSolved(this,value.equals(q.correct));
        }
        else if(!prior.equals(value)){
            boolean was=prior.equals(q.correct), now=value.equals(q.correct);
            if(was!=now){int r=CycleHelper.prefs(this).getInt("right",0),w=CycleHelper.prefs(this).getInt("wrong",0);
                CycleHelper.prefs(this).edit().putInt("right",Math.max(0,r+(now?1:-1))).putInt("wrong",Math.max(0,w+(now?-1:1))).apply();}
        }
        CycleHelper.setAnswer(this,q.id,value);render();
    }
    private void skip(){
        Question q=current();String key="skipped_ids_"+CycleHelper.cycle(this);String list=CycleHelper.prefs(this).getString(key,"");
        if(CycleHelper.answer(this,q.id).isEmpty()&&!contains(list,q.id)){
            CycleHelper.skip(this);CycleHelper.prefs(this).edit().putString(key,list.isEmpty()?""+q.id:list+","+q.id).apply();
        }
        next();
    }
    private boolean contains(String ids,int id){for(String s:ids.split(","))try{if(Integer.parseInt(s)==id)return true;}catch(Exception ignored){}return false;}
    private String removeId(String ids,int id){StringBuilder out=new StringBuilder();for(String s:ids.split(","))try{if(Integer.parseInt(s)!=id){if(out.length()>0)out.append(',');out.append(s);}}catch(Exception ignored){}return out.toString();}
    private void next(){if(index<99){index++;saveAndRender();}else startActivity(new Intent(this,ResultActivity.class));}
    private void saveAndRender(){CycleHelper.setIndex(this,index);render();}
    private void updateTimer(){long ms=TimerHelper.remaining(this);long s=ms/1000;timer.setText(String.format(java.util.Locale.getDefault(),"%02d:%02d",s/60,s%60));}
}
