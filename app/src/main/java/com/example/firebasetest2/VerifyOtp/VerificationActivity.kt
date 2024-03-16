package com.example.firebasetest2.VerifyOtp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasetest2.MainActivity
import com.example.firebasetest2.R
import com.example.firebasetest2.UserProfile.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class VerificationActivity : AppCompatActivity() {
    private lateinit var verifyOtp: AppCompatButton
    private lateinit var enterOtp: AppCompatEditText

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var tokenId: PhoneAuthProvider.ForceResendingToken

    private lateinit var otp: String
    private lateinit var userNumber: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        enterOtp = findViewById(R.id.enter_otp)
        verifyOtp = findViewById(R.id.verify_otp)

        firebaseAuth = FirebaseAuth.getInstance()

        otp = intent.getStringExtra("otp").toString()
        userNumber = intent.getStringExtra("number").toString()

        tokenId = ((intent.getParcelableExtra("tokenId") as? PhoneAuthProvider.ForceResendingToken)!!)

        verifyOtp.setOnClickListener {
            verification()
        }
    }

    private fun verification() {
        val verifyOtp = enterOtp.text.toString().trim()
        val userCredential = PhoneAuthProvider.getCredential(otp, verifyOtp)

        firebaseAuth.signInWithCredential(userCredential).addOnSuccessListener {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

            FirebaseFirestore.getInstance().collection("student").whereEqualTo("number",FirebaseAuth.getInstance().currentUser?.phoneNumber).get()
                .addOnSuccessListener {
                    if (it.documents.isEmpty()){
                        startActivity(Intent(this,UserProfile::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                }

            startActivity(Intent(this, UserProfile::class.java))


            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}