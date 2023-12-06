package com.example.mindscroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.graphics.Color;

public class splashsecond extends AppCompatActivity {

    int count = 0;
    ConstraintLayout constraintLayout;


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

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        constraintLayout = findViewById(R.id.content);
        constraintLayout.setBackgroundResource(R.drawable.splash1pink);

        Button BtnNext = findViewById(R.id.btnNext);
        Button BtnSkipHome = findViewById(R.id.btnSkipHome);
        BtnSkipHome.setOnClickListener(v -> {
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
        });

        BtnNext.setOnClickListener(v -> {
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
        });
    }
}