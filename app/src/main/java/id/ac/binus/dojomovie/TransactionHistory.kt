package id.ac.binus.dojomovie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.binus.dojomovie.adapter.DB
import id.ac.binus.dojomovie.adapter.TransactionAdapter
import id.ac.binus.dojomovie.model.TransactionModel

class TransactionHistory : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val transactionModelList: MutableList<TransactionModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val ibBack = findViewById<ImageButton>(R.id.ibBack)

        ibBack.setOnClickListener {
            val intent = Intent(this, DashboardPage::class.java)
            startActivity(intent)
        }

        val user = DB.LOGGED_IN_USER
        Log.d("TransactionCheck", "Logged in user id: ${user?.id}")

        if (user != null) {
            val film_transaction = DB.getTransactionsByUser(this, user.id)
            Log.d("TransactionCheck", "Transaction count: ${film_transaction.size}")
            recyclerView.adapter = TransactionAdapter(film_transaction)
        }
    }
}