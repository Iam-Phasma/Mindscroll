package com.example.mindscroll;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;

import java.io.IOException;


public class ambience extends AppCompatActivity {

    MediaPlayer[] mediaPlayers = new MediaPlayer[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the title bar and set full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Do not sleep when the app is open
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_ambience);

        //Fullscreen beyond punch hole camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        // Set the background to a drawable resource
        getWindow().setBackgroundDrawableResource(R.drawable.musicbg);

        ScrollView ScrollView = findViewById(R.id.scrollviewMusic);
        ScrollView.setVerticalScrollBarEnabled(false);

        ImageView ImgviewSurf = findViewById(R.id.imgviewSurf);
        Glide.with(this).load(R.drawable.surf).into(ImgviewSurf);

        ImageView ImgviewNature = findViewById(R.id.imgviewNature);
        Glide.with(this).load(R.drawable.bird).into(ImgviewNature);

        ImageView ImgviewCar = findViewById(R.id.imgviewCar);
        Glide.with(this).load(R.drawable.car).into(ImgviewCar);

        ImageView ImgviewDance = findViewById(R.id.imgviewDance);
        Glide.with(this).load(R.drawable.dance).into(ImgviewDance);

        ImageView ImgviewThunder = findViewById(R.id.imgviewThunder);
        Glide.with(this).load(R.drawable.thunder).into(ImgviewThunder);

        ImageView ImgviewBlizzard = findViewById(R.id.imgviewBlizzard);
        Glide.with(this).load(R.drawable.snow).into(ImgviewBlizzard);

        ImageView ImgviewCowboy = findViewById(R.id.imgviewCowboy);
        Glide.with(this).load(R.drawable.cowboy).into(ImgviewCowboy);


        // Initialize the MediaPlayers for each button
        mediaPlayers[0] = MediaPlayer.create(this, R.raw.oceansound);
        mediaPlayers[1] = MediaPlayer.create(this, R.raw.naturesound);
        mediaPlayers[2] = MediaPlayer.create(this, R.raw.citysound);
        mediaPlayers[3] = MediaPlayer.create(this, R.raw.oldsong);
        mediaPlayers[4] = MediaPlayer.create(this, R.raw.thunder);
        mediaPlayers[5] = MediaPlayer.create(this, R.raw.blizzard);
        mediaPlayers[6] = MediaPlayer.create(this, R.raw.wildwest);

        // Initialize button 1
        final ImageView BtnSea = findViewById(R.id.btnSea);
        BtnSea.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = false;
            final MediaPlayer mediaPlayer = mediaPlayers[0];
            // Use mediaPlayers[0] for the first button

            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    isPlaying = true;
                }

                if (!isPlaying) {
                    BtnSea.setImageResource(R.drawable.musicholder);
                    mediaPlayer.setVolume(0.0f, 0.0f);
                } else {
                    BtnSea.setImageResource(R.drawable.musicholderclicked);
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
            }
        });

        // Initialize button 2
        final ImageView BtnNature = findViewById(R.id.btnNature);
        BtnNature.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = false;
            final MediaPlayer mediaPlayer = mediaPlayers[1];
            // Use mediaPlayers[1] for the second button

            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    isPlaying = true;
                }

                if (!isPlaying) {
                    BtnNature.setImageResource(R.drawable.musicholder);
                    mediaPlayer.setVolume(0.0f, 0.0f);
                } else {
                    BtnNature.setImageResource(R.drawable.musicholderclicked);
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
            }
        });

        // Initialize button 3
        final ImageView BtnCity = findViewById(R.id.btnCity);
        BtnCity.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = false;
            final MediaPlayer mediaPlayer = mediaPlayers[2];
            // Use mediaPlayers[2] for the third button

            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    isPlaying = true;
                }

                if (!isPlaying) {
                    BtnCity.setImageResource(R.drawable.musicholder);
                    mediaPlayer.setVolume(0.0f, 0.0f);
                } else {
                    BtnCity.setImageResource(R.drawable.musicholderclicked);
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
            }
        });

        // Initialize button 4
        final ImageView BtnDance = findViewById(R.id.btnDance);
        BtnDance.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = false;
            final MediaPlayer mediaPlayer = mediaPlayers[3];
            // Use mediaPlayers[3] for the fourth button

            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    isPlaying = true;
                }

                if (!isPlaying) {
                    BtnDance.setImageResource(R.drawable.musicholder);
                    mediaPlayer.setVolume(0.0f, 0.0f);
                } else {
                    BtnDance.setImageResource(R.drawable.musicholderclicked);
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
            }
        });

        // Initialize button 5
        final ImageView BtnThunder = findViewById(R.id.btnThunder);
        BtnThunder.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = false;
            final MediaPlayer mediaPlayer = mediaPlayers[4];
            // Use mediaPlayers[4] for the fourth button

            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    isPlaying = true;
                }

                if (!isPlaying) {
                    BtnThunder.setImageResource(R.drawable.musicholder);
                    mediaPlayer.setVolume(0.0f, 0.0f);
                } else {
                    BtnThunder.setImageResource(R.drawable.musicholderclicked);
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
            }
        });

        // Initialize button 6
        final ImageView BtnBlizzard = findViewById(R.id.btnBlizzard);
        BtnBlizzard.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = false;
            final MediaPlayer mediaPlayer = mediaPlayers[5];
            // Use mediaPlayers[5] for the fourth button

            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    isPlaying = true;
                }

                if (!isPlaying) {
                    BtnBlizzard.setImageResource(R.drawable.musicholder);
                    mediaPlayer.setVolume(0.0f, 0.0f);
                } else {
                    BtnBlizzard.setImageResource(R.drawable.musicholderclicked);
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
            }
        });

        // Initialize button 7
        final ImageView BtnWildWest = findViewById(R.id.btnWildWest);
        BtnWildWest.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = false;
            final MediaPlayer mediaPlayer = mediaPlayers[6];
            // Use mediaPlayers[6] for the fourth button

            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    isPlaying = true;
                }

                if (!isPlaying) {
                    BtnWildWest.setImageResource(R.drawable.musicholder);
                    mediaPlayer.setVolume(0.0f, 0.0f);
                } else {
                    BtnWildWest.setImageResource(R.drawable.musicholderclicked);
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
            }
        });


        ImageButton ImgBtnHomeMusic = findViewById(R.id.imgbtnHomeMusic);
        ImgBtnHomeMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (MediaPlayer mp : mediaPlayers) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                }

                Intent iPrevBack = new Intent(getApplicationContext(), Home.class);
                startActivity(iPrevBack);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {

        for (MediaPlayer mp : mediaPlayers) {
            if (mp.isPlaying()) {
                mp.stop();
            }
        }


        super.onBackPressed();
        Intent iPrevBack = new Intent(getApplicationContext(), Home.class);
        startActivity(iPrevBack);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}