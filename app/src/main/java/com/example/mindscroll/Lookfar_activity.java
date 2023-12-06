package com.example.mindscroll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Lookfar_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the title bar and set full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Do not sleep when the app is open
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_lookfar);

        //Fullscreen beyond punch hole camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        getWindow().setBackgroundDrawableResource(R.drawable.hydro_wall);

        message();

        ImageView BtnLookExit = findViewById(R.id.btnLookExit);
        BtnLookExit.setOnClickListener(v -> {
            Intent iPrev = new Intent(getApplicationContext(), Home.class);
            startActivity(iPrev);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }

    private List<Toast> activeToasts = new ArrayList<>();
    public void message() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Look at the image from the side going to the center", Toast.LENGTH_LONG);
            toast1.show();
            activeToasts.add(toast1);

            Toast toast2 = Toast.makeText(getApplicationContext(), "Look for 10 seconds. Tap to exit", Toast.LENGTH_SHORT);
            toast2.show();
            activeToasts.add(toast2);
        }, 500); // Delay for 1 second
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (Toast toast : activeToasts) {
            toast.cancel();
        }

        activeToasts.clear();

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

}