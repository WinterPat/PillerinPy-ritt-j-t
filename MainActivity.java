package com.example.projekti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView tv1;
    DatabaseHelper myDB;
    Button btnAdd,btnView;
    public static final String TAG = "MainActivity: ";

    private Chronometer chronometer;
    private boolean isStart;
    public static int aika = 0;

    // Users data
    public double paino = 70;
    public double pituus = 180;
    public int ika = 30;
    public String sukupuoli = "Nainen";


    private void updateUI(){
        tv1 =(TextView) findViewById(R.id.textView);
        tv1.setText(secToTime(aika));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnView = (Button) findViewById(R.id.btnView);
        myDB = new DatabaseHelper(this);

        //Painoindeksi
        final double BMI = paino/((pituus/100)*(pituus/100));

        //Lepoaineenvaihdunnat sekunnissa
        final double miehenLepo = (66.47 + (13.75 * paino) + (5 * pituus) - (6.76 * ika))/86400.0;
        final double naisenLepo = (65.51 + (9.56 * paino) + (1.85 * pituus) - (4.68 * ika))/86400.0;

        //kalorikulutus sekunnissa
        final double kävely = 250.0/3600.0;
        final double pyöräily = 295.0/3600.0;
        final double juokseminen = 710.0/3600.0;

        chronometer = findViewById(R.id.chronometer);

        //Ajastin
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chronometer = chronometerChanged;
                aika++;
                updateUI();
            }
        });
        updateUI();

        //Button that adds data to the list
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                DecimalFormat df = new DecimalFormat("0.00");

                //Aikaleima
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                String format = simpleDateFormat.format(new Date());
                Log.d("MainActivity", "Timestamp: " + format);

                //Kalorikulutus kerrottuna ajalla
                double naisenKaloritKulutus = naisenLepo * aika + pyöräily * aika;
                double miehenKaloriKulutus = miehenLepo * aika + pyöräily * aika;

                if(sukupuoli == "Mies") {
                    String newEntry ="Päiväys: " + format + "\nTreenin pituus: " + secToTime(aika) +
                             "\nKalorikulutus: " + df.format(miehenKaloriKulutus) + " kcal.";
                    if (newEntry.length() != 0) {
                        AddData(newEntry);
                        aika = 0;
                        updateUI();
                    } else {
                        Toast.makeText(MainActivity.this, "You must put something in the text field!", Toast.LENGTH_LONG).show();
                    }
                }else if(sukupuoli == "Nainen"){
                    String newEntry = "Päiväys: " + format + "\nTreenin pituus: " + secToTime(aika) +
                            "\nKalorikulutus: " + df.format(naisenKaloritKulutus) + " kcal.";
                    if (newEntry.length() != 0) {
                        AddData(newEntry);
                        aika = 0;
                        updateUI();
                    } else {
                        Toast.makeText(MainActivity.this, "You must put something in the text field!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //Button that changes the view to ViewListContents
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewListContents.class);
                startActivity(intent);
            }
        });

    }

    public void startStopChronometer(View view){
        if(isStart){
            chronometer.stop();
            isStart = false;
            ((Button)view).setText("Start");
            Log.d("hello", "hello:" + aika);
            updateUI();

        }else{
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isStart = true;
            ((Button)view).setText("Stop");
            aika = 0;
            updateUI();

        }
    }

    //Checking the data
    public void AddData(String newEntry) {

        boolean insertData = myDB.addData(newEntry);

        if(insertData==true){
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }

    //Converting seconds to hour, minute and second format
    public String secToTime(int aika) {

        int seconds = aika % 60;
        int minutes = aika / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if( hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days,hours%24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("00:%02d:%02d", minutes, seconds);
    }
}
