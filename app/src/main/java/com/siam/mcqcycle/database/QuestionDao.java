package com.siam.mcqcycle.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.siam.mcqcycle.model.Question;
import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY id") List<Question> all();
    @Query("SELECT COUNT(*) FROM questions") int count();
    @Insert(onConflict = OnConflictStrategy.REPLACE) void insertAll(List<Question> questions);
}
