package com.example.medialibrary

//import android.support.v4.content.ContextCompat
//import android.support.v7.graphics.Palette

import android.app.DownloadManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File


class MediaActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get rid of the action bar
        try {
            this.getSupportActionBar()?.hide();
        } catch (e: NullPointerException){}
        setContentView(R.layout.activity_media)

        val info = intent.extras?.get("title") as String
        val details = info.split("\n").toMutableList()

        // Get the image uri
        val pictureFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), details[details.size - 1])
        val pictureUri = FileProvider.getUriForFile(this, "com.example.medialibrary", pictureFile)
        // Set the image
        val imageSrc = findViewById<ImageView>(R.id.ivCover)
        Glide.with(this).
        load(pictureUri).
        diskCacheStrategy(DiskCacheStrategy.NONE).
        skipMemoryCache(true).
        into(imageSrc);

        // Create all the text content
        insertText(details)

        // Set the status bar color to a color based on our image
        setStatusBarColor(pictureFile)

        val backBtn = findViewById<ImageButton>(R.id.ibBack)
        backBtn.setOnClickListener { finish() }
    }

    private fun insertText(details: MutableList<String>) {
        val title = findViewById<TextView>(R.id.tvTitle)
        val author = findViewById<TextView>(R.id.tvAuthor)
        val series = findViewById<TextView>(R.id.tvSeries)
        val genre = findViewById<TextView>(R.id.tvGenre)
        val year = findViewById<TextView>(R.id.tvYear)

        title.text = details[0]
        author.text = details[1].replace("_", " ") // Format it nice
        genre.text = details[2]
        series.text = details[3]
        year.text = details[4]
    }

    private fun getDesc() {
        // TODO: 6/30/2021 Access api to populate the description 
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setStatusBarColor(pictureFile: File) {
        val window = this.window
        val image = BitmapFactory.decodeFile(pictureFile.path)
        Palette.from(image).generate(Palette.PaletteAsyncListener {
            val darkMutedSwatch = it?.darkMutedSwatch
            if (darkMutedSwatch!= null) {
                window.statusBarColor = darkMutedSwatch.rgb
            }
        })
    }
}