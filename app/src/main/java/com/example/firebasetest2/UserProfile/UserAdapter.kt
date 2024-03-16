package com.example.firebasetest2.UserProfile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firebasetest2.R
import com.google.firebase.storage.FirebaseStorage

class UserAdapter(private var context: Context, private var userList: List<UserModel>) :
    RecyclerView.Adapter<UserAdapter.UserItemViewHolder>() {

        class UserItemViewHolder(userItem:View): RecyclerView.ViewHolder(userItem){
            val name : TextView = userItem.findViewById(R.id.userName)
            val email : TextView = userItem.findViewById(R.id.userEmail)
            val details : TextView = userItem.findViewById(R.id.userDetails)
            val image: ImageView = userItem.findViewById(R.id.userImageUpload)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val userView = LayoutInflater.from(context).inflate(R.layout.user_list_view, parent, false)
        return UserItemViewHolder(userView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.name.text = userList[position].userName
        holder.email.text = userList[position].userEmail
        holder.details.text = userList[position].userDetails

        val imageStore = FirebaseStorage.getInstance().reference
        imageStore.child(userList[position].child!!)
            .downloadUrl
            .addOnSuccessListener { uri->
                Glide.with(context)
                    .load(uri)
                    .into(holder.image)
                Toast.makeText(context, "Image download url success", Toast.LENGTH_SHORT).show()
            }

            .addOnFailureListener { exception->
                Toast.makeText(
                    context,
                    "Image download url failed:${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}