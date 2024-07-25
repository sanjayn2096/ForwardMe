package com.example.forwardme

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = application.getSharedPreferences("ForwardingPrefs", Context.MODE_PRIVATE)
        val getNumber = prefs.getString("forwardNumber", "default")
        val isSetupDone = (getNumber != "default")

        val fragment: Fragment = if (isSetupDone) {
            LandingFragment() // Replace with the fragment you want to launch if setup is done
        } else {
            SetupFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}