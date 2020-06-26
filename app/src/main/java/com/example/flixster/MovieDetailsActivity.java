package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    //the movie to display
    Movie movie;

    //the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView detailsPoster;

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
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

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
    }
}