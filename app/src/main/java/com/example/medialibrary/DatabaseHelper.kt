package com.example.medialibrary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "media.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        createAuthorTable(db)
        createBookTable(db)
        createGenreTable(db)
//        createBookGenreTable(db)
        createSeriesTable(db)

//        db?.close()
    }

    private fun createAuthorTable(db: SQLiteDatabase?) {
        val createAuthorStatement = "CREATE TABLE IF NOT EXISTS author (\n" +
                "        author_id INTEGER PRIMARY KEY,\n" +
                "        first_name TEXT NOT NULL,\n" +
                "        last_name TEXT NOT NULL\n" +
                ");"
        db?.execSQL(createAuthorStatement)
    }

    private fun createBookTable(db: SQLiteDatabase?) {
        val createBookTableStatement = "CREATE TABLE IF NOT EXISTS book (\n" +
                "       book_id INTEGER PRIMARY KEY,\n" +
                "       title TEXT NOT NULL,\n" +
                "       year INTEGER(4),\n" +
                "       author_id INTEGER,\n" +
                "       series_id INTEGER,\n" +
                "       image TEXT,\n" +
                "       FOREIGN KEY(author_id) REFERENCES author(author_id),\n" +
                "       FOREIGN KEY(series_id) REFERENCES series(series_id)\n" +
                ");"
        db?.execSQL(createBookTableStatement)
    }

    private fun createSeriesTable(db: SQLiteDatabase?) {
        val createSeriesTableStatement = "CREATE TABLE IF NOT EXISTS series (\n" +
                "       series_id INTEGER PRIMARY KEY,\n" +
                "       series_name TEXT NOT NULL\n" +
                ");"
        db?.execSQL(createSeriesTableStatement)
    }

    private fun createGenreTable(db: SQLiteDatabase?) {
        val createGenreTableStatement = "CREATE TABLE IF NOT EXISTS genre (\n" +
                "       genre_id   INTEGER PRIMARY KEY,\n" +
                "       genre_name TEXT NOT NULL\n" +
                ");"
        db?.execSQL(createGenreTableStatement)
    }

    // This function doesn't work for some reason. It crashes everything
    private fun createBookGenreTable(db: SQLiteDatabase?) {
        TODO("Fix this. It doesn't work. It crashes everything")
        val createBookGenreTableStatement = "CREATE TABLE IF NOT EXISTS book_genre (\n" +
                "       genre_id INTEGER NOT NULL,\n" +
                "       book_id  INTEGER NOT NULL,\n" +
                "       FOREIGN KEY(genre_id) REFERENCES genre(genre_id),\n" +
                "       FOREIGN KEY(book_id)  REFERENCES book(book_id),\n" +
                ");"
        db?.execSQL(createBookGenreTableStatement)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    private fun getAuthorId(authorName: MutableList<String>): Int {
        val sql: String = "SELECT author_id FROM author WHERE first_name = '${authorName[0]}' AND last_name = '${authorName[1]}';"
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
//            cursor.close()
//            db.close()
            return id
        } else {
//            cursor.close()
//            db.close()
            return -1
        }
    }

    private fun getSeriesId(series: String): Int {
        val sql = "SELECT series_id FROM series WHERE series_name = \"$series\";"
        val db = this.readableDatabase

        val cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
//            cursor.close()
//            db.close()
            return id
        } else {
//            cursor.close()
//            db.close()
            return -1
        }
    }

    private fun insertIntoAuthor(authorName: MutableList<String>): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("first_name", authorName[0])
        cv.put("last_name", authorName[1])

        val insert = db.insert("author", null, cv)

//        db.close()
        return insert.toInt() != -1
    }

    private fun insertIntoSeries(series: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("series_name", series)

        val insert = db.insert("series", null, cv)

//        db.close()
        return insert.toInt() != -1
    }

    public fun insertIntoBook(book: Book): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        var author_name = book.author.split(' ').toMutableList()
        // Make sure author_name isn't too big
        while (author_name.size > 2) {
            author_name[0] = author_name[0] + "_" + author_name[1]
            author_name.removeAt(1)
        }

        // Get the author_id. Insert it if it does not exist
        var author_id = getAuthorId(author_name)
        if (author_id == -1) {
            insertIntoAuthor(author_name)
            author_id = getAuthorId(author_name)
        }

        // Get the series_id. Insert it if it does not exist
        var series_id = getSeriesId(book.series)
        if (series_id == -1) {
            insertIntoSeries(book.series)
            series_id = getSeriesId(book.series)
        }

        // Populate the content values
        cv.put("title", book.title)
        cv.put("year", book.year)
        cv.put("author_id", author_id)
        cv.put("series_id", series_id)
        cv.put("image", book.image)

        // Insert the book
        val insert = db.insert("book", null, cv)
//        db.close()

        return insert.toInt() != -1
    }

    private fun getSeriesById(id: Int): String {
        val sql = "SELECT series_name FROM series WHERE series_id = $id;"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            val series = cursor.getString(0)
//            cursor.close()
//            db.close()
            return series
        } else {
//            cursor.close()
//            db.close()
            return "None"
        }
    }

    private fun getAuthorById(id: Int): String {
        val sql = "SELECT first_name || ' ' || last_name name FROM author where author_id = $id;"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            val author = cursor.getString(0)
//            cursor.close()
//            db.close()
            return author
        } else {
//            cursor.close()
//            db.close()
            return "None"
        }
    }

    public fun getAllBooks(): MutableList<Book> {
        val sql = "SELECT * FROM book;"
        var books = mutableListOf<Book>()

        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)

        while (cursor.moveToNext()) {
            // Get the info
            val title = cursor.getString(1)
            val year = cursor.getInt(2)
            val author = getAuthorById(cursor.getInt(3))
            val series = getSeriesById(cursor.getInt(4))
            val image = cursor.getString(5)
            val book = Book(title, author, mutableListOf("None"), series, year, image)
            // Add book to list
            books.add(book)
        }
//        cursor.close()
//        db.close()
        // Return list of books
        return books
    }

}