package com.example.movies.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteMovieDao {

    @Insert
    suspend fun insertAll(vararg  favouriteMovies: FavouriteMovie): List<Long> //Returns uuids of movies(primary key list)
    // suspend means that run whenever processing power is available for this operation


    // @Insert
    // suspend fun insertTopRatedMovies(vararg  movies: Movie): List<Long>

    @Query("SELECT * FROM favouritemovie")
    suspend fun getAllMovies(): List<FavouriteMovie>


    @Query("SELECT * FROM favouritemovie WHERE uuid = :uuid")
    suspend fun getMovie(uuid: Int): FavouriteMovie


    @Query("DELETE FROM favouritemovie")
    suspend fun deleteAllFavouriteMovies()


    @Query("DELETE FROM favouritemovie WHERE id = :movieId")
    suspend fun deleteMovieWithId(movieId: Int)




}