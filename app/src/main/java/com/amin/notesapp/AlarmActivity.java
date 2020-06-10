package com.amin.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class AlarmActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    TextView txtTitle;
    MainActivity mainActivity;
    Button btnStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setUpView();

    }

    private void setUpView() {
        changeStatusBarColor("#42A5F5");
        txtTitle = (TextView)findViewById(R.id.txt_alarm_title);
        mainActivity = new MainActivity();
        String text = Objects.requireNonNull(getIntent().getExtras()).getString("title");
        txtTitle.setText(text);

        mediaPlayer = MediaPlayer.create(AlarmActivity.this,R.raw.wakeup);
        mediaPlayer.start();
        btnStop = (Button)findViewById(R.id.btn_alarm_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
            }
        });
    }

    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
}
