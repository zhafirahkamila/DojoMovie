package id.ac.binus.dojomovie

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import id.ac.binus.dojomovie.adapter.DB
import java.text.NumberFormat
import java.util.Locale

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.profile_page)

        val btnLogOut = findViewById<AppCompatButton>(R.id.btnLogOut)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)
        val txtBalance = findViewById<TextView>(R.id.txtBalance)
        val ibBack = findViewById<ImageButton>(R.id.ibBack)
        val historyIcon = findViewById<ImageView>(R.id.historyIcon)

        val randomAmount = (10_000..10_000_000).random()
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        var formatted = formatRupiah.format(randomAmount)
        formatted = formatted.replace("IDR", "Rp. ").replace(",00", "").trim()
        txtBalance.text = formatted


        val currentUser = DB.LOGGED_IN_USER

        historyIcon.setOnClickListener {
            val intent = Intent(this, TransactionHistory::class.java)
            startActivity(intent)
        }

        btnLogOut.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { dialog, _ ->
                    val intent = Intent(this, LoginPage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        if (currentUser != null) {
            tvPhone.setText(currentUser.phone)
        } else {
            tvPhone.setText("")
        }

        ibBack.setOnClickListener {
            val intent = Intent(this, DashboardPage::class.java)
            startActivity(intent)
        }
    }
}