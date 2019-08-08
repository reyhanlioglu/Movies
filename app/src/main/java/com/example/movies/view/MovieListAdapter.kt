package com.example.movies.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.model.FavouriteMovie
import com.example.movies.model.Movie
import com.example.movies.viewmodel.FavouritesViewModel
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder
import kotlinx.android.synthetic.main.item_movie.*
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieListAdapter(var fragment: Fragment) :
    ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieAsync()),
    MovieClickListener, FavouriteButtonListener {

    private var favouritesViewModel: FavouritesViewModel =
        ViewModelProviders.of(fragment).get(FavouritesViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //  val view = inflater.inflate(R.layout.item_movie, parent, false)
        val view = DataBindingUtil.inflate<ItemMovieBinding>(inflater, R.layout.item_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.movie = getItem(position)
        holder.view.listener = this  //this means MovieClickListener
        holder.view.listenerFav = this

        refreshFavouriteIcons(holder, position)
    }

    private fun refreshFavouriteIcons(holder: MovieViewHolder, position: Int) {
        val favouriteMovie = FavouriteMovie(getItem(position))

        favouritesViewModel.checkWhetherMovieExist(favouriteMovie)?.let {
            if (it) {
                holder.view.favouriteButton.setBackgroundResource(R.drawable.heart_red)
            } else {
                holder.view.favouriteButton.setBackgroundResource(R.drawable.heart_black)
            }
        }
    }

    override fun onMovieClicked(v: View) {
        val uuid = v.movieId.text.toString().toInt()
        val id = v.movieId.text.toString().toInt()
        val movieType = v.movieType.text.toString()
        //if (movieType.equals("Popular")) {
        if (fragment is PopularFragment) {
            val action = PopularFragmentDirections.actionDetailFragment()
            action.movieUuid = uuid     //put int into bundle and get it from detail fragment
            action.fromFavourite = false
            Navigation.findNavController(v).navigate(action)
        }
        //else if (movieType.equals("Top Rated")) {
        else if (fragment is TopRatedFragment) {
            val action = TopRatedFragmentDirections.actionDetailFromTopRated()
            action.movieUuid = uuid     //put int into bundle and get it from detail fragment
            action.fromFavourite = false
            Navigation.findNavController(v).navigate(action)
        } else if(fragment is FavouritesFragment ) {
            val action = FavouritesFragmentDirections.actionDetailFromFavourites()
            println("INSIDE : UUID= "+uuid)
            action.movieUuid = uuid     //put int into bundle and get it from detail fragment
            action.fromFavourite = true
            Navigation.findNavController(v).navigate(action)
        }


    }


    class MovieViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root), AnimateViewHolder {

        override fun preAnimateRemoveImpl(holder: RecyclerView.ViewHolder) {
            // do something
        }

        override fun animateRemoveImpl(holder: RecyclerView.ViewHolder, listener: ViewPropertyAnimatorListener) {
            ViewCompat.animate(itemView).apply {
                translationY(-itemView.height * 0.3f)
                alpha(0f)
                duration = 300
                setListener(listener)
            }.start()
        }

        override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
            ViewCompat.setTranslationY(itemView, -itemView.height * 0.3f)
            ViewCompat.setAlpha(itemView, 0f)
        }

        override fun animateAddImpl(holder: RecyclerView.ViewHolder, listener: ViewPropertyAnimatorListener) {
            ViewCompat.animate(itemView).apply {
                translationY(0f)
                alpha(1f)
                duration = 300
                setListener(listener)
            }.start()
        }
    }

    class MovieAsync : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onFavouriteButtonClicked(view: View, movie: Movie) {
        val favouriteMovie = FavouriteMovie(movie)

        println("Current movie is " + movie.movieName)

        favouritesViewModel.checkWhetherMovieExist(favouriteMovie).let {
            if (it) {
                favouritesViewModel.removeMovieFromFavourites(favouriteMovie)
                view.setBackgroundResource(R.drawable.heart_black)
                println("Favorilerden kaldırıldı")
            } else {
                favouritesViewModel.addMovieToFavourites(favouriteMovie)
                view.setBackgroundResource(R.drawable.heart_red)
                println("Favorilere eklendi")
            }
        }

    }

}