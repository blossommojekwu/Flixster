package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    //the movie to display
    Movie movie;

    //the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView detailsPoster;

    String movieId;
    String endpointURL;
    public String MOVIE_TRAILER_URL = "";
    public static final String TAG = "MovieDetailsActivity";
    String videoId;
    //retrieve and unwrap the Movie from the Intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity_movie_details.xml -> ActivityMovieDetailsBinding
        //setContentView(R.layout.activity_movie_details);
        ActivityMovieDetailsBinding activityMovieDetailsBinding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        //layout of activity is stored in a special property called root
        View movieDetailsView = activityMovieDetailsBinding.getRoot();
        setContentView(movieDetailsView);

        //resolve the view objects
        tvTitle = activityMovieDetailsBinding.tvTitle;
        tvOverview = activityMovieDetailsBinding.tvOverview;
        rbVoteAverage = activityMovieDetailsBinding.rbVoteAverage;
        detailsPoster = activityMovieDetailsBinding.detailsPoster;

        //unwrap the movie passed in via intent, using its simple name key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle())); //"Showing details for 'Artemis Fowl'

        //set the title and overview;
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        //vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        //set detailsPoster
        String detailsImg;
        int dplaceHolder;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            detailsImg = movie.getBackdropPath();
            dplaceHolder = R.drawable.flicks_backdrop_placeholder;
            //if phone is in portrait -> imageURL = poster image
        } else {
            detailsImg = movie.getPosterPath();
            dplaceHolder = R.drawable.flicks_movie_placeholder;
        }
        Glide.with(this).load(detailsImg).placeholder(dplaceHolder).transform(new RoundedCornersTransformation(30, 0)).into(detailsPoster);

        //https://api.themoviedb.org/3/movie/554993/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US
        movieId = Integer.toString(movie.getId());
        endpointURL = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + this.getString(R.string.movies_api_key);
        MOVIE_TRAILER_URL = endpointURL;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(MOVIE_TRAILER_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");        //log data
                JSONObject jsonObject= json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    videoId = Movie.movieID(results);
                    Log.i(TAG, "videoId: " + videoId); //475430
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

        //implement listener that
        detailsPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Play Trailer", Toast.LENGTH_SHORT).show();
                //create an intent that will contain results
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                //Use an Intent and pass the video id as a String extra
                intent.putExtra("videoId", videoId);
                startActivity(intent);
            }
        });

    }
}