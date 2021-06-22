package layout

data class Book(val title:String, val author:String, val genre:MutableList<String>, val series:String, val year: Int) {

    override fun toString(): String {
        return "Title: $title\nAuthor: $author\nGenre: $genre\nSeries: $series\nYear: $year"
    }
}