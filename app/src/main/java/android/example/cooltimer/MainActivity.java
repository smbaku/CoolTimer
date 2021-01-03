package android.example.cooltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public SeekBar seekBar;
    private TextView textView;
    private Button button;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;
    private boolean isTimeOn;
   // private int defaultInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        seekBar = findViewById(R.id.seekBarMy);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);


        seekBar.setMax(300);
        seekBar.setProgress(30);
        isTimeOn = false;


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                manageTime(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    public void start(View view) {

        if (!isTimeOn) {

            runningTimer();


        } else {
            defaultTimeSettings();


        }


    }

    public void runningTimer() {
        button.setText("STOP");
        seekBar.setEnabled(false);
        isTimeOn = true;
        countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000, 1000) {
            @Override
            public void onTick(long progress) {
                progress = progress / 1000L;
                manageTime(progress);

            }

            @Override
            public void onFinish() {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                if (sharedPreferences.getBoolean("enable_sound", true)) {

                    String melodyName = sharedPreferences.getString("timer_melody", "bell");
                    if (melodyName.equals("moon")) {
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.moon);
                        mediaPlayer.start();
                    } else if (melodyName.equals("alarm")) {
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                        mediaPlayer.start();
                    } else if (melodyName.equals("brip")) {
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.brip);
                        mediaPlayer.start();
                    }


                }
                defaultTimeSettings();

            }
        };
        countDownTimer.start();
    }

    public void defaultTimeSettings() {
        countDownTimer.cancel();
        textView.setText("00:30");
        button.setText("START");
        seekBar.setEnabled(true);
        seekBar.setProgress(30);
        isTimeOn = false;
    }

    public void manageTime(long progress) {
        int minutes = (int) progress / 60;

        int seconds = (int) progress - (minutes * 60);


        String minString = "";
        String secString = "";


        if (minutes < 10) {
            minString = "0" + minutes;
        } else {
            minString = String.valueOf(minutes);

        }

        if (seconds < 10) {
            secString = "0" + seconds;

        } else {
            secString = String.valueOf(seconds);
        }

        textView.setText(minString + ":" + secString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true;
        } else {
            if (id == R.id.action_about) {
                Intent openAbout = new Intent(this, AboutActivity.class);
                startActivity(openAbout);
                return true;
            }


            return super.onOptionsItemSelected(item);
        }
    }

 /*   public void setIntervalFromShearedPreferences(SharedPreferences interval) {

        defaultInterval = Integer.valueOf( interval.getString("default_interval", "30"));
        textView.setText("00 :" + defaultInterval);
        seekBar.setProgress(defaultInterval);
    }*/


}



