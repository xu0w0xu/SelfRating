package com.xu0w0xu.SelfRating;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Score.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();
}
