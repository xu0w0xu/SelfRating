package com.xu0w0xu.SelfRating;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Score {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int score;
    public String date;
    public int scoreChange;
    public int maxScore;
}





