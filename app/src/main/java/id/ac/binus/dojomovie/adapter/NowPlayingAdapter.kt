package id.ac.binus.dojomovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.binus.dojomovie.R
import id.ac.binus.dojomovie.model.NowPlaying


class NowPlayingAdapter(
    private val filmList: List<NowPlaying>,
    private val onItemClick: (NowPlaying) -> Unit
) : RecyclerView.Adapter<NowPlayingAdapter.MovieViewHolder>() {


    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.imgPoster)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtDescription: TextView = view.findViewById(R.id.txtDescription)
        val txtGenre: TextView = view.findViewById(R.id.txtGenre)
        val txtDuration: TextView = view.findViewById(R.id.txtDuration)
        val txtYear: TextView = view.findViewById(R.id.txtYear)
        val txtRating: TextView = view.findViewById(R.id.txtRating)
        val btnShowDetail: Button = view.findViewById(R.id.btnShowDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount() = filmList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val film = filmList[position]

        val context = holder.itemView.context
        val imageResId = when (film.title.lowercase()) {
            "kongzilla" -> R.drawable.kongzilla
            "final fantalion" -> R.drawable.final_fantalion
            "bond jampshoot" -> R.drawable.bond_jampshoot
            else -> R.drawable.film1
        }

        Glide.with(context)
            .load(imageResId)
            .into(holder.imgPoster)

        holder.txtTitle.text = film.title
        holder.txtTitle.visibility = View.VISIBLE
        holder.txtPrice.text = "Rp. ${film.price}"
        holder.txtPrice.visibility = View.VISIBLE
        holder.txtGenre.text = film.genre
        holder.txtDescription.text = film.description
        holder.txtDuration.text = film.duration
        holder.txtYear.text = film.year.toString()
        holder.txtRating.text = film.rating.toString()
        holder.btnShowDetail.setOnClickListener{
            onItemClick(film)
        }

//        holder.itemView.setOnClickListener { onItemClick(film) }

    }
}