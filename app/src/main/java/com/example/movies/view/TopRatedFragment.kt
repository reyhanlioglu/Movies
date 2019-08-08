package com.example.movies.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.movies.R
import com.example.movies.viewmodel.ListViewModel
import com.example.movies.viewmodel.TopRatedListViewModel
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_top_rated.*


class TopRatedFragment : Fragment() {

    private lateinit var viewModel: TopRatedListViewModel
    private lateinit var moviesListAdapter: MovieListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesListAdapter = MovieListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_rated, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // INSPECT (NEW LIBRARY OPERATIONS)
        recyclerView = topRatedList
        recyclerView.itemAnimator = LandingAnimator()


        viewModel = ViewModelProviders.of(this).get(TopRatedListViewModel::class.java)
        viewModel.refresh()

        topRatedList.apply {
            layoutManager = LinearLayoutManager(context)
            //  adapter = moviesListAdapter
            adapter = ScaleInAnimationAdapter(moviesListAdapter).apply { setFirstOnly(false) }
        }

        refreshTopRatedLayout.setOnRefreshListener {
            topRatedList.visibility = View.GONE
            listErrorTopRated.visibility = View.GONE
            loadingViewTopRated.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refreshTopRatedLayout.isRefreshing = false
        }

        observeViewModel()

    }

    fun observeViewModel() {
        viewModel.movies.observe(this, Observer { movies ->
            movies?.let {
                topRatedList.visibility = View.VISIBLE
                moviesListAdapter.submitList(movies)
            }
        })

        viewModel.moviesLoadError.observe(this, Observer { isError ->
            isError?.let {
                listErrorTopRated.visibility = if (it) View.VISIBLE else View.GONE
            }

        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingViewTopRated.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listErrorTopRated.visibility = View.GONE
                    topRatedList.visibility = View.GONE
                }
            }

        })
    }


}



