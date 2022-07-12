package com.example.editprofileapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.editprofileapplication.User
import com.example.editprofileapplication.data.Users
import java.io.InputStream
import java.io.OutputStream

object UserSerializer: Serializer<User> {
    override val defaultValue: User
        get() = User.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): User {
        return User.parseFrom(input)
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        return t.writeTo(output)
    }
}

private const val DATA_STORE_FILE_NAME="user_info.pb"

val Context.datastore: DataStore<User> by dataStore(
    fileName = "user_info.pb",
    serializer = UserSerializer
)