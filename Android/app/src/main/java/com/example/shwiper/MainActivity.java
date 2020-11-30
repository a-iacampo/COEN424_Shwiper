package com.example.shwiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shwiper.CardStackAdapter;
import com.example.shwiper.CardStackCallback;
import com.example.shwiper.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CardView cardView;
    private ProgressBar progressBar;


    protected FirebaseHelper firebaseHelper;
    //protected ArrayList<Ad> listFetchedOfAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initFirebaseHelper();

        progressBar.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        Log.d(TAG, "initViews:started");
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_drawer);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initDrawerMenu(){

    }

    private void initFirebaseHelper(){
        firebaseHelper = new FirebaseHelper();

        //Attempts to fetch Kijiji Ads from Scraper
        firebaseHelper.FectchFromScraper(new FirebaseHelper.FirebaseHelperCallback(){
            @Override
            public void onFetchAdsGot(List<Ad> items) {

                progressBar.setVisibility(View.INVISIBLE);

                initCardStack(items);
            }

            @Override
            public void onFetchLikedAds(List<Ad> items) {

            }
        });
    }

    private void initCardStack(List<Ad> listFetchedOfAds){
        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right){
                    Toast.makeText(MainActivity.this, "Liked !", Toast.LENGTH_SHORT).show();
                    firebaseHelper.storeLikedAd(listFetchedOfAds.get(manager.getTopPosition()));
                }
                if (direction == Direction.Left){
                    Toast.makeText(MainActivity.this, "Did not Like !", Toast.LENGTH_SHORT).show();
                }

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5){
                    paginate(listFetchedOfAds);
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());

            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setStackFrom(StackFrom.Bottom);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new CardStackAdapter(listFetchedOfAds, MainActivity.this);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

    }


    private void paginate(List<Ad> items) {
        List<Ad> old = adapter.getItems();
        List<Ad> baru = new ArrayList<>(items);
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                Intent intent = new Intent(MainActivity.this, profileActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                Intent intent2 = new Intent(MainActivity.this, adsLikedActivity.class);
                startActivity(intent2);
                return true;
            case R.id.item3:
                logout();
                return true;
        }
        return true;
    }

    //Logout User
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
