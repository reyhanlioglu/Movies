package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.movies.model.FavouriteMovie
import com.example.movies.model.MovieDatabase
import kotlinx.coroutines.launch

class FavouritesViewModel(application: Application) : BaseViewModel(application) {

    var favouriteMoviesLiveData = MutableLiveData<MutableList<FavouriteMovie>>()
    var favouriteMovieDao = MovieDatabase(getApplication()).favouriteMovieDao()

    init {
        launch {
            //  println("DELETE UUID: "+movie.uuid)
            // println("REMOVE MOVIE WITH ID: "+movie.movieId)
            //SEALED CLASS ILE DUZENLEMELER YAP
            (favouriteMovieDao.getAllMovies() as MutableList<FavouriteMovie>?).let { movieList ->
                favouriteMoviesLiveData.value = movieList
            }
        }
    }

    fun addMovieToFavourites(movie: FavouriteMovie) {
        storeFavouriteMovieLocally(movie)
    }

    fun removeMovieFromFavourites(movie: FavouriteMovie) {
        if (checkWhetherMovieExist(movie)) {
            favouriteMoviesLiveData.value?.remove(movie)
            launch {
                favouriteMovieDao.deleteMovieWithId(movie.movieId!!.toInt())
            }
            println("Total size is " + favouriteMoviesLiveData.value?.size)
        }

    }


    fun moviesRetrieved() {
        launch {
            favouriteMoviesLiveData.value =
                MovieDatabase(getApplication()).favouriteMovieDao().getAllMovies() as ArrayList<FavouriteMovie>?
        }

    }

    private fun storeFavouriteMovieLocally(favouriteMovie: FavouriteMovie) {
        //Background operations should be in coroutine
        launch {

            if (!checkWhetherMovieExist(favouriteMovie)) {

                val result = favouriteMovieDao.insertAll(favouriteMovie)
                favouriteMovie.uuid = result[0].toInt()
                println("ID is " + favouriteMovie.movieId)
                favouriteMoviesLiveData.value?.add(favouriteMovie)

            } else
                println("Movie is already in database")

        }
    }

    fun checkWhetherMovieExist(movie: FavouriteMovie): Boolean {
        // moviesRetrieved()
        return if (favouriteMoviesLiveData.value != null) {
            favouriteMoviesLiveData.value!!.contains(movie)
        } else
            false
    }

    override fun onCleared() {
        super.onCleared()
        //   disposable.clear()
    }

}