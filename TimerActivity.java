package com.example.projekti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class TimerActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private boolean isStart;
    public static int aika = 0;
    private static final String TAG = "TimerActivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        chronometer = findViewById(R.id.chronometer);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chronometer = chronometerChanged;
                aika++;
            }
        });

        backButton();
    }
    private void backButton() {
        Button backButton = (Button) findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(MainActivity.TAG, MODE_PRIVATE);

                // Saving to SharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("aika", aika);
                editor.commit();
                startActivity(new Intent(TimerActivity.this, MainActivity.class));
            }
        });
    }

    public void startStopChronometer(View view){
        if(isStart){
            chronometer.stop();
            isStart = false;
            ((Button)view).setText("Start");
            aika -= 2;
            Log.d("hello", "hello:" + aika);

        }else{
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isStart = true;
            ((Button)view).setText("Stop");
        }
    }


}
