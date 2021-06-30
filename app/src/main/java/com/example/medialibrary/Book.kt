package com.example.medialibrary

data class Book(val title:String, val author:String, val genre:MutableList<String>, val series:String, val year: Int, val image: String) {

    override fun toString(): String {
        return "$title\nAuthor: $author\nGenre: $genre\nSeries: $series\nYear: $year\n$image"
    }
}