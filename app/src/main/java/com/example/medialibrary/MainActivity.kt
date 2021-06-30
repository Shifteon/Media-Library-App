package com.example.medialibrary

import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display()

//        createViews()

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

    @RequiresApi(Build.VERSION_CODES.P)
    protected override fun onResume() {
        super.onResume()
        // Display all the books again once we come back to the activity
        display()
//        createViews()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun display() {
        val db = DatabaseHelper(this)
        val books = db.getAllBooks() // Get all the books
        var titles = mutableListOf<String>()
        for (book in books) {
            titles.add(book.title)
        }

        // Create array adapter and display all the books
        val arrayAdapter = ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, books)
//        val arrayAdapter = ArrayAdapter<Book>(this, android.R.layout.simple_gallery_item, books)
        val lv = findViewById<ListView>(R.id.lvMedia)
        lv.adapter = arrayAdapter

        // Set a click listener on all list items
        lv.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position)
            val intent = Intent(this, MediaActivity::class.java)
            intent.putExtra("title", element.toString())
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createViews() {
        val db = DatabaseHelper(this)
        val books = db.getAllBooks() // Get all the books

        val linearLayout = findViewById<LinearLayout>(R.id.llBooks)

        for (book in books) {
            val imageView = ImageView(this)
//            imageView.adjustViewBounds
            val layoutParams = imageView.layoutParams
//            layoutParams.width = 200
            // Get the image uri
            val pictureFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),book.image)
            val pictureUri =
                FileProvider.getUriForFile(this, "com.example.medialibrary", pictureFile)
            // Set the image
            Glide.with(this).load(pictureUri).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imageView)

            linearLayout.addView(imageView)
        }
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