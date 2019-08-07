package com.example.movies.view

import android.view.View
import com.example.movies.model.Movie

interface FavouriteButtonListener {
    fun onFavouriteButtonClicked(view: View, movie: Movie)
}