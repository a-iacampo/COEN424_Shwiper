package com.example.shwiper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class adsLikedActivity extends AppCompatActivity implements adsLikedListAdapter.OnLikedAdsListener {

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected List<Ad> likedAdsList = new ArrayList<>();
    private FirebaseHelper firebaseHelper;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_liked);

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar2);

        firebaseHelper = new FirebaseHelper();
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(false);

        loadAds();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Load all course from DB
    public void loadAds(){

        firebaseHelper.fetchLikedAds(new FirebaseHelper.FirebaseHelperCallback() {
            @Override
            public void onFetchAdsGot(List<Ad> items) {
            }

            @Override
            public void onFetchLikedAds(List<Ad> items) {
                progressBar.setVisibility(View.INVISIBLE);
                likedAdsList = new ArrayList<>(items);
                initRecycleView();
            }

            @Override
            public void onFetchAd(DetailedAd ad) {

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
        Ad selectedAd = likedAdsList.get(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedAd.getUrl()));
        startActivity(browserIntent);
    }
}