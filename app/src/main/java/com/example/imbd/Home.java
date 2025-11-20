package com.example.imbd;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class Home extends Fragment implements MovieAdapter.MyClicks {
    RecyclerView recyclerMovies;
    MovieAdapter movieAdapter;
    ArrayList<MovieModel> movieList = new ArrayList<>();
    private ProgressBar loadingProgressBar;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerMovies = v.findViewById(R.id.recyclerMovies);
        loadingProgressBar = v.findViewById(R.id.loadingProgressBar); // <-- Find the ProgressBar by its ID

        // 3 columns
        recyclerMovies.setLayoutManager(new GridLayoutManager(getContext(), 3));

        movieAdapter = new MovieAdapter(this.movieList,this);
        recyclerMovies.setAdapter(movieAdapter);

        loadMovies();
        Log.d("vvv", "onCreateView: "+movieList.size());


        return v;
    }
    private void loadMovies() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=3eb1ae63cae30f7f59288876da6c30c7&language=en-US&page=1";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    try {
                        // TMDB uses "results", not "items"
                        JSONArray items = response.getJSONArray("results");
                        Log.d("vvvcc", "onCreateView: "+items.length());

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject obj = items.getJSONObject(i);

                            String poster = obj.getString("poster_path");
                            String fullPosterUrl = "https://image.tmdb.org/t/p/w500" + poster;

                            MovieModel movie = new MovieModel(
                                    obj.getString("id"),
                                    obj.getString("title"),
                                    fullPosterUrl,
                                    obj.getString("vote_average")
                            );

                            this.movieList.add(movie);
                            Log.d("vvvtt", "onCreateView: "+movieList.size());
                        }

                        movieAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    Log.e("API_ERROR", error.toString());
                    Toast.makeText(getContext(), "API error", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    @Override
    public void onClick(int position) {
        MovieModel movie = movieList.get(position);

        // Intent vers page de détails
        Bundle intent = new Bundle();
        intent.putString("movieId", movie.getId());
        intent.putString("movieTitle", movie.getTitle());
        intent.putString("movieImage", movie.getImageUrl());
        intent.putString("movieRating", movie.getRating());

        getParentFragmentManager().setFragmentResult("movieId", intent);

    }

}