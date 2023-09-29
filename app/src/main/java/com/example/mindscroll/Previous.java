package com.example.mindscroll;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Previous extends AppCompatActivity {

    //TextView tvPrevName;
    ImageButton imgbtnPrevBack;

    TextView tvPrevDate;
    TextView[] dateTextViews = new TextView[5];
    TextView[] appTextViews = new TextView[3];

    //save the time and set the color
    int color1;
    int color2;
    int color3;
    int color4;
    int color5;
    int color6;
    int color7;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title bar and set full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Do not sleep when the app is open
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_previous);

        //Fullscreen beyond punch hole camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        // Set the background to a drawable resource
        getWindow().setBackgroundDrawableResource(R.drawable.a_appbackground);


        //GIF Hello wave
        ImageView ImgviewPrevHello = findViewById(R.id.imgViewPrevHello);
        Glide.with(this).load(R.drawable.hello).into(ImgviewPrevHello);



        //Get and setting Username
        TextView tvPrevName = findViewById(R.id.tvPrevName);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String phoneModel = sharedPreferences.getString("phoneModel", "");
        tvPrevName.setText(phoneModel);



        // Get the current date and display it in the TextView
        tvPrevDate = findViewById(R.id.tvprevDate);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
        tvPrevDate.setText(currentDate);



        //On Back Action
        imgbtnPrevBack = findViewById(R.id.imgbtnPrevBack);
        imgbtnPrevBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iPrevBack = new Intent(getApplicationContext(), Home.class);

                startActivity(iPrevBack);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                iPrevBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });



        //Initializing Dates on TextViews
        // Get references to the TextViews for dates
        dateTextViews[0] = findViewById(R.id.tvday3);
        dateTextViews[1] = findViewById(R.id.tvday4);
        dateTextViews[2] = findViewById(R.id.tvday5);
        dateTextViews[3] = findViewById(R.id.tvday6);
        dateTextViews[4] = findViewById(R.id.tvday7);

        // Set the text for each date TextView
        for (int i = 0; i < dateTextViews.length; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -(2 + i)); // subtract 2+i days from today

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.US);
            String date = dateFormat.format(calendar.getTime());

            dateTextViews[i].setText(date);
        }



        //Initializing Top 3 most used Apps
        // Get references to the TextViews for apps
        appTextViews[0] = findViewById(R.id.tvApp1);
        appTextViews[1] = findViewById(R.id.tvApp2);
        appTextViews[2] = findViewById(R.id.tvApp3);

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, -1); // get usage stats for the last day

        // Sort the usage stats by total time spent in the app
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, calendar.getTimeInMillis(), System.currentTimeMillis());

        // Use a HashSet to store unique package names and their corresponding usage stats
        HashSet<String> uniquePackages = new HashSet<>();
        List<UsageStats> uniqueUsageStatsList = new ArrayList<>();

        for (UsageStats usageStats : usageStatsList) {
            String packageName = usageStats.getPackageName();
            if (!uniquePackages.contains(packageName)) {
                uniquePackages.add(packageName);
                uniqueUsageStatsList.add(usageStats);
            }
        }

        // Sort the unique usage stats by total time spent in the app
        Collections.sort(uniqueUsageStatsList, new Comparator<UsageStats>() {
            @Override
            public int compare(UsageStats usageStats1, UsageStats usageStats2) {
                return Long.compare(usageStats2.getTotalTimeInForeground(), usageStats1.getTotalTimeInForeground());
            }
        });

        // Set the text for the app usage TextViews
        for (int i = 0; i < Math.min(3, uniqueUsageStatsList.size()); i++) {
            UsageStats usageStats = uniqueUsageStatsList.get(i);

            String appName = getAppNameFromPackage(usageStats.getPackageName());
            long totalTime = usageStats.getTotalTimeInForeground() / (1000 * 60); // convert to minutes

            String appUsageText = String.format(Locale.getDefault(), "%02d" + "Hr " + "%02d" + " | " + "%s", totalTime / 60, totalTime % 60, appName);
            appTextViews[i].setText(appUsageText);
        }
    }

    //Rename Apps, removes unnecessary texts
    // Get the app name from the package name
    private String getAppNameFromPackage(String packageName) {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appName = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : null);
        if (appName == null) {
            appName = packageName;

            if (appName.startsWith("com.google.android.")) {
                appName = appName.substring("com.google.android.".length());
                Log.d("App Name Modification", "Removed 'com.google.android.' from app name");
            }

            if (appName.startsWith("com.google.")) {
                appName = appName.substring("com.google.".length());
                Log.d("App Name Modification", "Removed 'com.google.' from app name");
            }

            if (appName.startsWith("com.app.")) {
                appName = appName.substring("com.app.".length());
                Log.d("App Name Modification", "Removed 'com.app.' from app name");
            }

            if (appName.startsWith("com.")) {
                appName = appName.substring("com.".length());
                Log.d("App Name Modification", "Removed 'com.' from app name");
            }

            if (appName.startsWith("apps.")) {
                appName = appName.substring("apps.".length());
                Log.d("App Name Modification", "Removed 'apps.' from app name");
            }

            if (appName.startsWith("app.rvx.android.")) {
                appName = appName.substring("App.rvx.android.".length());
                Log.d("App Name Modification", "Removed 'App.rvx.android.' from app name");
            }

            if (appName.startsWith("android.")) {
                appName = appName.substring("android.".length());
                Log.d("App Name Modification", "Removed 'android.' from app name");
            }

            /*
            // Remove unnecessary text from the app name
            if (appName.startsWith("com.google.android.")) {
                appName = appName.substring("com.google.android.".length());
                Log.d("App Name Modification", "Removed 'com.google.android.' from app name");
            } else if (appName.startsWith("com.google.")) {
                appName = appName.substring("com.google.".length());
                Log.d("App Name Modification", "Removed 'com.google.' from app name");
            } else if (appName.startsWith("com.app.")) {
                appName = appName.substring("com.app.".length());
                Log.d("App Name Modification", "Removed 'com.app.' from app name");
            } else if (appName.startsWith("com.")) {
                appName = appName.substring("com.".length());
                Log.d("App Name Modification", "Removed 'com.' from app name");
            } else if (appName.startsWith("apps.")) {
                appName = appName.substring("apps.".length());
                Log.d("App Name Modification", "Removed 'apps.' from app name");
            }else if (appName.startsWith("App.rvx.android.")) {
                appName = appName.substring("App.rvx.android.".length());
                Log.d("App Name Modification", "Removed 'App.rvx.android.' from app name");
            }*/

            // Remove extra text after the app name
            int dotIndex = appName.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex > 0) {
                appName = appName.substring(0, dotIndex);
                Log.d("App Name Modification", "Removed extra text from app name");
            }


            //Capitalize first letter
            if (appName.length() > 0) {
                appName = appName.substring(0, 1).toUpperCase() + appName.substring(1);
            }

        } else {


            if (appName.startsWith("com.google.android.")) {
                appName = appName.substring("com.google.android.".length());
                Log.d("App Name Modification", "Removed 'com.google.android.' from app name");
            }

            if (appName.startsWith("com.google.")) {
                appName = appName.substring("com.google.".length());
                Log.d("App Name Modification", "Removed 'com.google.' from app name");
            }

            if (appName.startsWith("com.app.")) {
                appName = appName.substring("com.app.".length());
                Log.d("App Name Modification", "Removed 'com.app.' from app name");
            }

            if (appName.startsWith("com.")) {
                appName = appName.substring("com.".length());
                Log.d("App Name Modification", "Removed 'com.' from app name");
            }

            if (appName.startsWith("apps.")) {
                appName = appName.substring("apps.".length());
                Log.d("App Name Modification", "Removed 'apps.' from app name");
            }

            if (appName.startsWith("app.rvx.android.")) {
                appName = appName.substring("App.rvx.android.".length());
                Log.d("App Name Modification", "Removed 'App.rvx.android.' from app name");
            }

            if (appName.startsWith("android.")) {
                appName = appName.substring("android.".length());
                Log.d("App Name Modification", "Removed 'android.' from app name");
            }

            /*
            // Remove unnecessary text from the app name
            if (appName.startsWith("com.google.android.")) {
                appName = appName.substring("com.google.android.".length());
                Log.d("App Name Modification", "Removed 'com.google.android.' from app name");
            } else if (appName.startsWith("com.google.")) {
                appName = appName.substring("com.google.".length());
                Log.d("App Name Modification", "Removed 'com.google.' from app name");
            } else if (appName.startsWith("com.app.")) {
                appName = appName.substring("com.app.".length());
                Log.d("App Name Modification", "Removed 'com.app.' from app name");
            } else if (appName.startsWith("com.")) {
                appName = appName.substring("com.".length());
                Log.d("App Name Modification", "Removed 'com.' from app name");
            } else if (appName.startsWith("apps.")) {
                appName = appName.substring("apps.".length());
                Log.d("App Name Modification", "Removed 'apps.' from app name");
            }*/

            // Remove extra text after the app name
            int dotIndex = appName.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex > 0) {
                appName = appName.substring(0, dotIndex);
                Log.d("App Name Modification", "Removed extra text from app name");
            }

            //Capitalize first letter
            if (appName.length() > 0) {
                appName = appName.substring(0, 1).toUpperCase() + appName.substring(1);
            }
        }
        return appName;
    }



    //On Back Action
    public void onBackPressed(){
        Intent slideback = new Intent(this, Home.class);
        startActivity(slideback);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        slideback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }




    @Override
    protected void onStart() {
        super.onStart();

        //Setting Screen time on Textviews
        TextView tvScreenTime1 = findViewById(R.id.tvScreenTimeToday);
        TextView tvScreenTime2 = findViewById(R.id.screentimeday2);
        TextView tvScreenTime3 = findViewById(R.id.screentimeday3);
        TextView tvScreenTime4 = findViewById(R.id.screentimeday4);
        TextView tvScreenTime5 = findViewById(R.id.screentimeday5);
        TextView tvScreenTime6 = findViewById(R.id.screentimeday6);
        TextView tvScreenTime7 = findViewById(R.id.screentimeday7);

        Calendar calendar = Calendar.getInstance();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        for (int i = 0; i < 7; i++) {
            long endTime = calendar.getTimeInMillis();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            long startTime = calendar.getTimeInMillis();

            // Adjust start time to 12:00 AM
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startTime = calendar.getTimeInMillis();

            List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
            long totalScreenTime = 0;
            for (UsageStats usageStats : usageStatsList) {
                if (usageStats.getLastTimeUsed() >= startTime && usageStats.getLastTimeUsed() <= endTime &&
                        usageStats.getTotalTimeInForeground() > 0) {
                    totalScreenTime += usageStats.getTotalTimeInForeground();
                }
            }

            // Display the total screen time in the respective TextView
            switch (i) {
                case 0:
                    tvScreenTime1.setText(formatScreenTime(totalScreenTime));
                    break;
                case 1:
                    tvScreenTime2.setText(formatScreenTime(totalScreenTime));
                    break;
                case 2:
                    tvScreenTime3.setText(formatScreenTime(totalScreenTime));
                    break;
                case 3:
                    tvScreenTime4.setText(formatScreenTime(totalScreenTime));
                    break;
                case 4:
                    tvScreenTime5.setText(formatScreenTime(totalScreenTime));
                    break;
                case 5:
                    tvScreenTime6.setText(formatScreenTime(totalScreenTime));
                    break;
                case 6:
                    tvScreenTime7.setText(formatScreenTime(totalScreenTime));
                    break;
            }


        }


        // Calculate and display the total screen time for the current day (from 12am to current time)
        long endTime = System.currentTimeMillis();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> todayUsageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        long totalScreenTimeToday = 0;
        for (UsageStats usageStats : todayUsageStatsList) {
            if (usageStats.getLastTimeUsed() >= startTime && usageStats.getLastTimeUsed() <= endTime &&
                    usageStats.getTotalTimeInForeground() > 0) {
                totalScreenTimeToday += usageStats.getTotalTimeInForeground();
            }
        }

        tvScreenTime1.setText(formatScreenTime(totalScreenTimeToday));
        CalendarCounter();
    }

