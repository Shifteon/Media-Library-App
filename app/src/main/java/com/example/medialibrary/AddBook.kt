package com.example.medialibrary

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URI
import java.nio.file.CopyOption
import java.nio.file.Files.copy
import 	java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class AddBook : AppCompatActivity() {
//    private val bookTitle: String
//    private val author
//    private val series
//    private val genre
//    private val year

    private var bookTitle = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val btn = findViewById<Button>(R.id.btnBookSubmit)
        btn.setOnClickListener { createBook() }

        val coverBtn = findViewById<Button>(R.id.btnCover)
        coverBtn.setOnClickListener {
            if (findViewById<TextView>(R.id.etBookTitle).text.toString() != "") {
                openFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path.toUri())
                // We don't want the user to change the name after the file has been created
                findViewById<TextView>(R.id.etBookTitle).isEnabled = false
            }
            else
                Toast.makeText(this, "Please enter a title first", Toast.LENGTH_LONG).show()
        }
    }

    private fun display() {
        // Add input validation
        val bookTitle = findViewById<TextView>(R.id.etBookTitle).text.toString()
        val author = findViewById<TextView>(R.id.etBookAuthor).text.toString()
        val series = findViewById<TextView>(R.id.etBookSeries).text.toString()
        val genre = findViewById<TextView>(R.id.etBookGenre).text.toString()
        val year = findViewById<TextView>(R.id.etBookYear).text.toString().toInt()
        val book = Book(bookTitle, author, mutableListOf(genre), series, year, " ")

        val bookContent = findViewById<TextView>(R.id.tvBookContent)
        bookContent.text = book.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == PICK_IMAGE_FILE
            && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->
                // Perform operations on the document using its URI.
                contentResolver.openFileDescriptor(uri, "r")?.use {
                    bookTitle = findViewById<TextView>(R.id.etBookTitle).text.toString()
                    val image = bookTitle.replace(" ", "_").lowercase() + "_cover.jpg"

                    val inStream = FileInputStream(it.fileDescriptor)
                    val outStream = FileOutputStream(File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), image))
                    val inChannel = inStream.channel
                    val outChannel = outStream.channel
                    inChannel.transferTo(0, inChannel.size(), outChannel)
                    inStream.close()
                    outStream.close()
                }

                Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    // This is the code to make sure onActivityResult is pulling from the right method
    private val PICK_IMAGE_FILE = 2

    fun openFile(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/jpeg"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(intent, PICK_IMAGE_FILE)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createBook() {
        try {
            val db = DatabaseHelper(this)
            // Get book info from inputs. Add input validation
//            val bookTitle = findViewById<TextView>(R.id.etBookTitle).text.toString()
            val author = findViewById<TextView>(R.id.etBookAuthor).text.toString()
            val series = findViewById<TextView>(R.id.etBookSeries).text.toString()
            val genre = findViewById<TextView>(R.id.etBookGenre).text.toString()
            val year = findViewById<TextView>(R.id.etBookYear).text.toString().toInt()
            val image = bookTitle.replace(" ", "_").lowercase() + "_cover.jpg"
            val book = Book(bookTitle, author, mutableListOf(genre), series, year, image)
            // Insert the book into the database
            db.insertIntoBook(book)

            Toast.makeText(this, "Successfully added book!", Toast.LENGTH_LONG).show()
            // We are done here so end the activity and return to main
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error adding book: Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }
}