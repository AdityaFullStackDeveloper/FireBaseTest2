package com.example.firebasetest2.UserProfile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebasetest2.MainActivity
import com.example.firebasetest2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UserProfile : AppCompatActivity() {
    private lateinit var chooseImage: AppCompatButton
    private lateinit var userImageView: ImageView
    private var uri: Uri? = null

    private lateinit var enterYourName: AppCompatEditText
    private lateinit var enterYourEmail: AppCompatEditText
    private lateinit var enterYourDetails: AppCompatEditText
    private lateinit var saveData: AppCompatButton

    private lateinit var fireBaseStorage: FirebaseStorage
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val childName = "${UUID.randomUUID()}"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        userImageView = findViewById(R.id.userProfileImage)
        chooseImage = findViewById(R.id.chooseImage)

        fireBaseStorage = FirebaseStorage.getInstance()

        enterYourName = findViewById(R.id.enter_yourName)
        enterYourEmail = findViewById(R.id.enter_yourEmail)
        enterYourDetails = findViewById(R.id.enter_yourDetails)
        saveData = findViewById(R.id.user_saveData)

        saveData.setOnClickListener {
            setUser()
        }

        chooseImage.setOnClickListener {
            chooseImageGallery()
        }
    }

    private fun chooseImageGallery() {
        val gallery = Intent()
        gallery.type = "image/*"
        startActivityForResult(
            Intent.createChooser(gallery, "Select photo"), 200
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            try {
                uri = imageUri
                userImageView.setImageURI(imageUri)
                imageUpload()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setUser() {
        uri?.let { imageUri ->
            val dataSet = FirebaseStorage.getInstance().reference
                .child("profileImageSet")
                .child(UUID.randomUUID().toString())

            dataSet.putFile(imageUri).addOnSuccessListener { task ->
                dataSet.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val id = UUID.randomUUID().toString()
                    val imageUrl = downloadUrl.toString()
                    val number = FirebaseAuth.getInstance().currentUser?.phoneNumber

                    val userProfile = UserModel(
                        id = id,
                        userName = enterYourName.text.toString(),
                        userEmail = enterYourEmail.text.toString(),
                        userDetails = enterYourDetails.text.toString(),
                        imageUrl = imageUrl,
                        child = childName,
                        number
                    )

                    fireStore.collection("student")
                        .add(userProfile)
                        .addOnSuccessListener {
                            Toast.makeText(this, "User data added successful", Toast.LENGTH_SHORT)
                                .show()

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { it ->
                            Log.e("FireStore", "Error uploading data to Firestore: $it")
                            Toast.makeText(
                                this,
                                "Error uploading data to Firestore: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Image upload failed: ${exception.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }?: run {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }
    private fun imageUpload(){
        if (uri == null){
            Toast.makeText(this, "No image select", Toast.LENGTH_SHORT).show()
        }
        val image = FirebaseStorage.getInstance().reference
            .child(childName)

        val task = image.putFile(uri!!)
        task.addOnSuccessListener {
            Toast.makeText(this, "Image upload success", Toast.LENGTH_SHORT).show()
            return@addOnSuccessListener
        }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }
}