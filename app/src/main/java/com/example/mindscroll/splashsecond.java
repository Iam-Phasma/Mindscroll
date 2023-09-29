package com.example.mindscroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class splashsecond extends AppCompatActivity {

    int count = 0;
    ConstraintLayout constraintLayout;

    //private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Do not sleep when the app is open
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_splashsecond);

        //Fullscreen beyond punch hole camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        // Set the background to a drawable resource
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        constraintLayout = findViewById(R.id.content);
        constraintLayout.setBackgroundResource(R.drawable.splash1pink);

        Button BtnNext = findViewById(R.id.btnNext);
        Button BtnSkipHome = findViewById(R.id.btnSkipHome);
        BtnSkipHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    Intent iPrev = new Intent(getApplicationContext(), Home.class);
                    startActivity(iPrev);
                    finish();
                } else if (count == 1) {
                    constraintLayout.setBackgroundResource(R.drawable.splash1pink);
                    BtnSkipHome.setText("Skip");
                    BtnNext.setText("Next");
                    count--;
                } else if (count == 2) {
                    constraintLayout.setBackgroundResource(R.drawable.splash2blue);
                    BtnSkipHome.setText("Back");
                    BtnNext.setText("Next");
                    count--;
                }
            }
        });


        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    constraintLayout.setBackgroundResource(R.drawable.splash2blue);
                    BtnSkipHome.setText("Back");
                    BtnNext.setText("Next");
                    count++;
                } else if (count == 1) {
                    constraintLayout.setBackgroundResource(R.drawable.splash3cyan);
                    BtnSkipHome.setText("Back");
                    BtnNext.setText("Home");
                    count++;
                } else if (count == 2) {
                    Intent iPrev = new Intent(getApplicationContext(), Home.class);
                    startActivity(iPrev);
                    finish();
                }
            }
        });

    }





}