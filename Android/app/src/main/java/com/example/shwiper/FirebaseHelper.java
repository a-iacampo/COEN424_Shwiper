package com.example.shwiper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseHelper {

    protected static final String TAG = "FirebaseHelper";
    protected FirebaseFunctions mFunctions;

    public FirebaseHelper() {
        mFunctions = FirebaseFunctions.getInstance();
    }

    private Task<String> onCallFetchFromScraper() {
        Log.d(TAG, "Call");
        return mFunctions.getHttpsCallable("FetchFromScraper").call().continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                String result = task.getResult().getData().toString();
                Log.d(TAG, "Result: " + result);
                return result;
            }
        });
    }

    public void FectchFromScraper(FirebaseHelperCallback callback) {
        Log.d(TAG, "Start call");

        // [START call_add_message]
        onCallFetchFromScraper().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        Object details = ffe.getDetails();
                    }
                    Log.d(TAG, "ERROR: " + e);
                    return;
                }
                // [START_EXCLUDE]
                try {
                    String result = task.getResult();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                    List<Ad> ads = Arrays.asList(mapper.readValue(result, Ad[].class));

                    ArrayList<ItemModel> items = new ArrayList<>();
                    for (int i = 0; i < ads.size(); i++) {
                        items.add(new ItemModel(ads.get(i).getImage(), ads.get(i).getTitle(), ads.get(i).getPrice(), ads.get(i).getLocation(), ads.get(i).getDescription(), ads.get(i).getUrl()));
                    }

                    //Send collected strings to callback
                    callback.onFetchAdsGot(items);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Task<String> onCallStoreLikedAd(ItemModel likedAd) {
        Log.d(TAG, "onCallStoreLikedAd");

        //Create Map for likedAd JSON document
        Map<String, Object> data = new HashMap<>();
        data.put("title", likedAd.getTitle());
        data.put("image", likedAd.getImage());
        data.put("price", likedAd.getPrice());
        data.put("description", likedAd.getDescription());
        data.put("location", likedAd.getLocation());
        data.put("url", likedAd.getUrl());

        return mFunctions.getHttpsCallable("storeLikedAd").call(data).continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {

                //returns nothing....
                return "";
            }
        });
    }

    public void storeLikedAd(ItemModel likedAd){

        onCallStoreLikedAd(likedAd).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        Object details = ffe.getDetails();
                    }
                    Log.d(TAG, "ERROR: " + e);
                }
            }
        });
    }

    private Task<String> onCallFetchLikedAds() {

        return mFunctions.getHttpsCallable("fetchLikedAds").call().continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                String result = task.getResult().getData().toString();
                Log.d(TAG, "Result: " + result);
                return result;
            }
        });
    }

    public void fetchLikedAds(FirebaseHelperCallback callback) {
        Log.d(TAG, "Start fetchLikedAds");

        // [START call_add_message]
        onCallFetchLikedAds().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        Object details = ffe.getDetails();
                    }
                    Log.d(TAG, "ERROR: " + e);
                    return;
                }
                // [START_EXCLUDE]
                try {
                    String result = task.getResult();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                    List<Ad> likedAds = Arrays.asList(mapper.readValue(result, Ad[].class));

                    ArrayList<ItemModel> items = new ArrayList<>();
                    for (int i = 0; i < likedAds.size(); i++) {
                        items.add(new ItemModel(likedAds.get(i).getImage(), likedAds.get(i).getTitle(), likedAds.get(i).getPrice(), likedAds.get(i).getLocation(), likedAds.get(i).getDescription(), likedAds.get(i).getUrl()));
                    }

                    //Send collected strings to callback
                    callback.onFetchLikedAds(items);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface FirebaseHelperCallback{
        void onFetchAdsGot(ArrayList<ItemModel> items);
        void onFetchLikedAds(ArrayList<ItemModel> items);
    }
}
