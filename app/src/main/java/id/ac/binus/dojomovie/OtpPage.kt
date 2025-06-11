package id.ac.binus.dojomovie

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import id.ac.binus.dojomovie.adapter.DB

class OtpPage : AppCompatActivity() {

    private lateinit var edtOtp1: EditText
    private lateinit var edtOtp2: EditText
    private lateinit var edtOtp3: EditText
    private lateinit var edtOtp4: EditText
    private lateinit var btnOtp : AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.otp_page)

        edtOtp1 = findViewById(R.id.edtOtp1)
        edtOtp2 = findViewById(R.id.edtOtp2)
        edtOtp3 = findViewById(R.id.edtOtp3)
        edtOtp4 = findViewById(R.id.edtOtp4)
        btnOtp = findViewById(R.id.btnOtp)

        val phone = intent.getStringExtra("phone")
        val password = intent.getStringExtra("password")
        val correctOtp = intent.getStringExtra("otp")

        if (phone.isNullOrEmpty() || password.isNullOrEmpty() || correctOtp.isNullOrEmpty()) {
            Toast.makeText(this, "Missing data from previous screen", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnOtp.setOnClickListener{
            val inputOtp = edtOtp1.text.toString() + edtOtp2.text.toString() +
                    edtOtp3.text.toString() + edtOtp4.text.toString()

            if (inputOtp.length < 4) {
                Toast.makeText(this, "Please fill the full OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (inputOtp == correctOtp) {
                DB.insertNewUser(this, phone ?: "", password ?: "")
                Toast.makeText(this, "OTP Verified! User Registered", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid OTP Code", Toast.LENGTH_SHORT).show()
            }
        }

        edtOtp1.addTextChangedListener { if (edtOtp1.text.length == 1) edtOtp2.requestFocus() }
        edtOtp2.addTextChangedListener { if (edtOtp2.text.length == 1) edtOtp3.requestFocus() }
        edtOtp3.addTextChangedListener { if (edtOtp3.text.length == 1) edtOtp4.requestFocus() }
    }
}