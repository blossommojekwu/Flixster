package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    //context to inflate view and position
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    TextView tvTitle;
    TextView tvOverview;
    ImageView ivPoster;

    //inflates a layout from XML(item_movie) and returning the holder (place layout inside ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        //item_movie.xml -> ItemMovieBinding
        ItemMovieBinding itemMovieBinding = ItemMovieBinding.inflate(LayoutInflater.from(context));
        View movieView = itemMovieBinding.getRoot(); //LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        tvTitle = itemMovieBinding.tvTitle;
        tvOverview = itemMovieBinding.tvOverview;
        ivPoster = itemMovieBinding.ivPoster;

        return new ViewHolder(movieView);
    }

    //populating data into the view through the viewholder
    //take data at the position and put it into the view contained inside the viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        //get the movie at the position
        Movie movie = movies.get(position);
        //bind the movie data into the viewHolder
        holder.bind(movie);
    }

    //return the count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;
            //if phone is in landscape -> imageURL = back drop image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageURL = movie.getBackdropPath();
                //if phone is in portrait -> imageURL = poster image
            } else {
                imageURL = movie.getPosterPath();
            }
            int imgRadius = 30; //corner radius
            int imgMargin = 0; //crop margin
            Glide.with(context).load(imageURL).transform(new RoundedCornersTransformation(imgRadius, imgMargin)).into(ivPoster);

        }
    }
}
