package com.example.shwiper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "FirebaseHelper";
    protected FirebaseFunctions mFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Connected to Firebase!", Toast.LENGTH_SHORT).show();

        mFunctions = FirebaseFunctions.getInstance();
        load();
    }

    private Task<String> onCallTest() {
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

    private void load() {
        Log.d(TAG, "Start call");

        // [START call_add_message]
        onCallTest().addOnCompleteListener(new OnCompleteListener<String>() {
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
                    List<Ad> ppl2 = Arrays.asList(mapper.readValue(result, Ad[].class));
                    for (int i = 0; i < ppl2.size(); i++) {
                        Log.d(TAG, "Map " + ppl2.get(i).getTitle());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // [END_EXCLUDE]
            }
        });
        // [END call_add_message]
    }
}