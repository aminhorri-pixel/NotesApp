package com.amin.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class SplashScreenActivity extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setUpView();

    }

    private void setUpView() {

        changeStatusBarColor("#42A5F5");
        getScreen();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getScreen()<5){
                    startActivity(new Intent(SplashScreenActivity.this,LowScreenActivity.class));
                    timer.cancel();
                    finish();
                }else {

                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    timer.cancel();
                    finish();
                }
            }
        }, 3000, 1000);

    }

    private void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public double getScreen() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        double x = Math.pow(screenWidth / dm.xdpi, 2);
        double y = Math.pow(screenHeight / dm.xdpi, 2);

        double screenInches = Math.sqrt(x+y);

        screenInches = (double)Math.round(screenInches*10)/10;
        return screenInches;
    }
}
