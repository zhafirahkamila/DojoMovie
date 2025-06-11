package id.ac.binus.dojomovie.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.binus.dojomovie.R
import id.ac.binus.dojomovie.model.TransactionModel

class TransactionAdapter(private val transactions: List<TransactionModel>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtTitle)
        val amount: TextView = view.findViewById(R.id.txtAmount)
        val status: TextView = view.findViewById(R.id.txtStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        // Get film title from filmId using DB
        val film = DB.getFilmById(holder.itemView.context, transaction.filmId)
        val titleText = film?.title ?: "Unknown Film"

        holder.title.text = titleText
        holder.amount.text = "${transaction.qty} x Rp ${transaction.totalPrice / transaction.qty}"
        holder.status.text = "Successful"
    }

    override fun getItemCount(): Int = transactions.size
}
