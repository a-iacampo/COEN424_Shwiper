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
import java.util.List;


public class FirebaseHelper {
    protected List<Ad> ads = new ArrayList<>();

    protected static final String TAG = "FirebaseHelper";
    protected FirebaseFunctions mFunctions;
    protected FirebaseAuth fAuth;

    public FirebaseHelper() {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword("test@email.com", "123456");
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

    public void FectchFromScraper() {
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
                    ads = Arrays.asList(mapper.readValue(result, Ad[].class));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<ItemModel> items = new ArrayList<>();
                for (int i = 0; i < ads.size(); i++) {
                    items.add(new ItemModel(ads.get(i).getImage(), ads.get(i).getTitle(), ads.get(i).getPrice(), ads.get(i).getLocation(), ads.get(i).getDescription()));
                }

                // [END_EXCLUDE]
            }
        });
        // [END call_add_message]
    }
}
