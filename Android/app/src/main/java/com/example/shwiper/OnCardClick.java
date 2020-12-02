package com.example.shwiper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class OnCardClick extends AppCompatActivity {

    static String TAG = "OnCarClick";

    protected Toolbar toolbar;
    protected TextView itemName;
    protected TextView itemPrice;
    protected TextView itemLocation;
    protected TextView itemSize;
    protected TextView staticDescription;
    protected TextView itemDescription;
    protected ProgressBar progressBar;

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    protected FirebaseHelper firebaseHelper;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_card_click);
        setupUI();
        loadAd();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "Click back button");
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

    private void setupUI() {
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.horizontal_recycler_view);
        recyclerView.setHasFixedSize(false);
        itemName = findViewById(R.id.itemNameText);
        itemPrice = findViewById(R.id.itemPriceText);
        itemLocation = findViewById(R.id.itemLocationText);
        itemSize = findViewById(R.id.itemSizeText);
        itemDescription = findViewById(R.id.itemDescriptionText);
        staticDescription = findViewById(R.id.static_description);
        progressBar = findViewById(R.id.ad_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }

    private void loadAd(){
        firebaseHelper = new FirebaseHelper();

        //Attempts to fetch ad from Kijiji
        firebaseHelper.fetchAd(url, new FirebaseHelper.FirebaseHelperCallback() {
            @Override
            public void onFetchAdsGot(List<Ad> items) {

            }

            @Override
            public void onFetchLikedAds(List<Ad> items) {

            }

            @Override
            public void onFetchAd(DetailedAd ad) {
                progressBar.setVisibility(View.INVISIBLE);
                List<String> images = Arrays.asList(ad.getImages().split(","));
                initRecycleView(images);
                loadUI(ad);
            }
        });
    }

    private void initRecycleView(List<String> imageUrl) {
        layoutManager = new LinearLayoutManager(OnCardClick.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new horizontalListAdapter(imageUrl);
        recyclerView.setAdapter(mAdapter);
    }

    private void loadUI(DetailedAd ad) {
        itemName.setText(ad.getTitle());

        if(ad.getPrice().equals("undefined"))
            itemPrice.setText("Price: -");
        else
            itemPrice.setText("Price: " + ad.getPrice() + "$");

        if(ad.getSize().equals("undefined"))
            itemSize.setText("Size: -");
        else
            itemSize.setText("Size: " + ad.getSize());

        staticDescription.setText("Description");
        itemLocation.setText(ad.getLocation());
        itemDescription.setText(ad.getDescription());
    }
}