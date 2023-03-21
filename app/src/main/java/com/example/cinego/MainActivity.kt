package com.example.cinego

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.cinego.databinding.ActivityMainBinding
import okhttp3.Headers
import org.json.JSONException


private const val TAG = "MainActivity"
private const val API_KEY = BuildConfig.API_KEY
private const val NOW_PLAYING_URL =
    "https://api.themoviedb.org/3/movie/now_playing?api_key=$API_KEY"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val movieAdapter = MovieAdapter(this, movies)

        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context)
        }

        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?,
            ) {
                Log.e(TAG, "onFailure Status Code: $statusCode, Error: $throwable")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))
                    movieAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })
    }

}