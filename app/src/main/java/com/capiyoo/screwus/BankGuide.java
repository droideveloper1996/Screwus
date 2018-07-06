package com.capiyoo.screwus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BankGuide extends Fragment {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    CardView cardView;
    ArrayList<Reviews> reviewsArrayList;
    RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        cardView = view.findViewById(R.id.newcardView);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Review").child(firebaseAuth.getCurrentUser().getUid());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FeedbackActivity.class));
            }
        });
        getMyFeedback();
        reviewsArrayList=new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBaar);
        recyclerView = view.findViewById(R.id.feedback_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    void getMyFeedback() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewsArrayList.clear();

                if (dataSnapshot != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            Reviews reviews = dataSnapshot1.getValue(Reviews.class);
                            reviewsArrayList.add(reviews);

                    }
                    MyFeebackRecylerViewAdapter recyclerViewAdapter = new MyFeebackRecylerViewAdapter(getContext(), reviewsArrayList);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    if (recyclerViewAdapter.getItemCount() > 0) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    class MyFeebackRecylerViewAdapter extends RecyclerView.Adapter<MyFeebackRecylerViewAdapter.MyFeedbackViewHolder> {

        Context mCtx;
        ArrayList<Reviews> reviews;

        public MyFeebackRecylerViewAdapter(Context mctx, ArrayList<Reviews> reviews) {

            this.mCtx = mctx;
            this.reviews = reviews;
        }

        @NonNull
        @Override
        public MyFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyFeedbackViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.feedback_post, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyFeedbackViewHolder holder, int position) {
            holder.review.setText(reviews.get(position).getmReview());
            holder.from.setText(reviews.get(position).getmFrom());
            holder.ratingBar.setRating(reviews.get(position).getmRating());
            if (reviews.get(position).getmRating() >= 4.0f && reviews.get(position).getmRating() < 5) {

                holder.reaction.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.smiling));

            }
            if (reviews.get(position).getmRating() < 3.0f) {
                holder.reaction.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.sad));

            }

            if (reviews.get(position).getmRating() == 3.0f) {
                holder.reaction.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.confused));

            }

            if (reviews.get(position).getmRating() == 5.0) {
                holder.reaction.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.love));

            }
        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }

        class MyFeedbackViewHolder extends RecyclerView.ViewHolder {
            TextView from;
            TextView review;
            RatingBar ratingBar;
            ImageView reaction;

            public MyFeedbackViewHolder(View itemView) {
                super(itemView);

                from = itemView.findViewById(R.id.from);
                review = itemView.findViewById(R.id.review);
                reaction = itemView.findViewById(R.id.reaction);
                ratingBar = itemView.findViewById(R.id.rating);
            }
        }
    }
}
