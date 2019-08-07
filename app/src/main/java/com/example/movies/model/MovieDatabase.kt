package com.example.movies.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Movie::class, FavouriteMovie::class), version = 1)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun favouriteMovieDao(): FavouriteMovieDao

    companion object {
        @Volatile private var instance: MovieDatabase? = null
        private val LOCK = Any()

        //Singleton approach used
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) { //Semaphore is used to lock for block multiple time access
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,  //Application context should be used because rotation operations should be considered
            MovieDatabase::class.java,
    "moviedatabase"
        ).build()
    }
}