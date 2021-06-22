package com.example.medialibrary

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import layout.Book

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display()

        val fab = findViewById<FloatingActionButton>(R.id.fabAddMedia)
        fab.setOnClickListener { press() }

        val dbHelper: DatabaseHelper = DatabaseHelper(this)

        val book = Book("Skyward", "Brandon Sanderson", mutableListOf("Science Fiction"), "Skyward", 2018)

//        val success = dbHelper.insertIntoBook(book)

//        Toast.makeText(this, "Success = $success", Toast.LENGTH_LONG).show()

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

    @RequiresApi(Build.VERSION_CODES.P)
    protected override fun onResume() {
        super.onResume()
        // Display all the books again once we come back to the activity
        display()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun display() {
        val db = DatabaseHelper(this)
        val books = db.getAllBooks()

        val arrayAdapter = ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, books)
        val lv = findViewById<ListView>(R.id.lvMedia)
        lv.adapter = arrayAdapter
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