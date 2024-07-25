package com.example.forwardme.Data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ForwardingViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("ForwardingPrefs", Context.MODE_PRIVATE)
    private val _forwardNumber = MutableLiveData<String>()

    val forwardNumber: LiveData<String>
        get() = _forwardNumber

    init {
        _forwardNumber.value = prefs.getString("forwardNumber", null)
    }

    fun saveForwardNumber(number: String) {
        prefs.edit().putString("forwardNumber", number).apply()
        _forwardNumber.value = number
    }
}
