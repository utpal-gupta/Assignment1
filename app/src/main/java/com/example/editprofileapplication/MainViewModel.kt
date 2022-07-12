package com.example.editprofileapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.editprofileapplication.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor( private val mainRepository: UserRepository) : ViewModel() {
    fun writeToLocal(name:String,phone:String,email:String)= viewModelScope.launch {
        mainRepository.writeToLocal(name,phone,email)
    }

    val readToLocal= mainRepository.readToLocal
}