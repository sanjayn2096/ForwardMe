package com.example.forwardme

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = application.getSharedPreferences("ForwardingPrefs", Context.MODE_PRIVATE)
        val getNumber = prefs?.getString("forwardNumber", "default")
        val setupIsDone = (getNumber != "default")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        if (navHostFragment == null) {
            Log.e("MainActivity", "NavHostFragment is null. Check the layout ID and ensure it's correctly set.")
        } else {
                val navController = navHostFragment.navController
                if (setupIsDone) {
                    Log.d("MainActivity", "Set up done = $setupIsDone")
                    navController.setGraph(R.navigation.nav_graph, Bundle().apply {
                        putBoolean("setupIsDone", true)
                    })
                } else {
                    navController.setGraph(R.navigation.nav_graph)
                }
                setupActionBarWithNavController(this, navController)
                Log.d("MainActivity", "NavHostFragment found: $navHostFragment")

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        return navHostFragment?.navController?.navigateUp() ?: false
    }
}