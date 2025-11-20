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
import androidx.appcompat.app.AppCompatActivity;
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

public class MovieDetails extends Fragment {
    public MovieDetails() {
        // Required empty public constructor
    }
    ImageView poster;
    Button btnAddToDB;
    TextView movietitle, movierating, moviedescription;
    TextView moviedate;
    String title = "";
    String genre = "";
    String year = "";
    String description = "";
    String image_url = "";

    String movieId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_details, container, false);
        moviedate = v.findViewById(R.id.detailsDate);
        poster = v.findViewById(R.id.detailsPoster);
        movietitle = v.findViewById(R.id.detailsTitle);
        movierating = v.findViewById(R.id.detailsRating);
        moviedescription = v.findViewById(R.id.detailsDescription);
        btnAddToDB = v.findViewById(R.id.btnAddToDB);

        getParentFragmentManager().setFragmentResultListener("movieId", this, new FragmentResultListener() {


                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        movieId = bundle.getString("movieId");
                        moviedate.setText(bundle.getString("movieDate"));
                    }
                });

            getMovieDetails(movieId);

        btnAddToDB.setOnClickListener(view -> addMovieToDatabase());
        return v;
    }

    private void getMovieDetails(String id) {

        String apiKey = "3eb1ae63cae30f7f59288876da6c30c7";
        String url = "https://api.themoviedb.org/3/movie/" + id +
                "?api_key=" + apiKey + "&language=en-US";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        // ====== Title ======
                        title = response.getString("title");
                        movietitle.setText(title);

                        // ====== Description ======
                        description = response.getString("overview");
                        moviedescription.setText(description);

                        // ====== Rating ======
                        String vote = response.getString("vote_average");
                        movierating.setText("Rating : " + vote);

                        // ====== Release date ======
                        year = response.getString("release_date");
                        moviedate.setText(year);

                        // ====== Genre (first one) ======
                        JSONArray genres = response.getJSONArray("genres");
                        if (genres.length() > 0) {
                            genre = genres.getJSONObject(0).getString("name");
                        }

                        // ====== Image ======
                        String posterPath = response.getString("poster_path");
                        image_url = "https://image.tmdb.org/t/p/w500" + posterPath;
                        Glide.with(this).load(image_url).into(poster);

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        Log.e("DETAIL_PARSE", e.toString());
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
                    Log.d("DETAIL_ERROR", error.toString());
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    private void addMovieToDatabase() {

        String url = "http://192.168.56.1/movie/add_movie.php"; //

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(getContext(), "Film ajouté avec succès !", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    Log.d("Erreur de connexion", error.toString());
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

        queue.add(postRequest);
    }
}
