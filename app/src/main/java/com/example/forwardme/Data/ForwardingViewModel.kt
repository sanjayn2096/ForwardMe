package com.example.forwardme.Data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ForwardingViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("ForwardingPrefs", Context.MODE_PRIVATE)
    private val _forwardNumber = MutableLiveData<String>()
    private val _fromNumber = MutableLiveData<String>()
    private val _keywords = MutableLiveData<String>()
    private val _criteria = MutableLiveData<String>()
    private val _countryCode = MutableLiveData<String>()

    val forwardNumber: LiveData<String>
        get() = _forwardNumber

    val fromNumber: LiveData<String>
        get() = _fromNumber

    val keywords: LiveData<String>
        get() = _keywords

    val criteria: LiveData<String>
        get() = _criteria

    val countryCode: LiveData<String>
        get() = _countryCode

    init {
        _forwardNumber.value = prefs.getString("forwardNumber", null)
        _fromNumber.value = prefs.getString("fromNumber", null)
        _keywords.value = prefs.getString("keywords", null)
        _criteria.value = prefs.getString("criteria", "AND")
    }

    fun saveForwardNumber(number: String, country: Country) {
        prefs.edit().putString("forwardNumber", number).apply()
        prefs.edit().putString("countryCodeOfForwardingNumber", country.code).apply()
        prefs.edit().putString("countryCode", country.code).apply()
        prefs.edit().putString("countryCodeOfForwardingNumber", country.code).apply()
        _forwardNumber.value = number
    }

    fun saveFilterCriteria(fromNumber: String, keywords: String, criteria: String, countryCode: String) {
        prefs.edit().putString("countryCode", countryCode).apply()
        prefs.edit().putString("fromNumber", fromNumber).apply()
        prefs.edit().putString("keywords", keywords).apply()
        prefs.edit().putString("criteria", criteria).apply()
        _fromNumber.value = fromNumber
        _keywords.value = keywords
        _criteria.value = criteria
        _countryCode.value = countryCode
    }
}
