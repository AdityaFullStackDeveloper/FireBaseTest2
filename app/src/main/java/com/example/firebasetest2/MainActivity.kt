package com.example.firebasetest2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetest2.UserProfile.UserAdapter
import com.example.firebasetest2.UserProfile.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {
    private var id: String? = null
    private var childName: String? = null
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUser()
        userRecyclerView = findViewById(R.id.userRecyclerView)

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getUser() {
        val firebaseAuth = FirebaseFirestore.getInstance()
        val createDataBase = firebaseAuth.collection("student")
            createDataBase.get()
            .addOnSuccessListener { result ->
                val userList = ArrayList<UserModel>()
                for (document in result) {
                    val profile = document.toObject<UserModel>()
                    userList.add(profile)
                }

                userAdapter = UserAdapter(this, userList)
                userRecyclerView.layoutManager = LinearLayoutManager(this)
                userRecyclerView.adapter = userAdapter
                Toast.makeText(this, "User data add successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { i ->
                Toast.makeText(this, "User data add failed:${i.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

