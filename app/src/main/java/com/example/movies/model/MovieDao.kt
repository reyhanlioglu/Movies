package com.example.movies.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * This interface contains Database Access Object (Dao)
 */

@Dao
interface MovieDao {
    @Insert
    suspend fun insertAll(vararg  movies: Movie): List<Long> //Returns uuids of movies(primary key list)
    // suspend means that run whenever processing power is available for this operation

   // @Insert
   // suspend fun insertTopRatedMovies(vararg  movies: Movie): List<Long>

    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movie WHERE type = :type ")
    suspend fun getMoviesWithType(type: String): List<Movie>

    @Query("SELECT * FROM movie WHERE uuid = :movieId")
    suspend fun getMovie(movieId: Int): Movie

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()

    @Query("DELETE FROM movie WHERE type = :type")
    suspend fun deleteMoviesWithType(type: String)






}