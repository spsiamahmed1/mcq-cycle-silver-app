package com.siam.mcqcycle.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.siam.mcqcycle.model.Question;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    public abstract QuestionDao questions();
    public static AppDatabase get(Context context) {
        if (INSTANCE == null) synchronized (AppDatabase.class) {
            if (INSTANCE == null) INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mcq-cycle.db")
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
