package com.example.shwiper;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.navigation.NavigationView;

public class profileActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //assign buttons
        submit = (Button)findViewById(R.id.location_submit_button);
        radioGroup = (RadioGroup)findViewById(R.id.location_groupradio);

        // reset default
        radioGroup.clearCheck();
        radioGroup.check(R.id.radio_greater_montreal);//fetch from database to set preference

        // listener to radio group
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    //when selection changes
                    //check changed selection

                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                    }
                });

        // listener to submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //get checked when submit clicked, else -1
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    //toss error
                } else {
                    //return selected id
                    switch (selectedId) {
                        case R.id.radio_greater_montreal:
                            //set location 1
                            break;
                        case R.id.radio_city_of_montreal:
                            //set location 2
                            break;
                        case R.id.radio_laval_north_shore:
                            //set location 3
                            break;
                        case R.id.radio_longueuil_south_shore:
                            //set location 4
                            break;
                        case R.id.radio_west_island:
                            //set location 5
                            break;
                    }
                }
            }
        });
    }
}