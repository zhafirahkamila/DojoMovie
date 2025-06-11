package id.ac.binus.dojomovie

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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

class SignUpPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup_page)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        val txtConfirmPass = findViewById<TextView>(R.id.txtConfirmPass)
        val txtPhone = findViewById<TextView>(R.id.txtPhone)
        val txtPassword = findViewById<TextView>(R.id.txtPassword)
        val btnSignUp = findViewById<AppCompatButton>(R.id.btnSignUp)

        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val edtConfirmPass = findViewById<EditText>(R.id.edtConfirmPass)
        val txtLogin = findViewById<TextView>(R.id.txtHaveAcc)

        txtLogin.movementMethod = LinkMovementMethod.getInstance()
        txtLogin.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        setColoredText(txtConfirmPass, "Confirm Password*")
        setColoredText(txtPhone, "Phone Number*")
        setColoredText(txtPassword, "Password*")

        // Handle Sign Up
        btnSignUp.setOnClickListener {
            val phone = edtPhone.text.toString().trim()
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirmPass.text.toString()

            // Sync data before validation
            DB.syncData(this)

            // Validate fields are not empty
            if (phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate password length
            if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate password match
            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate phone number is unique
            val existingUser = DB.userList.find { it.phone == phone }
            if (existingUser != null) {
                Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // All validations passed, show OTP notification
            val otpCode = (1000..9999).random().toString()

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
            val channelId = "otp_channel"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = android.app.NotificationChannel(
                    channelId,
                    "OTP Notifications",
                    android.app.NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }

            val notification = android.app.Notification.Builder(this, channelId)
                .setContentTitle("Your OTP Code")
                .setContentText("Your OTP is: $otpCode")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build()

            notificationManager.notify(1, notification)

            // Move to OTP page
            val intent = Intent(this, OtpPage::class.java)
            intent.putExtra("phone", phone)
            intent.putExtra("password", password)
            intent.putExtra("otp", otpCode)
            startActivity(intent)
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

