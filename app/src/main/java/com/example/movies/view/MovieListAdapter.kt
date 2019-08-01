package com.example.movies.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieListAdapter(val movieList: ArrayList<Movie>) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(), MovieClickListener {

    // private val BASE_URL = "https://image.tmdb.org/t/p/w500"

    fun updateMovieList(newMovieList: List<Movie>){
        movieList.clear()
        movieList.addAll(newMovieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
      //  val view = inflater.inflate(R.layout.item_movie, parent, false)
        val view = DataBindingUtil.inflate<ItemMovieBinding>(inflater, R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.movie = movieList[position]
        holder.view.listener = this  //this means MovieClickListener

        //Old codes
 /*         holder.view.name.text = movieList[position].movieName
            holder.view.raiting.text = "Raiting: "+movieList[position].voteAverage
            holder.view.setOnClickListener{
            val action = ListFragmentDirections.actionDetailFragment()
            action.movieUuid = movieList[position].uuid
            Navigation.findNavController(it).navigate(action)
        }

        holder.view.imageView.loadImage(BASE_URL + movieList[position].imagePath, getProgressDrawable(holder.view.imageView.context))
    */
    }

    override fun onMovieClicked(v: View) {
        val uuid = v.movieId.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.movieUuid = uuid     //put int into bundle and get it from detail fragment
        Navigation.findNavController(v).navigate(action)
    }

    class MovieViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root)  //Binding class created automatically because we wrote <layout> tag at the top of xml file
}