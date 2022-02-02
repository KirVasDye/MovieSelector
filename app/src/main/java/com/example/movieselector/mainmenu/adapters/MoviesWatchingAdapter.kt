package com.example.movieselector.mainmenu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieselector.R
import com.example.movieselector.mainmenu.models.Image
import com.example.movieselector.mainmenu.models.MoreMovie
import com.example.movieselector.mainmenu.models.Time
import com.squareup.picasso.Picasso

class MoviesWatchingAdapter(private val movies: List<MoreMovie>) :
    RecyclerView.Adapter<MoviesWatchingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = movies[position].title
        holder.duration.text = Time.intToStringTime(movies[position].runtime)
        holder.rating.text = movies[position].vote_average.toString()
        Picasso.get()
            .load(Image.getImageURL(movies[position].poster_path))
            .placeholder(R.drawable.ic_menu_slideshow)
            .into(holder.poster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.film_name)
        val duration: TextView = itemView.findViewById(R.id.duration)
        val rating: TextView = itemView.findViewById(R.id.film_rate)
        val poster: ImageView = itemView.findViewById(R.id.picture)
    }
}