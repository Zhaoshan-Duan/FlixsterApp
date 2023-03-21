package com.example.cinego

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.cinego.databinding.ActivityDetailBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import okhttp3.Headers

private const val TAG = "DetailActivity"
private const val YOUTUBE_API_KEY = "AIzaSyDwMF8Q6P85k_XsbqMvRM4TlAVFovNgEKI"
private const val TRAILERS_URL =
    "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class DetailActivity : YouTubeBaseActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        // cast as Movie since it's non-nullable
        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie

        with(binding) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            rbVoteAverage.rating = movie.voteAverage.toFloat()
        }

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieID), object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?,
            ) {
                Log.e(TAG, "onFailure $statusCode: $response")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() == 0) {
                    Log.w("Test", "No movies trailers found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")

                // play the with this trailer
                initializeYoutube(youtubeKey)
            }
        })
    }

    private fun initializeYoutube(youtubeKey: String) {
        binding.player.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean,
            ) {
                Log.i("Test", "onInitializationSuccess")
                player?.cueVideo(youtubeKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?,
            ) {
                Log.i("Test", "onInitializationFailure")
            }

        })
    }
}