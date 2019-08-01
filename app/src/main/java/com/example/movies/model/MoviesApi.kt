package com.example.movies.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("3/movie/popular?api_key=75bf214782603826f42acbeae4d445c8&language=en-US&page=1")
    fun getMovies() : Single<Response>
    //(@Query("search") search: String)

}