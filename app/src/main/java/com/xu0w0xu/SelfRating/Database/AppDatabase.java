package com.xu0w0xu.SelfRating.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xu0w0xu.SelfRating.Models.Score;
import com.xu0w0xu.SelfRating.Database.Dao.ScoreDao;

@Database(entities = {Score.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();
}
