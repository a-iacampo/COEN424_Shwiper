package com.example.shwiper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class OnCardClick extends AppCompatActivity {
    protected Toolbar toolbar;
    protected HorizontalScrollView horizontalScrollView;
    protected TextView itemName;
    protected TextView itemPrice;
    protected TextView itemLocation;
    protected TextView itemSize;
    protected TextView itemDescription;
    protected ProgressBar progressBar;

    protected FirebaseHelper firebaseHelper;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_card_click);
        setupUI();
        initFirebaseHelper();
    }

    private void setupUI() {
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        horizontalScrollView = findViewById(R.id.imageScrollView);
        itemName = findViewById(R.id.itemNameText);
        itemPrice = findViewById(R.id.itemPriceText);
        itemLocation = findViewById(R.id.itemLocationText);
        itemSize = findViewById(R.id.itemSizeText);
        itemDescription = findViewById(R.id.itemDescriptionText);
        progressBar = findViewById(R.id.ad_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }

    private void initFirebaseHelper(){
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
                loadUI(ad);
            }
        });
    }

    private void loadUI(DetailedAd ad) {
        itemName.setText(ad.getTitle());
        itemPrice.setText("Price: " + ad.getPrice());
        itemSize.setText("Size: " + ad.getSize());
        itemLocation.setText(ad.getLocation());
        itemDescription.setText(ad.getDescription());
    }
}