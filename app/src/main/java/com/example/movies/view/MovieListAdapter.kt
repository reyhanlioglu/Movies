package com.example.movies.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.model.Movie
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieListAdapter :
    ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieAsync()),
    MovieClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //  val view = inflater.inflate(R.layout.item_movie, parent, false)
        val view = DataBindingUtil.inflate<ItemMovieBinding>(inflater, R.layout.item_movie, parent, false)

        // ERROR
        //notifyItemRangeInserted(0, movieList.size-1)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.movie = getItem(position)
        holder.view.listener = this  //this means MovieClickListener

    }

    override fun onMovieClicked(v: View) {
        val uuid = v.movieId.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.movieUuid = uuid     //put int into bundle and get it from detail fragment
        Navigation.findNavController(v).navigate(action)
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

}