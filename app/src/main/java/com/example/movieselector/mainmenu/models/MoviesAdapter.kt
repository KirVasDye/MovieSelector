package com.example.movieselector.mainmenu.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.movieselector.R

import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.UserListFragment
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.WatchListFragment
import com.squareup.picasso.Picasso

class MoviesAdapter(private val movies: MutableList<MoreMovie>, private val clickListener: ClickListener) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = movies[position]
        holder.textView.text = item.title
        holder.duration.text = Time.intToStringTime(item.runtime)
        holder.rating.text = item.vote_average.toString()
        Picasso.get()
            .load(Image.getImageURL(item.poster_path))
            .placeholder(R.drawable.ic_menu_slideshow)
            .into(holder.poster)
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(item)
        }
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

    interface ClickListener{
        fun onItemClick(movie: MoreMovie)
    }
}