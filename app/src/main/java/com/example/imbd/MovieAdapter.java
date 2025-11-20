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

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    Context context;
    List<MovieModel> movieList;

    MyClicks myClicks;
    public interface MyClicks {
        void onClick(int position);
    }


    public MovieAdapter(List<MovieModel> movieList, MyClicks myClicks) {
        this.movieList = movieList;
        this.myClicks = myClicks;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(v, myClicks);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.rating.setText(movie.getRating());

        Glide.with(context)
                .load(movie.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView title, rating;

        public MovieViewHolder(@NonNull View itemView, MyClicks myClicks) {
            super(itemView);

            poster = itemView.findViewById(R.id.imagePoster);
            title = itemView.findViewById(R.id.textTitle);
            rating = itemView.findViewById(R.id.textRating);
            itemView.setOnClickListener(v -> {
                if (myClicks != null)
                    myClicks.onClick(getAdapterPosition());
            });
        }
    }
}
