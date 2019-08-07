package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.movies.model.FavouriteMovie
import com.example.movies.model.Movie
import com.example.movies.model.MovieDatabase
import com.example.movies.util.setMovieType
import kotlinx.coroutines.launch

class FavouritesViewModel (application: Application): BaseViewModel(application) {

    var favouriteMovies = ArrayList<FavouriteMovie>()
    val favouriteMoviesLiveData = MutableLiveData<List<FavouriteMovie>>()


    fun addMovieToFavourites(movie: FavouriteMovie) {
        favouriteMovies.add(movie) //DELETE
        favouriteMoviesLiveData.value = favouriteMovies //DELETE
        storeFavouriteMovieLocally(movie)
    }

    fun removeMovieFromFavourites(movie: FavouriteMovie) {

        if(checkWhetherMovieExist(movie)){
            launch {
                favouriteMovies.remove(movie)
                favouriteMoviesLiveData.value = favouriteMovies
               // println("UUID: "+movie.uuid)
                val dao = MovieDatabase(getApplication()).favouriteMovieDao()
                dao.deleteMovieWithUuid(movie.uuid)
            }
        }
    }



    private fun moviesRetrieved() {
        launch {
            favouriteMovies = MovieDatabase(getApplication()).favouriteMovieDao().getAllMovies() as ArrayList<FavouriteMovie>
            favouriteMoviesLiveData.value = MovieDatabase(getApplication()).favouriteMovieDao().getAllMovies()
        }

    }

    private fun storeFavouriteMovieLocally(favouriteMovie: FavouriteMovie) {
        //Background operations should be in coroutine
        launch {
            val dao = MovieDatabase(getApplication()).favouriteMovieDao()

            val result = dao.insertAll(favouriteMovie)

            favouriteMovie.uuid = result[0].toInt()

            moviesRetrieved()
        }
    }

    fun checkWhetherMovieExist(movie: FavouriteMovie): Boolean {
        moviesRetrieved()
        return favouriteMovies.contains(movie)
    }





    override fun onCleared() {
        super.onCleared()
     //   disposable.clear()
    }

}