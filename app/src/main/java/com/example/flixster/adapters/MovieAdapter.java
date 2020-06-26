package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

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

    //class *cannot* be static
    //implements View.OnClickListener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //modify to display the new activity via a intent when the user clicks on the row
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View view) {
            //gets item position
            int position = getAdapterPosition();
            //make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                //get the movie at the position (won't work if static)
                Movie movie = movies.get(position);
                //create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                //serialize the movies using parceler, ise its short name as key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                //show the activity
                context.startActivity(intent);
            }

        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;
            int placeHolder;
            //if phone is in landscape -> imageURL = back drop image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageURL = movie.getBackdropPath();
                placeHolder = R.drawable.flicks_backdrop_placeholder;
                //if phone is in portrait -> imageURL = poster image
            } else {
                imageURL = movie.getPosterPath();
                placeHolder = R.drawable.flicks_movie_placeholder;
            }
            int imgRadius = 30; //corner radius
            int imgMargin = 0; //crop margin
            Glide.with(context).load(imageURL).placeholder(placeHolder).transform(new RoundedCornersTransformation(imgRadius, imgMargin)).into(ivPoster);

        }
    }
}
