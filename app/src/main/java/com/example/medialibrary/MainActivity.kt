package com.example.medialibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fabAddMedia)
        fab.setOnClickListener { press() }

        create()

        // Nav stuff
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.main, R.id.profile))
//
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        val appBarConfiguration = AppBarConfiguration(
//            topLevelDestinationIds = setOf(),
//            fallbackOnNavigateUpListener = ::onSupportNavigateUp
//        )
//        findViewById<Toolbar>(R.id.toolbar)
//            .setupWithNavController(navController, appBarConfiguration)
    }

    private fun press() {
        val newIntent = Intent(this, AddBook::class.java)
        Toast.makeText(applicationContext, "You pushed the button!", Toast.LENGTH_SHORT).show()
        startActivity(newIntent)
    }

    private fun create() {
        val view = findViewById<View>(R.id.main)

    }
}