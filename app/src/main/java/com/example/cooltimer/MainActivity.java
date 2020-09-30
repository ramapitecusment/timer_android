package com.example.cooltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    Context context;

    private TextView timerTextView;
    private SeekBar timerSeekBar;
    private ProgressBar timerProgressBar;
    private ImageView playImage;

    MediaPlayer player;
    private CountDownTimer timer;
    private SharedPreferences sharedPreferences;

    private int default_value_int;

    private int timerInt;
    private long secondsLeft;

    private boolean isPlaying = false;

    @SuppressLint("DefaultLocale")
    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        timerSeekBar =  findViewById(R.id.timerSeekBar);
        timerProgressBar = findViewById(R.id.timerProgressBar);
        playImage = findViewById(R.id.playImage);

        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        default_value_int = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("timer_interval", "30")));
//        default_value_int = Integer.parseInt("30");

        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(default_value_int);
        timerTextView.setText(String.format("%02d:%02d", default_value_int / 60, default_value_int % 60));
        seekBarListener();

        setIntervalFromSharedPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings_activity:
                Intent settingsIntent = new Intent(context, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.about_activity:
                Intent aboutIntent = new Intent(context, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("DefaultLocale")
    public void trashImageClick(View view) {
        timerStop();
        timerSeekBar.setProgress(default_value_int);
        timerTextView.setText(String.format("%02d:%02d", default_value_int / 60, default_value_int % 60));
}

    public void playImageClick(View view) {
        if (isPlaying){
            timerStop();
        } else {
            timerPlay();
        }
    }

    public void addTimeImageClick(View view) {
        timerStop();
        timerSeekBar.setProgress((int)secondsLeft+60);
        timerPlay();
    }

    private void createCountDownTimer(long milliseconds){
        timer = new CountDownTimer(milliseconds, 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long l) {
                secondsLeft = l/1000;
                String timeLeftText = String.format("%02d:%02d", l/1000/60, l/1000%60);
                timerTextView.setText(timeLeftText);
                int checkProgress = (timerInt-((int)l/1000))*100/timerInt;
//                Log.d("checkProgree", String.valueOf(checkProgress) + " " + timerInt + " " + l);
                timerProgressBar.setProgress(checkProgress);
            }

            @Override
            public void onFinish() {
                if (sharedPreferences.getBoolean("sound_enabled", false)){
                    switch (Objects.requireNonNull(sharedPreferences.getString("timer_melody", "bell"))){
                        case "alarm_siren":
                            player = MediaPlayer.create(context, R.raw.alarm_siren_sound);
                            break;
                        case "bip":
                            player = MediaPlayer.create(context, R.raw.bip_sound);
                            break;
                        default:
                            player = MediaPlayer.create(context, R.raw.bell_sound);
                            break;
                    }

                    player.start();
                }
                timerStop();
            }
        }.start();
    }

    private void timerPlay(){
        timerInt = timerSeekBar.getProgress();
        playImage.setImageResource(R.drawable.ic_baseline_pause_24);
        isPlaying = true;
        timerSeekBar.setEnabled(false);
        createCountDownTimer((long) timerInt*1000);
    }

    @SuppressLint("DefaultLocale")
    private void timerStop(){
        if(timer!=null) {
            playImage.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            int seek_bar_value = timerSeekBar.getProgress();
            isPlaying = false;
            timerSeekBar.setEnabled(true);
            timerProgressBar.setProgress(0);
            timerTextView.setText(String.format("%02d:%02d", seek_bar_value / 60, seek_bar_value % 60));
            timer.cancel();
        }
    }

    @SuppressLint("DefaultLocale")
    private void seekBarListener(){
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    String timerText = String.format("%02d:%02d", i/60, i%60);
                    timerTextView.setText(timerText);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @SuppressLint("DefaultLocale")
    private void setIntervalFromSharedPreferences(SharedPreferences sharedPreferences) {
        //TODO asdas
        int defaultInterval = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("timer_interval", "30")));
//        int defaultInterval = Integer.parseInt("30");
        Log.d("msgggg", ""+ defaultInterval);
        timerTextView.setText(String.format("%02d:%02d", defaultInterval / 60, defaultInterval % 60));
        timerSeekBar.setProgress(defaultInterval);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("timer_interval")) {
            setIntervalFromSharedPreferences(sharedPreferences);
        }
    }
}