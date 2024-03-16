package com.example.firebasetest2.Splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasetest2.R
import com.example.firebasetest2.UserLogin.UserLoginActivity
import com.example.firebasetest2.UserProfile.UserProfile
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val isLogin = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed(
            {
                if (isLogin != null) {
                    startActivity(Intent(this, UserProfile::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, UserLoginActivity::class.java))
                }
            }, 3000
        )
    }
}