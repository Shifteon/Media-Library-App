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
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.FileProvider
import androidx.core.view.marginLeft
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createViews()

        val fab = findViewById<FloatingActionButton>(R.id.fabAddMedia)
        fab.setOnClickListener { press() }

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
        createViews()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createViews() {
        val db = DatabaseHelper(this)
        val books = db.getAllBooks() // Get all the books

        val main = findViewById<ConstraintLayout>(R.id.main)
        var id = 1

        for (book in books) {
            val imageView = ImageView(this)
            val cardView = CardView(this)
            cardView.id = id
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            cardView.addView(imageView, 500, 600)
            main.addView(cardView, 500, 600)

            var constraitSet = ConstraintSet()
            constraitSet.clone(main)
            // TODO: 6/30/2021 Make the books line up right even when there is no book to the right of the last book
            if (cardView.id % 2 != 0) {
                constraitSet.connect(cardView.id, ConstraintSet.LEFT, main.id, ConstraintSet.LEFT, 16)
                constraitSet.connect(cardView.id, ConstraintSet.RIGHT, cardView.id + 1, ConstraintSet.LEFT, 16)
            } else {
                constraitSet.connect(cardView.id, ConstraintSet.LEFT, cardView.id - 1, ConstraintSet.RIGHT, 16)
                constraitSet.connect(cardView.id, ConstraintSet.RIGHT, main.id, ConstraintSet.RIGHT, 16)
            }
            if (cardView.id < 3) {
                constraitSet.connect(cardView.id, ConstraintSet.TOP, main.id, ConstraintSet.TOP, 16)
            } else {
                constraitSet.connect(cardView.id, ConstraintSet.TOP, cardView.id - 2, ConstraintSet.BOTTOM, 16)
            }
            constraitSet.applyTo(main)


            // Get the image uri
            val pictureFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),book.image)
            val pictureUri =
                FileProvider.getUriForFile(this, "com.example.medialibrary", pictureFile)
            // Set the image
            Glide.with(this).load(pictureUri).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imageView)
            imageView.setOnClickListener {
                val intent = Intent(this, MediaActivity::class.java)
                intent.putExtra("title", book.toString())
                startActivity(intent)
            }

            id++
        }
    }

    private fun press() {
        val newIntent = Intent(this, AddBook::class.java)
        Toast.makeText(applicationContext, "You pushed the button!", Toast.LENGTH_SHORT).show()
        startActivity(newIntent)
    }
}