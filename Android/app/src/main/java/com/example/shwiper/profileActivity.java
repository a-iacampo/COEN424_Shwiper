package com.example.shwiper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class profileActivity extends AppCompatActivity {

    private TextView nameOutput;
    private TextView emailOutput;
    private CheckBox maleCheck;
    private CheckBox femaleCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupUI();
    }

    protected void setupUI(){
        nameOutput = findViewById(R.id.nameOutput);
        emailOutput = findViewById(R.id.emailOutput);
        maleCheck = findViewById(R.id.maleCheck);
        femaleCheck = findViewById(R.id.femaleCheck);


    }
}