package com.example.firebasetest2.UserLogin

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasetest2.R
import com.example.firebasetest2.VerifyOtp.VerificationActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class UserLoginActivity : AppCompatActivity() {
    private var verificationID = ""
    private lateinit var enterUserPhone: AppCompatEditText
    private lateinit var getOtp:AppCompatButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        verificationID = intent.putExtra("otp", "").toString()

        enterUserPhone = findViewById(R.id.enter_userPhoneNumber)
        getOtp = findViewById(R.id.get_userOtp)

        getOtp.setOnClickListener {
            sendOtp()
        }
    }

    private fun sendOtp(){
        val number = ""
        if (number.isEmpty()){
            val phoneOtp = PhoneAuthOptions.newBuilder()
                .setActivity(this)
                .setTimeout(50L, TimeUnit.SECONDS)
                .setPhoneNumber("+91${enterUserPhone.text.toString()}")
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        Toast.makeText(
                            this@UserLoginActivity,
                            "verification complete",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Toast.makeText(
                            this@UserLoginActivity,
                            "verification failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(verificationId, p1)
                        verificationID = verificationId

                        val intent = Intent(this@UserLoginActivity, VerificationActivity::class.java)
                        intent.putExtra("otp", verificationID)
                        intent.putExtra("tokenId", p1)
                        intent.putExtra("number", number)
                        Toast.makeText(this@UserLoginActivity, "Otp sent", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    }
                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(phoneOtp)
        }
    }
}