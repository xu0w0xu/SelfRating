package com.xu0w0xu.SelfRating;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScoreDao {
    @Query("SELECT * FROM Score")
    List<Score> getAll();

    @Query("SELECT * FROM Score WHERE date LIKE :date")
    List<Score> findByDate(String date);

    @Insert
    void insertAll(Score... scores);

    @Update
    void update(Score score);
}
