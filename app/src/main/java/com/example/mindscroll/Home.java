package com.example.mindscroll;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Home extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView imgviewProfile;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PROFILE_IMAGE_KEY = "profileImage";

    Button BtnPrevStats;
    private boolean backPressedOnce = false;
    TextView TvName;

    ImageButton BtnTrivia;
    Dialog dlg;
    EditText ETNameHome;

    private static final int PERMISSION_REQUEST_CODE = 1;
    int contextNotif;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title bar and set full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Do not sleep when the app is open
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_home);

        //Fullscreen beyond punch hole camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        // Set the background to a drawable resource
        getWindow().setBackgroundDrawableResource(R.drawable.wallrift);


        //Setting GIFs
        ImageView ImgViewCoffee = findViewById(R.id.imgviewCoffee);
        Glide.with(this).load(R.drawable.coffee).into(ImgViewCoffee);

        ImageView ImgViewLookFar = findViewById(R.id.imgviewLookFar);
        Glide.with(this).load(R.drawable.look).into(ImgViewLookFar);

        ImageView ImgViewMusic = findViewById(R.id.imgviewMusic);
        Glide.with(this).load(R.drawable.notes).into(ImgViewMusic);


        //Button Info
        ImageButton BtnAppInfo = findViewById(R.id.btnAppInfo);
        BtnAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Screen Monitoring Application Developed by Madonza Ricson Jay.", Toast.LENGTH_SHORT).show();
            }
        });


        // Changing Image Profile
        imgviewProfile = findViewById(R.id.imgviewProfile);
        imgviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an intent to launch the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        ImageButton ImgBtmBoardCoffee = findViewById(R.id.imgbtnBoardCoffee);
        ImgBtmBoardCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Phone's Current Record. View Details for more information", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton ImgBtmBoardLook = findViewById(R.id.imgbtnBoardLook);
        ImgBtmBoardLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iPrev = new Intent(getApplicationContext(), Lookfar_activity.class);
                startActivity(iPrev);
                finish();
            }

        });

        ImageButton ImgBtmBoardMusic = findViewById(R.id.imgbtnBoardMusic);
        ImgBtmBoardMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iPrev = new Intent(getApplicationContext(), ambience.class);
                startActivity(iPrev);
                finish();
            }
        });


        //Get and setting Username
        TvName = findViewById(R.id.tvNameHome);
        String deviceName = Build.MODEL;
        TvName.setText(deviceName);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String phoneModel = sharedPreferences.getString("phoneModel", "");

        EditText ETNameHome = findViewById(R.id.etNameHome);

        if (phoneModel.isEmpty()) {
            String defaultPhoneModel = Build.MODEL;
            ETNameHome.setText(defaultPhoneModel);
        } else {
            ETNameHome.setText(phoneModel);
        }

        ETNameHome.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String newPhoneModel = ETNameHome.getText().toString();
                if (newPhoneModel.isEmpty()) {
                    newPhoneModel = Build.MODEL;
                    ETNameHome.setText(newPhoneModel);
                }
                sharedPreferences.edit().putString("phoneModel", newPhoneModel).apply();
                ETNameHome.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ETNameHome.getWindowToken(), 0);

                return true;
            }
            return false;
        });


        // Get the current date and display it in the TextView
        TextView tvDate = findViewById(R.id.tvDate);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
        tvDate.setText(currentDate);


        //Trivia pop-up dialog
        dlg = new Dialog(this, R.style.PopupDialog);
        BtnTrivia = findViewById(R.id.btnTrivia);
        BtnTrivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.setContentView(R.layout.popup);

                // Find the TextView in the popup layout
                TextView tvTrivia = dlg.findViewById(R.id.tvQuoteHolder);

                // Get an array of strings from the 'Quotes' string arrayresource
                String[] quotes = getResources().getStringArray(R.array.QuotesList);

                // Generate a random number between 0 and the length of the quotes array
                int randomIndex = new Random().nextInt(quotes.length);

                // Set the random quote to the TextView
                tvTrivia.setText(quotes[randomIndex]);
                dlg.show();
            }
        });

    }






    //Set Image Profile
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            int wakeCountToday = data.getIntExtra("wakeCountToday", 0);
            TextView tvScreenWakeCountHome = findViewById(R.id.tvScreenWakeCountHome);
            tvScreenWakeCountHome.setText(String.valueOf(wakeCountToday));
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of the selected image
            Uri imageUri = data.getData();

            try {
                // Load the image into a Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Crop the image into a circle
                Bitmap circleBitmap = getCircularBitmap(bitmap);

                // Set the cropped image as the new profile image
                imgviewProfile.setImageBitmap(circleBitmap);

                // Save the profile image to SharedPreferences
                saveProfileImage(circleBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Cut Image in Circle
    private Bitmap getCircularBitmap(Bitmap bitmap) {
        // Crop the bitmap to a square
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap squareBitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() - size) / 2, (bitmap.getHeight() - size) / 2, size, size);

        // Create a circular bitmap using the cropped bitmap
        Bitmap circleBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(squareBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);

        Canvas canvas = new Canvas(circleBitmap);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);

        return circleBitmap;
    }

    //Save Image
    private void saveProfileImage(Bitmap bitmap) {
        // Convert the Bitmap to a Base64-encoded string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        // Save the encoded image string to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_IMAGE_KEY, encodedImage);
        editor.apply();
    }

    private Bitmap loadProfileImage() {
        // Load the encoded image string from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String encodedImage = sharedPreferences.getString(PROFILE_IMAGE_KEY, null);

        if (encodedImage != null) {
            // Decode the Base64-encoded string back to a Bitmap
            byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            return bitmap;
        }

        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load the profile image from SharedPreferences
        Bitmap profileImage = loadProfileImage();
        if (profileImage != null) {
            imgviewProfile.setImageBitmap(profileImage);
        }
    }


    //Exit on BackPressed
    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed();
            return;
        }

        backPressedOnce = true;
        Toast.makeText(this, "Swipe again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 2000);


    }


    @Override
    protected void onStart() {

        //Go to "Previous" Activity
        BtnPrevStats = findViewById(R.id.btnViewDetails);
        BtnPrevStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iPrev = new Intent(getApplicationContext(), Previous.class);
                startActivity(iPrev);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                iPrev.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        super.onStart();


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start_time = calendar.getTimeInMillis();
        long end_time = System.currentTimeMillis(); // current time

        int unlockcount = getdailyUsageStatistics(start_time, end_time);
        Log.e("UNLOCK COUNT", unlockcount + " ==============");

        screentimeHome();

        //FOR NOTIFICATION
        // Check if the permission is granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed with creating the notification
            createNotificationChannel();

            //Toast.makeText(getApplicationContext(), String.valueOf(contextNotif), Toast.LENGTH_SHORT).show();


            String contentText = "";
            String fullContentText = "";
            if (contextNotif >= 40) {
                contentText = "Level 4 (Red) Significant distraction";
                fullContentText = "Level 4 (Red) Significant distraction" + "\n" + "\n" + "You’ve opened your phone more than 40 times today. That's quite a lot! It might be helpful to reassess your phone usage and find ways to reduce distractions. Remember to take breaks and find time for relaxation.";
            } else if (contextNotif >= 30 && contextNotif <= 39) {
                contentText = "Level 3 (Orange) Substantial distraction";
                fullContentText = "Level 3 (Orange) Substantial distraction" + "\n" + "\n" + "You’ve opened your phone less than 40 times today. You’re doing well! It's important to be mindful of your screen time, but you're still managing to stay productive. Keep up the effort!";
            } else if (contextNotif >= 11 && contextNotif <= 29) {
                contentText = "Level 2 (Blue) Moderate distraction";
                fullContentText = "Level 2 (Blue) Moderate distraction" + "\n" + "\n" + "Great job! You’ve opened your phone less than 30 times today. You're staying on track and maintaining a good balance. Keep it up and stay focused on your priorities.";
            } else if (contextNotif < 10) {
                contentText = "Level 1 (Green) Minimal distractions";
                fullContentText = "Level 1 (Green) Minimal distractions" + "\n" + "\n" + "You’re living in the moment! We admire your focus and mindfulness. You’ve opened your phone less than 10 times today. That’s amazing! You’re not letting distractions get in the way of your goals.";
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                    .setSmallIcon(R.drawable.splash_logo)
                    .setContentTitle("Your Distraction Level")
                    .setContentText(contentText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(fullContentText))
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());


        } else {
            // Permission is not granted, request the permission from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
        }
    }

    private int getdailyUsageStatistics(long start_time, long end_time) {

        int unlockcount = 0;

        UsageEvents.Event currentEvent;

        UsageStatsManager mUsageStatsManager = (UsageStatsManager)
                getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);

        if (mUsageStatsManager != null) {
            UsageEvents usageEvents = mUsageStatsManager.queryEvents(start_time, end_time);

            while (usageEvents.hasNextEvent()) {
                currentEvent = new UsageEvents.Event();
                usageEvents.getNextEvent(currentEvent);

                if (currentEvent.getEventType() == UsageEvents.Event.KEYGUARD_HIDDEN) {
                    ++unlockcount;
                    contextNotif = unlockcount;
                }
            }
            TextView tvWakeCount = findViewById(R.id.tvScreenWakeCountHome);
            tvWakeCount.setText(String.valueOf(unlockcount));


        } else {
            Toast.makeText(getApplicationContext(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }

        return unlockcount;
    }

    //Get Screentime in Home
    public void screentimeHome() {
        TextView tvScreenTimeHome = findViewById(R.id.tvScreenTimeHoursHome);

        Calendar calendar = Calendar.getInstance();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        // Set the start time to 12:00 AM of the current day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();

        // Set the end time to the current time
        long endTime = System.currentTimeMillis();

        // Get the app usage statistics for the current day
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        // Calculate the total screen time
        long totalScreenTime = 0;
        for (UsageStats usageStats : usageStatsList) {
            if (usageStats.getLastTimeUsed() >= startTime && usageStats.getLastTimeUsed() <= endTime &&
                    usageStats.getTotalTimeInForeground() > 0) {
                totalScreenTime += usageStats.getTotalTimeInForeground();
            }
        }

        // Display the total screen time in the respective TextView
        tvScreenTimeHome.setText(formatScreenTime(totalScreenTime));
    }


    private String formatScreenTime(long screenTime) {
        long hours = screenTime / (60 * 60 * 1000);
        long minutes = (screenTime % (60 * 60 * 1000)) / (60 * 1000);
        return String.format("%02dhr %02dmin", hours, minutes);
    }


/*
    //NOTIFICATION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, proceed with creating the notification
                createNotificationChannel();

                String contentText;
                if (contextNotif <= 10) {
                    contentText = "a";
                } else if (contextNotif <= 20) {
                    contentText = "b";
                } else if (contextNotif <= 30) {
                    contentText = "c";
                } else {
                    contentText = "d";
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                        .setSmallIcon(R.drawable.splash_logo)
                        .setContentTitle("Your Distraction Level")
                        .setContentText(contentText)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                notificationManager.notify(1, builder.build());
            } else {
                // Permission is not granted, handle the situation
                Toast.makeText(this, "Permission denied. Unable to create the notification.", Toast.LENGTH_SHORT).show();
            }
        }
    }*/


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}

