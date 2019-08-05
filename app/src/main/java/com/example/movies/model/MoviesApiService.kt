package com.example.movies.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MoviesApiService {
    private val BASE_URL = "https://api.themoviedb.org/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MoviesApi::class.java)

    fun getMovies(): Single<Response> {
        return api.getMovies()
    }

    fun getTopRatedMovies(): Single<Response> {
        return api.getTopRatedMovies()
    }

}