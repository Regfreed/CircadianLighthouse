package com.customer.fade;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class Sleep extends AppCompatActivity {
    public RelativeLayout relativeLayout;
    private SoundPool soundPool;
    ToggleButton toggleButton;
    AnimatorSet mAnimationSet;

    long live = 0;
    long delayTime = 5000;
    long fadeLevel;
    long intervalLevel;
    long interval;
    long lastFade = 1000;
    private int soundID;
    final int[] music = {R.raw.strong_waves, R.raw.waves};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initWidgets();
        getPreference();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1f,1f, status, -1, 1);
            }
        });
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(relativeLayout, "alpha", 0.0f, 1f);
        fadeIn.setDuration(1000);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(relativeLayout, "alpha", 1f, 0.0f);
        fadeOut.setDuration(1000);
        ObjectAnimator peak = ObjectAnimator.ofFloat(relativeLayout, "alpha", 1f, 1f);
        peak.setDuration(1000);
        ObjectAnimator delay = ObjectAnimator.ofFloat(relativeLayout, "alpha", 0f, 0f);
        delay.setDuration(delayTime);


        mAnimationSet = new AnimatorSet();
        mAnimationSet.playSequentially(delay, fadeIn, peak, fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
                live += fadeIn.getDuration() + peak.getDuration() + fadeOut.getDuration() + delay.getDuration();
                lastFade *= fadeLevel;
                interval = 1000 * intervalLevel;
                fadeOut.setDuration(fadeOut.getDuration() + lastFade);
                delay.setDuration(delay.getDuration() + interval);
                if(delay.getDuration() >60000 || fadeOut.getDuration() > 120000 || live > 900000){
                   soundPool.stop(soundID);
                    mAnimationSet.removeAllListeners();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        mAnimationSet.start();

    }

    private void initWidgets() {
        relativeLayout = findViewById(R.id.layoutSleep);
        toggleButton = findViewById(R.id.on_off_sleep);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {

        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }


    public void back(View view) {
        soundPool.stop(soundID);
        soundPool.release();
        mAnimationSet.removeAllListeners();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    public void pauseSleep(View view) {
        if (toggleButton.isChecked()) {
            soundPool.pause(soundID);
        }
        if (!toggleButton.isChecked()) {
            soundPool.resume(soundID);
        }
    }

    private void getPreference() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains("fadeOutLevels")) {
            fadeLevel = Long.parseLong(sp.getString("fadeOutLevels", "1"));
        }
        if (sp.contains("nightIntervalLevels")) {
            intervalLevel = Long.parseLong(sp.getString("nightIntervalLevels", "1"));
        }
        if (sp.contains("sleep_sounds")) {
            if (sp.getString("sleep_sounds", null).equals("2")) {
               soundID = soundPool.load(this, music[1],1);
            } else if (sp.getString("sleep_sounds", null).equals("1")) {
               soundID = soundPool.load(this, music[0],1);
            }
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
        soundPool.autoPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimationSet.pause();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        soundPool.autoResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimationSet.resume();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        soundPool.release();
        soundPool = null;
        mAnimationSet.removeAllListeners();
        mAnimationSet = null;
    }


}