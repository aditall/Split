package com.example.split1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the navigation controller
        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_graph)
        // Set the navigation graph
        navController.setGraph(R.navigation.navigation)
    }
}