package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Parcel // annotation indicates class is Parcelable
public class Movie {
    //public static Integer id;
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    Integer id;

    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        id = Integer.parseInt(String.valueOf(jsonObject.get("id"))); //jsonObject.getInt("id");
        //System.out.println("IDVAL: " + id);
    }
    public static List<Movie> fromJSONArray(JSONArray movieJsonArray) throws JSONException {
        //iterate through json array and construct a movie for each elem in array
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public static String movieID(JSONArray movieIDJsonArray) throws JSONException {
        String youtubeID = "";
        for (int i = 0; i < movieIDJsonArray.length(); i++) {
            JSONObject property = movieIDJsonArray.getJSONObject(i);
            if(!property.isNull("key")) {
                youtubeID = property.getString("key");
                break;
            }
        }
        return youtubeID;
    }

    public String getPosterPath() {
        //want to get full url to render properly
        String baseURL = "https://image.tmdb.org/t/p/w342/%s";
        return String.format(baseURL, posterPath);
    }

    public String getBackdropPath() {
        String baseURL = "https://image.tmdb.org/t/p/w342/%s";
        return String.format(baseURL, backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        Log.d("MOVIE", "voteAverage" + voteAverage);
        return voteAverage;
    }

    public Integer getId() {
        Log.d("MOVIE", "id " + id);
        return id;
    }
}

