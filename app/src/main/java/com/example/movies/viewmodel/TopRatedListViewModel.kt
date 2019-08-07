package com.example.movies.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.movies.model.*
import com.example.movies.util.NotificationsHelper
import com.example.movies.util.SharedPreferencesHelper
import com.example.movies.util.setMovieType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class TopRatedListViewModel(application: Application): BaseViewModel(application) {

    private val moviesService = MoviesApiService()
    private val disposable = CompositeDisposable()
    private val prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5*60*1000000000L


    val movies = MutableLiveData<List<Movie>>()
    val moviesLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()


    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getTopMoviesUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }

    }


    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try{
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60 // 5 mins
            refreshTime = cachePreferenceInt.times(1000000000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }


    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            moviesService.getTopRatedMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Response>(){
                    override fun onSuccess(response: Response) {
                        storeMoviesLocally(response.results)
                        Toast.makeText(getApplication(), "Top Rated Movies retreived from endpoint", Toast.LENGTH_SHORT).show()

                        //NOTIFICATION
                        NotificationsHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        moviesLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val movies = MovieDatabase(getApplication()).movieDao().getMoviesWithType("Top Rated") // SHOULD BE EDITED
            moviesRetrieved(movies)
            Toast.makeText(getApplication(), "Top Rated Movies retreived from database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moviesRetrieved(list: List<Movie>) {
        movies.value = list
        moviesLoadError.value = false
        loading.value = false
    }


    private fun storeMoviesLocally(list: List<Movie>) {
        //Background operations should be in coroutine
        launch {
            val dao = MovieDatabase(getApplication()).movieDao()

            //NEW CODES
            dao.deleteMoviesWithType("Top Rated")  // SHOULD BE EDITED
            setMovieType("Top Rated", list)

            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while(i < list.size) {
                list[i].uuid = result[i].toInt()
                i++
            }
            moviesRetrieved(list)
        }
        prefHelper.saveTopMoviesUpdateTime(System.nanoTime()) //Saves current time

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}