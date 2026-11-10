package com.siam.mcqcycle.database;

import android.content.Context;
import com.siam.mcqcycle.model.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Bundled, deterministic question bank. All questions are generated offline at first launch. */
public final class QuestionSeeder {
    private static final Random R = new Random(20261110L);
    private static final List<Question> BANK = new ArrayList<>();
    private static int id = 1;
    public static synchronized void seedIfEmpty(Context context) {
        AppDatabase db = AppDatabase.get(context);
        if (db.questions().count() > 0) return;
        BANK.clear(); id = 1; R.setSeed(20261110L);
        math(); ict(); science(); english(); gk(); sscHsc(); jobExam();
        db.questions().insertAll(BANK);
    }
    private static void math() {
        for (int a=2; a<=31; a++) for (int b=2; b<=21; b++) {
            int ans=a*b;
            numeric("গণিত", a+" × "+b+" = কত?", ans, "গুণের সূত্র: "+a+" বার "+b+" = "+ans);
        }
    }
    private static void ict() {
        for (int n=0; n<100; n++) {
            String bin=Integer.toBinaryString(n);
            choices("ICT", "দশমিক "+n+"-এর বাইনারি মান কোনটি?", bin,
                    new String[]{Integer.toBinaryString(n+1),Integer.toBinaryString(Math.max(0,n-1)),Integer.toBinaryString(n+2)},
                    n+"-কে ২ দিয়ে ভাগ করে ভাগশেষগুলো উল্টো ক্রমে পড়লে "+bin+" হয়।");
        }
    }
    private static void science() {
        String[] symbols={"H","He","Li","Be","B","C","N","O","F","Ne","Na","Mg","Al","Si","P","S","Cl","Ar","K","Ca","Sc","Ti","V","Cr","Mn","Fe","Co","Ni","Cu","Zn","Ga","Ge","As","Se","Br","Kr","Rb","Sr","Y","Zr","Nb","Mo","Tc","Ru","Rh","Pd","Ag","Cd","In","Sn","Sb","Te","I","Xe","Cs","Ba","La","Ce","Pr","Nd","Pm","Sm","Eu","Gd","Tb","Dy","Ho","Er","Tm","Yb","Lu","Hf","Ta","W","Re","Os","Ir","Pt","Au","Hg","Tl","Pb","Bi","Po","At","Rn","Fr","Ra","Ac","Th","Pa","U","Np","Pu","Am","Cm","Bk","Cf","Es","Fm","Md","No","Lr","Rf","Db","Sg","Bh","Hs","Mt","Ds","Rg","Cn","Nh","Fl","Mc","Lv","Ts","Og"};
        for (int i=0;i<symbols.length;i++) {
            String[] d={symbols[(i+1)%symbols.length],symbols[(i+7)%symbols.length],symbols[(i+13)%symbols.length]};
            choices("বিজ্ঞান", "পর্যায় সারণিতে পারমাণবিক সংখ্যা "+(i+1)+"-এর মৌলের প্রতীক কী?", symbols[i], d,
                    symbols[i]+" হলো পারমাণবিক সংখ্যা "+(i+1)+"-এর মৌলের প্রতীক।");
        }
    }
    private static void english() {
        String[][] pairs={
            {"abandon","leave"},{"ability","skill"},{"accurate","correct"},{"ancient","old"},{"assist","help"},{"brave","courageous"},{"brief","short"},{"calm","peaceful"},{"candid","frank"},{"cease","stop"},{"common","usual"},{"conceal","hide"},{"confident","certain"},{"diminish","reduce"},{"eager","keen"},{"enormous","huge"},{"essential","necessary"},{"fragile","delicate"},{"frequent","common"},{"genuine","real"},{"hostile","unfriendly"},{"humble","modest"},{"improve","enhance"},{"include","contain"},{"joyful","happy"},{"liberty","freedom"},{"loyal","faithful"},{"major","important"},{"maintain","preserve"},{"obvious","clear"},{"permit","allow"},{"rapid","fast"},{"reject","refuse"},{"select","choose"},{"silent","quiet"},{"timid","shy"},{"vacant","empty"},{"valuable","precious"},{"wander","roam"},{"wealthy","rich"},{"wise","sensible"},{"accurate","precise"},{"adapt","adjust"},{"admire","respect"},{"afford","manage"},{"alert","watchful"},{"ancient","historic"},{"approach","come near"},{"beneath","under"},{"benefit","advantage"},{"capable","able"},{"complex","complicated"},{"discover","find"},{"difficult","hard"},{"enough","sufficient"},{"fortunate","lucky"},{"generous","giving"},{"honest","truthful"},{"intend","plan"},{"observe","notice"},{"protect","defend"},{"reveal","show"},{"scarce","rare"},{"valuable","worthwhile"}};
        for (int i=0;i<pairs.length;i++) {
            String word=pairs[i][0], ans=pairs[i][1];
            String[] d={pairs[(i+9)%pairs.length][1],pairs[(i+19)%pairs.length][1],pairs[(i+29)%pairs.length][1]};
            choices("English", "Choose the closest meaning of “"+word+"”.", ans, d, "“"+word+"” means “"+ans+"”.");
        }
    }
    private static void gk() {
        String[][] places={
            {"Bangladesh","Dhaka"},{"India","New Delhi"},{"Pakistan","Islamabad"},{"Nepal","Kathmandu"},{"Bhutan","Thimphu"},{"Sri Lanka","Sri Jayawardenepura Kotte"},{"Maldives","Malé"},{"Japan","Tokyo"},{"China","Beijing"},{"South Korea","Seoul"},
            {"Thailand","Bangkok"},{"Malaysia","Kuala Lumpur"},{"Singapore","Singapore"},{"Indonesia","Jakarta"},{"Philippines","Manila"},{"Vietnam","Hanoi"},{"Cambodia","Phnom Penh"},{"Laos","Vientiane"},{"Myanmar","Naypyidaw"},{"Afghanistan","Kabul"},
            {"Iran","Tehran"},{"Iraq","Baghdad"},{"Saudi Arabia","Riyadh"},{"United Arab Emirates","Abu Dhabi"},{"Qatar","Doha"},{"Turkey","Ankara"},{"France","Paris"},{"Germany","Berlin"},{"Italy","Rome"},{"Spain","Madrid"},
            {"Portugal","Lisbon"},{"United Kingdom","London"},{"Ireland","Dublin"},{"Norway","Oslo"},{"Sweden","Stockholm"},{"Finland","Helsinki"},{"Denmark","Copenhagen"},{"Poland","Warsaw"},{"Greece","Athens"},{"Russia","Moscow"},
            {"United States","Washington, D.C."},{"Canada","Ottawa"},{"Mexico","Mexico City"},{"Brazil","Brasília"},{"Argentina","Buenos Aires"},{"Chile","Santiago"},{"Peru","Lima"},{"Egypt","Cairo"},{"Kenya","Nairobi"},{"Australia","Canberra"},
            {"New Zealand","Wellington"},{"South Africa","Pretoria"},{"Nigeria","Abuja"},{"Ghana","Accra"},{"Ethiopia","Addis Ababa"},{"Morocco","Rabat"},{"Algeria","Algiers"},{"Switzerland","Bern"},{"Austria","Vienna"},{"Belgium","Brussels"}
        };
        for (int i=0;i<places.length;i++) choices("সাধারণ জ্ঞান", places[i][0]+"-এর রাজধানী কোনটি?", places[i][1],
                new String[]{places[(i+7)%places.length][1],places[(i+17)%places.length][1],places[(i+27)%places.length][1]}, places[i][0]+"-এর রাজধানী "+places[i][1]+"।");
    }
    private static void sscHsc() {
        for (int a=11;a<=20;a++) for (int b=1;b<=10;b++) numeric("SSC/HSC", a+"² + "+b+" = কত?", a*a+b, "প্রথমে "+a+"² = "+(a*a)+", এরপর "+b+" যোগ করলে "+(a*a+b)+"।");
    }
    private static void jobExam() {
        for (int p : new int[]{5,10,15,20,25}) for (int n=20;n<40;n++) {
            int amount=n*100;
            int ans=p*n;
            numeric("চাকরির পরীক্ষা", amount+"-এর "+p+"% কত?", ans, p+"% of "+amount+" = ("+p+"/100) × "+amount+" = "+ans+"।");
        }
    }
    private static void numeric(String subject, String prompt, int answer, String explanation) {
        choices(subject,prompt,String.valueOf(answer),new String[]{String.valueOf(answer+3),String.valueOf(Math.max(0,answer-2)),String.valueOf(answer+7)},explanation);
    }
    private static void choices(String subject,String prompt,String correct,String[] distractors,String explanation) {
        ArrayList<String> opts=new ArrayList<>(); opts.add(correct);
        for(String s:distractors) if(!opts.contains(s)) opts.add(s);
        int k=0; while(opts.size()<4) { String extra=""+(correct.hashCode()+17*(++k)); if(!opts.contains(extra)) opts.add(extra); }
        Collections.shuffle(opts,R);
        String letter="ABCD".charAt(opts.indexOf(correct))+"";
        BANK.add(new Question(id++,subject,prompt,opts.get(0),opts.get(1),opts.get(2),opts.get(3),letter,explanation));
    }
    private QuestionSeeder() {}
}
