package com.example.forwardme.utils

import com.example.forwardme.Data.Country
import com.example.forwardme.R

class Utils {

    fun getCountries(): List<Country> {
        return listOf(
            Country("United States", "+1", R.drawable.flag_us),
            Country("India", "+91", R.drawable.flag_in),
            Country("United Kingdom", "+44", R.drawable.flag_uk)
        )
    }
}