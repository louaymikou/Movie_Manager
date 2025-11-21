package com.example.imbd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieList;
    private MyClicks listener;
    private Context context;

    // Interface for click events
    public interface MyClicks {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(Context context, ArrayList<Movie> movieList, MyClicks listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.textTitle.setText(movie.getTitle());
        holder.textRating.setText(movie.getRating());

        // Load image with Glide and round the corners slightly
        Glide.with(context)
                .load(movie.getImageUrl())
                .transform(new RoundedCorners(16)) // Matches your XML radius roughly
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imagePoster);

        // Handle Clicks
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textRating;
        ImageView imagePoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textRating = itemView.findViewById(R.id.textRating);
            imagePoster = itemView.findViewById(R.id.imagePoster);
        }
    }
}
