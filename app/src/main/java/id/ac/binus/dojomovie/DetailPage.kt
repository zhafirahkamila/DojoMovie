package id.ac.binus.dojomovie

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.ac.binus.dojomovie.adapter.DB
import id.ac.binus.dojomovie.model.TransactionModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)

        val filmId = intent.getStringExtra("FILM_ID") ?: return
        val film = DB.getFilmById(this, filmId) ?: return

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvYear = findViewById<TextView>(R.id.tvMovieYear)
        val tvRating = findViewById<TextView>(R.id.tvRating) // rating dummy
        val tvTime = findViewById<TextView>(R.id.tvTime)
        val tvDesc = findViewById<TextView>(R.id.tvMovieDescription)
        val ivPoster = findViewById<ImageView>(R.id.ivPoster)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvGenre = findViewById<TextView>(R.id.tvGenre)
        val etQty = findViewById<EditText>(R.id.etQty)
        val btnBuy = findViewById<Button>(R.id.btnBuy)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val ivArrow = findViewById<ImageButton>(R.id.ivArrow)

        val basePrice = film.price
        val loggedUser = DB.LOGGED_IN_USER
        var qty = 0

        tvTitle.text = film.title
        tvYear.text = "${film.year ?: "Unknown"} |"
        tvRating.text = "${film.rating} |"
        tvTime.text = film.duration
        tvDesc.text = film.description
        tvPrice.text = "Rp. ${film.price}"
        tvGenre.text = film.genre

        val imageName = when (film.id) {
            "MV001" -> "kongzilla"
            "MV002" -> "final_fantalion"
            "MV003" -> "bond_jampshoot"
            else -> "default_image"
        }

        val imageResId = resources.getIdentifier(imageName, "drawable", packageName)
        if (imageResId != 0) {
            Glide.with(this)
                .load(imageResId)
                .into(ivPoster)
        }

        fun updatePrice() {
            val total = if (qty == 0) basePrice else qty * basePrice
            val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))
            val formattedPrice = "Rp. ${formatter.format(total)}"
            tvPrice.text = formattedPrice
        }

        updatePrice()

        etQty.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                qty = s.toString().toIntOrNull() ?: 0
                updatePrice()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnCancel.setOnClickListener {
            val intent = Intent(this, DashboardPage::class.java)
            startActivity(intent)
        }

        // Button Buy
        btnBuy.setOnClickListener {
            if (qty == 0) {
                Toast.makeText(this, "Please input at least 1 qty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (loggedUser == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totalPrice = qty * basePrice
            // Buat transactionDate sekarang
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            // Buat objek TransactionModel
            val transaction = TransactionModel(
                userId = loggedUser.id,
                filmId = filmId,
                qty = qty,
                totalPrice = totalPrice,
                transactionDate = currentDate
            )

            val success = DB.insertTransaction(this, transaction)

            if (success) {
                AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Transaction successful!")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()

                        // Reset qty, EditText, dan harga ke awal
                        qty = 0
                        etQty.setText("")
                        tvPrice.text = "Rp. ${film.price}"
                    }
                    .show()
            } else {
                Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show()
            }
        }

        ivArrow.setOnClickListener {
            val intent = Intent(this, DashboardPage::class.java)
            startActivity(intent)
        }

    }
}

