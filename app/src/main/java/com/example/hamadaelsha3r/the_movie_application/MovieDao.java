package com.example.hamadaelsha3r.the_movie_application;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<movie>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<movie> loadMovie(String id);

    @Insert
    void insertMovie(movie movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(movie movies);

    @Delete
    void deleteMovie(movie movies);
}
