package com.example.imbd;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class WachListFragmant extends Fragment implements MovieAdapter.MyClicks {

    RecyclerView recyclerWatchList;
    MovieAdapter adapter;
    ArrayList<Movie> watchList = new ArrayList<>();
    TextView emptyView;

    public WachListFragmant() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wach_list, container, false);

        recyclerWatchList = v.findViewById(R.id.recyclerWatchList);
        emptyView = v.findViewById(R.id.emptyView);

        // Reuse the same adapter and layout!
        recyclerWatchList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MovieAdapter(getContext(), watchList, this);
        recyclerWatchList.setAdapter(adapter);

        loadLocalWatchList();

        return v;
    }

    private void loadLocalWatchList() {
        String url = AppConfig.URL_GET_WATCHLIST;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    watchList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            // Note: The ID here is the Database ID (1, 2, 3), not TMDB ID
                            Movie movie = new Movie(
                                    obj.getString("id"),
                                    obj.getString("title"),
                                    obj.getString("image_url"),
                                    "10" // Default rating if DB doesn't have it, or fetch 'year'
                            );
                            watchList.add(movie);
                        }

                        if (watchList.isEmpty()) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("DB_ERROR", error.toString());
                    Toast.makeText(getContext(), "Could not fetch Watchlist. Check Server.", Toast.LENGTH_LONG).show();
                }
        );

        Volley.newRequestQueue(getContext()).add(request);
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Optional: What happens when you click a movie in the watchlist?
        // You could show a delete dialog, or just show details again.
        Toast.makeText(getContext(), "Clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