/*
    private void setColorIndicator(ImageView imageView, int value) {
        if (value > 10) {
            imageView.setImageResource(R.drawable.home_blueindicator);
        } else if (value > 20) {
            imageView.setImageResource(R.drawable.home_orangeindicator);
        } else if (value > 30) {
            imageView.setImageResource(R.drawable.home_redindicator);
        } else {
            imageView.setImageResource(R.drawable.home_greenindicator);
        }
    }*/




    private String formatScreenTime(long screenTime) {
        long hours = screenTime / (60 * 60 * 1000);
        long minutes = (screenTime % (60 * 60 * 1000)) / (60 * 1000);
        return String.format("%02dhr %02dmin", hours, minutes);
    }

    //Calendar day counter
    public void CalendarCounter(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start_time = calendar.getTimeInMillis();
        long end_time = System.currentTimeMillis(); // current time
        getdailyUsageStatistics0(start_time, end_time);


        // Get the unlock count for 2
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        end_time = start_time;
        start_time = calendar.getTimeInMillis();
        getdailyUsageStatistics1(start_time, end_time);

        // Get the unlock count for 2
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        end_time = start_time;
        start_time = calendar.getTimeInMillis();
        getdailyUsageStatistics2(start_time, end_time);

        // Get the unlock count for 3
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        end_time = start_time;
        start_time = calendar.getTimeInMillis();
        getdailyUsageStatistics3(start_time, end_time);

        // Get the unlock count for 4
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        end_time = start_time;
        start_time = calendar.getTimeInMillis();
        getdailyUsageStatistics4(start_time, end_time);

        // Get the unlock count for 5
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        end_time = start_time;
        start_time = calendar.getTimeInMillis();
        getdailyUsageStatistics5(start_time, end_time);

        // Get the unlock count for 6
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        end_time = start_time;
        start_time = calendar.getTimeInMillis();
        getdailyUsageStatistics6(start_time, end_time);
    }

    //Getting the data for each days
    private int getdailyUsageStatistics0(long start_time, long end_time) {

        int unlockcount = 0;
        TextView tvWakeCount = findViewById(R.id.tvWakeCountToday);


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
                }
            }

            tvWakeCount.setText(String.valueOf(unlockcount));
            color1 = unlockcount;

        } else {
            Toast.makeText(getApplicationContext(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }

        return unlockcount;

    }

    private int getdailyUsageStatistics1(long start_time, long end_time) {

        int unlockcount = 0;
        TextView tvWakeCountDay1 = findViewById(R.id.tvWakeCountDay1);


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
                }
            }

            tvWakeCountDay1.setText(String.valueOf(unlockcount));
            color2 = unlockcount;

        } else {
            Toast.makeText(getApplicationContext(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }

        return unlockcount;
    }

    private int getdailyUsageStatistics2(long start_time, long end_time) {

        int unlockcount = 0;
        TextView tvWakeCountDay2 = findViewById(R.id.tvWakeCountDay2);


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
                }
            }

            tvWakeCountDay2.setText(String.valueOf(unlockcount));
            color3 = unlockcount;

        } else {
            Toast.makeText(getApplicationContext(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }

        return unlockcount;
    }

    private int getdailyUsageStatistics3(long start_time, long end_time) {

        int unlockcount = 0;
        TextView tvWakeCountDay3 = findViewById(R.id.tvWakeCountDay3);


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
                }
            }

            tvWakeCountDay3.setText(String.valueOf(unlockcount));
            color4 = unlockcount;

        } else {
            Toast.makeText(getApplicationContext(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }

        return unlockcount;
    }

    private int getdailyUsageStatistics4(long start_time, long end_time) {

        int unlockcount = 0;
        TextView tvWakeCountDay4 = findViewById(R.id.tvWakeCountDay4);


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
                }
            }

            tvWakeCountDay4.setText(String.valueOf(unlockcount));
            color5 = unlockcount;

        } else {
            Toast.makeText(getApplicationContext(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }

        return unlockcount;
    }

    private int getdailyUsageStatistics5(long start_time, long end_time) {

        int unlockcount = 0;
        TextView tvWakeCountDay5 = findViewById(R.id.tvWakeCountDay5);


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
                }
            }

            tvWakeCountDay5.setText(String.valueOf(unlockcount));
            color6 = unlockcount;

        } else {
            Toast.makeText(getApplicationContext(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }

        return unlockcount;
    }

    private int getdailyUsageStatistics6(long start_time, long end_time) {

        int unlockcount = 0;
        TextView tvWakeCountDay6 = findViewById(R.id.tvWakeCountDay6);


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
                }
            }

            tvWakeCountDay6.setText(String.valueOf(unlockcount));
            color7 = unlockcount;
            ColorIndicator();

        } else {
            Toast.makeText(getApplicationContext(), "Could not load data.", Toast.LENGTH_SHORT).show();
        }

        return unlockcount;
    }

    public void ColorIndicator() {
        ImageView ImgD1ColorIndicator = findViewById(R.id.imgD1ColorIndicator);
        ImageView ImgD2ColorIndicator = findViewById(R.id.imgD2ColorIndicator);
        ImageView ImgD3ColorIndicator = findViewById(R.id.imgD3ColorIndicator);
        ImageView ImgD4ColorIndicator = findViewById(R.id.imgD4ColorIndicator);
        ImageView ImgD5ColorIndicator = findViewById(R.id.imgD5ColorIndicator);
        ImageView ImgD6ColorIndicator = findViewById(R.id.imgD6ColorIndicator);
        ImageView ImgD7ColorIndicator = findViewById(R.id.imgD7ColorIndicator);

        //Toast.makeText(getApplicationContext(), String.valueOf(color2), Toast.LENGTH_SHORT).show();

        if (color1 >= 40) {
            ImgD1ColorIndicator.setImageResource(R.drawable.home_redindicator);
        } else if (color1 >= 30 && color1 <= 39) {
            ImgD1ColorIndicator.setImageResource(R.drawable.home_orangeindicator);
        } else if (color1 >= 11 && color1 <= 29) {
            ImgD1ColorIndicator.setImageResource(R.drawable.home_blueindicator);
        } else if (color1 < 10) {
            ImgD1ColorIndicator.setImageResource(R.drawable.home_greenindicator);
        }

        if (color2 >= 40) {
            ImgD2ColorIndicator.setImageResource(R.drawable.home_redindicator);
        } else if (color2 >= 30 && color2 <= 39) {
            ImgD2ColorIndicator.setImageResource(R.drawable.home_orangeindicator);
        } else if (color2 >= 11 && color2 <= 29) {
            ImgD2ColorIndicator.setImageResource(R.drawable.home_blueindicator);
        } else if (color2 < 10) {
            ImgD2ColorIndicator.setImageResource(R.drawable.home_greenindicator);
        }

        if (color3 >= 40) {
            ImgD3ColorIndicator.setImageResource(R.drawable.home_redindicator);
        } else if (color3 >= 30 && color3 <= 39) {
            ImgD3ColorIndicator.setImageResource(R.drawable.home_orangeindicator);
        } else if (color3 >= 11 && color3 <= 29) {
            ImgD3ColorIndicator.setImageResource(R.drawable.home_blueindicator);
        } else if (color3 < 10) {
            ImgD3ColorIndicator.setImageResource(R.drawable.home_greenindicator);
        }

        if (color4 >= 40) {
            ImgD4ColorIndicator.setImageResource(R.drawable.home_redindicator);
        } else if (color4 >= 30 && color4 <= 39) {
            ImgD4ColorIndicator.setImageResource(R.drawable.home_orangeindicator);
        } else if (color4 >= 11 && color4 <= 29) {
            ImgD4ColorIndicator.setImageResource(R.drawable.home_blueindicator);
        } else if (color4 < 10) {
            ImgD4ColorIndicator.setImageResource(R.drawable.home_greenindicator);
        }

        if (color5 >= 40) {
            ImgD5ColorIndicator.setImageResource(R.drawable.home_redindicator);
        } else if (color5 >= 30 && color5 <= 39) {
            ImgD5ColorIndicator.setImageResource(R.drawable.home_orangeindicator);
        } else if (color5 >= 11 && color5 <= 29) {
            ImgD5ColorIndicator.setImageResource(R.drawable.home_blueindicator);
        } else if (color5 < 10) {
            ImgD5ColorIndicator.setImageResource(R.drawable.home_greenindicator);
        }

        if (color6 >= 40) {
            ImgD6ColorIndicator.setImageResource(R.drawable.home_redindicator);
        } else if (color6 >= 30 && color6 <= 39) {
            ImgD6ColorIndicator.setImageResource(R.drawable.home_orangeindicator);
        } else if (color6 >= 11 && color6 <= 29) {
            ImgD6ColorIndicator.setImageResource(R.drawable.home_blueindicator);
        } else if (color6 < 10) {
            ImgD6ColorIndicator.setImageResource(R.drawable.home_greenindicator);
        }

        if (color7 >= 40) {
            ImgD7ColorIndicator.setImageResource(R.drawable.home_redindicator);
        } else if (color7 >= 30 && color7 <= 39) {
            ImgD7ColorIndicator.setImageResource(R.drawable.home_orangeindicator);
        } else if (color7 >= 11 && color7 <= 29) {
            ImgD7ColorIndicator.setImageResource(R.drawable.home_blueindicator);
        } else if (color7 < 10) {
            ImgD7ColorIndicator.setImageResource(R.drawable.home_greenindicator);
        }



    }


}