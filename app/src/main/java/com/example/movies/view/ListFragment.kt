package com.example.movies.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.viewmodel.ListViewModel
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val moviesListAdapter = MovieListAdapter()
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // INSPECT (NEW LIBRARY OPERATIONS)
        recyclerView = moviesList
        recyclerView.itemAnimator = LandingAnimator()


        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        moviesList.apply {
            layoutManager = LinearLayoutManager(context)
            //  adapter = moviesListAdapter
            adapter = ScaleInAnimationAdapter(moviesListAdapter).apply { setFirstOnly(false) }
        }

        refreshLayout.setOnRefreshListener {
            moviesList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()

    }

    fun observeViewModel() {
        viewModel.movies.observe(this, Observer { movies ->
            movies?.let {
                moviesList.visibility = View.VISIBLE
                moviesListAdapter.submitList(movies)
            }
        })

        viewModel.moviesLoadError.observe(this, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }

        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    moviesList.visibility = View.GONE
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSettings -> {
                view?.let { Navigation.findNavController(it).navigate(ListFragmentDirections.actionSettings()) }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
