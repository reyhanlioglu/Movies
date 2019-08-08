package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.movies.model.Movie
import com.example.movies.model.MovieDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): BaseViewModel(application) {

    val movieLiveData = MutableLiveData<Movie>()

    fun fetch(id: Int) {
        //launch is similar to doInBackground method on java
        launch {
            val movie = MovieDatabase(getApplication()).movieDao().getMovie(id)
            movieLiveData.value = movie
        }
    }

    fun fetchFromFavourite(id: Int) {
        //launch is similar to doInBackground method on java
        launch {
            val movie = MovieDatabase(getApplication()).favouriteMovieDao().getMovie(id)
            movieLiveData.value = Movie(movie)
        }
    }
}