package com.siam.mcqcycle.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class QuestionSeedWorker extends Worker {
    public QuestionSeedWorker(@NonNull Context context,@NonNull WorkerParameters params){super(context,params);}
    @NonNull @Override public Result doWork(){try{QuestionSeeder.seedIfEmpty(getApplicationContext());return Result.success();}catch(Exception e){return Result.retry();}}
}
