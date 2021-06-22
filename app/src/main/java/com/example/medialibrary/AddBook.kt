package com.example.medialibrary

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import layout.Book
import java.lang.Exception

class AddBook : AppCompatActivity() {
//    private val bookTitle: String
//    private val author
//    private val series
//    private val genre
//    private val year

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val btn = findViewById<Button>(R.id.btnBookSubmit)
        btn.setOnClickListener { createBook() }
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

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createBook() {
        try {
            val db = DatabaseHelper(this)
            // Get book info from inputs
            val bookTitle = findViewById<TextView>(R.id.etBookTitle).text.toString()
            val author = findViewById<TextView>(R.id.etBookAuthor).text.toString()
            val series = findViewById<TextView>(R.id.etBookSeries).text.toString()
            val genre = findViewById<TextView>(R.id.etBookGenre).text.toString()
            val year = findViewById<TextView>(R.id.etBookYear).text.toString().toInt()
            val book = Book(bookTitle, author, mutableListOf(genre), series, year)
            // Insert the book into the database
            db.insertIntoBook(book)

            Toast.makeText(this, "Successfully added book!", Toast.LENGTH_LONG).show()
            // We are done here so end the activity and return to main
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error adding book: $e", Toast.LENGTH_LONG).show()
        }
    }
}