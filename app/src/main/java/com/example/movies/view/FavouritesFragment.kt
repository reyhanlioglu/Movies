package com.example.movies.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.movies.R
import com.example.movies.model.FavouriteMovie
import com.example.movies.model.Movie
import com.example.movies.viewmodel.FavouritesViewModel
import com.example.movies.viewmodel.TopRatedListViewModel
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.android.synthetic.main.fragment_top_rated.*


class FavouritesFragment : Fragment() {

    private lateinit var viewModel: FavouritesViewModel
    private val moviesListAdapter = MovieListAdapter(this)
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // INSPECT (NEW LIBRARY OPERATIONS)
        recyclerView = favouritesList
        recyclerView.itemAnimator = LandingAnimator()


        viewModel = ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
        viewModel.fetchFromDatabase()

        favouritesList.apply {
            layoutManager = LinearLayoutManager(context)
            //  adapter = moviesListAdapter
            adapter = ScaleInAnimationAdapter(moviesListAdapter).apply { setFirstOnly(false) }
        }


        observeViewModel()

    }


    fun observeViewModel() {
        viewModel.favouriteMoviesLiveData.observe(this, Observer { movies ->
            movies?.let {
                favouritesList.visibility = View.VISIBLE

                var arrayList: ArrayList<Movie> = arrayListOf()

                var i=0
                while(i<it.size) {
                    arrayList.add(Movie(it[i]))
                    i++
                }

                moviesListAdapter.submitList( arrayList)
            }
        })

    }



}
