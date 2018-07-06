package com.capiyoo.screwus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHoler> {

    Context mctx;
    ArrayList<Reviews> reviews;

    public RecyclerViewAdapter(Context mctx, ArrayList<Reviews> reviews) {
        this.mctx = mctx;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public RecyclerViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHoler(LayoutInflater.from(mctx).inflate(R.layout.feedback_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHoler holder, int position) {
        holder.review.setText(reviews.get(position).getmReview());
        holder.from.setText(reviews.get(position).getmFrom());
        holder.ratingBar.setRating(reviews.get(position).getmRating());
        if (reviews.get(position).getmRating() >= 4.0f && reviews.get(position).getmRating() < 5) {

            holder.reaction.setImageDrawable(mctx.getResources().getDrawable(R.drawable.smiling));

        }
        if (reviews.get(position).getmRating() < 3.0f) {
            holder.reaction.setImageDrawable(mctx.getResources().getDrawable(R.drawable.sad));

        }

        if (reviews.get(position).getmRating() == 3.0f) {
            holder.reaction.setImageDrawable(mctx.getResources().getDrawable(R.drawable.confused));

        }

        if (reviews.get(position).getmRating() == 5.0) {
            holder.reaction.setImageDrawable(mctx.getResources().getDrawable(R.drawable.love));

        }


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class RecyclerViewHoler extends RecyclerView.ViewHolder {
        TextView from;
        TextView review;
        RatingBar ratingBar;
        ImageView reaction;

        public RecyclerViewHoler(View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from);
            review = itemView.findViewById(R.id.review);
            reaction = itemView.findViewById(R.id.reaction);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }
}
