package com.example.movies.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.movies.model.FavouriteMovie
import com.example.movies.model.Movie
import com.example.movies.model.MovieDatabase
import kotlinx.coroutines.launch

class FavouritesViewModel(application: Application) : BaseViewModel(application) {

    var favouriteMoviesLiveData = MutableLiveData<MutableList<FavouriteMovie>>()
    var favouriteMovieDao = MovieDatabase(getApplication()).favouriteMovieDao()

    val movies = MutableLiveData<List<Movie>>()
    val moviesLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    init {
        launch {

            //SEALED CLASS ILE DUZENLEMELER YAP
            (favouriteMovieDao.getAllMovies() as MutableList<FavouriteMovie>?).let { movieList ->
                favouriteMoviesLiveData.value = movieList
            }
        }
    }

    fun fetchFromDatabase() {
        loading.value = true
        moviesRetrieved()

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

            moviesLoadError.value = false
            loading.value = false
        }

    }

    private fun storeFavouriteMovieLocally(favouriteMovie: FavouriteMovie) {

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