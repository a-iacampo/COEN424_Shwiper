package com.example.shwiper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class adsLikedActivity extends AppCompatActivity implements adsLikedListAdapter.OnLikedAdsListener {

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected List<Ad> likedAdsList = new ArrayList<>();
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_liked);

        firebaseHelper = new FirebaseHelper();
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(false);

        loadAds();
    }

    //Load all course from DB
    public void loadAds(){

        firebaseHelper.fetchLikedAds(new FirebaseHelper.FirebaseHelperCallback() {
            @Override
            public void onFetchAdsGot(List<Ad> items) {
            }

            @Override
            public void onFetchLikedAds(List<Ad> items) {
                likedAdsList = new ArrayList<>(items);
                initRecycleView();
            }

        });
    }

    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(adsLikedActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new adsLikedListAdapter(adsLikedActivity.this, likedAdsList, adsLikedActivity.this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLikedAdsClick(int position) {

    }
}