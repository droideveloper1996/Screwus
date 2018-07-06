package com.capiyoo.screwus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Reviews> reviewsArrayList;
    private ProgressBar progressBar;
    DatabaseReference firebaseDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        reviewsArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Review");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar = view.findViewById(R.id.progressBar);



        getReviews(getContext());

        return view;
    }

    /***
     *  Review
     *      Uid1
     *          Review1
     *          Review2
     *          Review3
     *      Uid2
     *          Review1
     *          Review2
     *          Review3
     *
     *
     * @param mCtx
     */
    public void getReviews(final Context mCtx) {

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewsArrayList.clear();
                if (dataSnapshot != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            Reviews reviews = dataSnapshot2.getValue(Reviews.class);
                            reviewsArrayList.add(reviews);
                        }
                    }
                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), reviewsArrayList);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    if(recyclerViewAdapter.getItemCount()>0)
                    {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
