package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {
    //https://api.themoviedb.org/3/movie/554993/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US

    String movieId = Integer.toString(Movie.getId());
    String endpointURL = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key" + R.string.movies_api_key;
    public final String MOVIE_TRAILER_URL = endpointURL;
    public static final String TAG = "MovieTrailerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        //temporary test video id to replace with movie trailer video id
        final String videoId = "tKodtNFpzBA";

        //resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        //initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                //log error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        final String finalVideoId = videoId;
        client.get(MOVIE_TRAILER_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");        //log data
                JSONObject jsonObject= json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    //videoId = Movie.movieID(results);
                    Log.i(TAG, "videoId: " + Movie.movieID(results));
                    Log.i(TAG, "Results: " + results.toString()); //[{"popularity":75.882,"vote_count":13,"video":false,"poster_path":"
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}