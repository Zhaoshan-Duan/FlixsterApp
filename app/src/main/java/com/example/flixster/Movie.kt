package com.example.flixster

import org.json.JSONArray

// Represent one movie object that displayed in the UI

data class Movie(
    val movieID: Int,
    private val posterPath: String,
    val title: String,
    val overview: String) {

    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"

    // allow method call without instance
    companion object{
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movie> {
            // iterate through the array and return the list of array
            val movies = mutableListOf<Movie>()
            for (i in 0 until movieJsonArray.length()){
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJson.getInt("id"),
                        movieJson.getString("poster_path"),
                        movieJson.getString("title"),
                        movieJson.getString("overview")
                    )
                )
            }


            return movies

        }
    }

}