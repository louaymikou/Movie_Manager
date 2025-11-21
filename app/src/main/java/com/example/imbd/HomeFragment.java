package com.example.imbd;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MovieAdapter.MyClicks {

    RecyclerView recyclerMovies;
    MovieAdapter movieAdapter;
    ArrayList<Movie> movieList = new ArrayList<>();
    ProgressBar loadingProgressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerMovies = v.findViewById(R.id.recyclerMovies);
        loadingProgressBar = v.findViewById(R.id.loadingProgressBar);

        // 2 columns for the grid looks better with the new card design
        recyclerMovies.setLayoutManager(new GridLayoutManager(getContext(), 2));

        movieAdapter = new MovieAdapter(getContext(), movieList, this);
        recyclerMovies.setAdapter(movieAdapter);

        loadMovies();

        return v;
    }

    private void loadMovies() {
        loadingProgressBar.setVisibility(View.VISIBLE);

        String url = AppConfig.getPopularMoviesUrl(1);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    try {
                        JSONArray items = response.getJSONArray("results");
                        movieList.clear(); // Clear old data if any

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject obj = items.getJSONObject(i);

                            String posterPath = obj.getString("poster_path");
                            String fullPosterUrl = AppConfig.TMDB_IMAGE_BASE_URL + posterPath;

                            Movie movie = new Movie(
                                    obj.getString("id"),
                                    obj.getString("title"),
                                    fullPosterUrl,
                                    obj.getString("vote_average")
                            );

                            movieList.add(movie);
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
                    Toast.makeText(getContext(), "API Connection Error", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Send ID to Details Fragment
        Bundle bundle = new Bundle();
        bundle.putString("movieId", movie.getId());

        getParentFragmentManager().setFragmentResult("requestKey", bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DetailMovieFragment())
                .addToBackStack(null)
                .commit();
    }
}
