package com.example.medialibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import layout.Book

class AddBook : AppCompatActivity() {
//    private val bookTitle: String
//    private val author
//    private val series
//    private val genre
//    private val year

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val btn = findViewById<Button>(R.id.btnBookSubmit)
        btn.setOnClickListener { display() }
    }

    private fun display() {
        // Add input validation
        val bookTitle = findViewById<TextView>(R.id.etBookTitle).text.toString()
        val author = findViewById<TextView>(R.id.etBookAuthor).text.toString()
        val series = findViewById<TextView>(R.id.etBookSeries).text.toString()
        val genre = findViewById<TextView>(R.id.etBookGenre).text.toString()
        val year = findViewById<TextView>(R.id.etBookYear).text.toString().toInt()
        val book = Book(bookTitle, author, mutableListOf(genre), series, year)

        val bookContent = findViewById<TextView>(R.id.tvBookContent)
        bookContent.text = book.toString()
    }
}