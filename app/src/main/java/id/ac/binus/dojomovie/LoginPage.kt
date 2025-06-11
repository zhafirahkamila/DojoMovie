package id.ac.binus.dojomovie

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import id.ac.binus.dojomovie.adapter.DB

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        val txtPhone2 = findViewById<TextView>(R.id.txtPhone2)
        val txtPassword2 = findViewById<TextView>(R.id.txtPassword2)
        val btnLogin = findViewById<AppCompatButton>(R.id.btnLogin)
        val edtPhone2 = findViewById<EditText>(R.id.edtPhone2)
        val edtPassword2 = findViewById<EditText>(R.id.edtPassword2)
        val txtRegister = findViewById<TextView>(R.id.txtHaveAcc)

        txtRegister.movementMethod = LinkMovementMethod.getInstance()
        txtRegister.setOnClickListener {
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
        }


        setColoredText(txtPhone2, "Phone Number*")
        setColoredText(txtPassword2, "Password*")

        // Handle Login
        btnLogin.setOnClickListener {
            val phone = edtPhone2.text.toString()
            val password = edtPassword2.text.toString()

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "Phone number must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Password must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            DB.syncData(this)
            // Check if user exists in DB
            DB.login(this, phone, password)


            if (DB.LOGGED_IN_USER != null) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardPage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }

            // Clear inputs after login
            edtPhone2.text.clear()
            edtPassword2.text.clear()
        }
    }

    fun setColoredText(textView: TextView, text: String) {
        val spannable = SpannableString(text)

        // Warna putih untuk teks utama
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)), 0, text.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Warna merah untuk "*"
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.required)), text.length - 1, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannable
    }
}