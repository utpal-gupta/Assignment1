package com.example.editprofileapplication.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.editprofileapplication.data.Users
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun writeToLocal(name:String,phone:String,email:String)= context.datastore.updateData { user->
        user.toBuilder()
            .setName(name)
            .setPhone(phone)
            .setEmail(email)
            .build()
    }
    val readToLocal: Flow<Users> = context.datastore.data.catch{
            if(this is Exception){
                Log.d("main","${this.message}")
            }
        }.map {
            val users = Users(it.name, it.phone,it.email)
             users

        }
}