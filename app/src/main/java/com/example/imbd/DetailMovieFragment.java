package com.example.imbd;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

public class DetailMovieFragment extends Fragment {

    ImageView poster;
    Button btnAddToDB;
    TextView movietitle, movierating, moviedescription, moviedate;

    String title = "", genre = "", year = "", description = "", image_url = "";
    String movieId;

    public DetailMovieFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        moviedate = v.findViewById(R.id.detailsDate);
        poster = v.findViewById(R.id.detailsPoster);
        movietitle = v.findViewById(R.id.detailsTitle);
        movierating = v.findViewById(R.id.detailsRating);
        moviedescription = v.findViewById(R.id.detailsDescription);
        btnAddToDB = v.findViewById(R.id.btnAddToDB);

        // Listen for the ID passed from HomeFragment
        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            movieId = bundle.getString("movieId");
            if (movieId != null) {
                getMovieDetails(movieId);
            }
        });

        btnAddToDB.setOnClickListener(view -> addMovieToDatabase());

        return v;
    }

    private void getMovieDetails(String id) {
        String url = AppConfig.getMovieDetailsUrl(id);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        title = response.getString("title");
                        movietitle.setText(title);

                        description = response.getString("overview");
                        moviedescription.setText(description);

                        String vote = response.getString("vote_average");
                        movierating.setText(vote);

                        year = response.getString("release_date");
                        moviedate.setText(year);

                        JSONArray genres = response.getJSONArray("genres");
                        if (genres.length() > 0) {
                            genre = genres.getJSONObject(0).getString("name");
                        }

                        String posterPath = response.getString("poster_path");
                        image_url = AppConfig.TMDB_IMAGE_BASE_URL + posterPath;

                        Glide.with(this).load(image_url).into(poster);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getContext(), "Error loading details", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void addMovieToDatabase() {
        String url = AppConfig.URL_ADD_MOVIE;
        Toast.makeText(getContext(), "Adding to watchlist...", Toast.LENGTH_SHORT).show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(getContext(), "Movie added to Watchlist!", Toast.LENGTH_LONG).show(),
                error -> {
                    Toast.makeText(getContext(), "Connection Error: Check Server", Toast.LENGTH_SHORT).show();
                    Log.e("DB_ERROR", error.toString());
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("genre", genre);
                params.put("year", year);
                params.put("description", description);
                params.put("image_url", image_url);
                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(postRequest);
    }
}
