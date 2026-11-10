package com.siam.mcqcycle.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question {
    @PrimaryKey public int id;
    public String subject;
    public String prompt;
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;
    public String correct;
    public String explanation;

    public Question(int id, String subject, String prompt, String optionA, String optionB,
                    String optionC, String optionD, String correct, String explanation) {
        this.id = id; this.subject = subject; this.prompt = prompt;
        this.optionA = optionA; this.optionB = optionB; this.optionC = optionC;
        this.optionD = optionD; this.correct = correct; this.explanation = explanation;
    }
    public String option(int index) {
        switch (index) { case 0: return optionA; case 1: return optionB; case 2: return optionC; default: return optionD; }
    }
}
