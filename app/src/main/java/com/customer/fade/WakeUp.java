package com.customer.fade;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WakeUp extends AppCompatActivity {
    Timer timer;
    RelativeLayout relativeLayout;
    ToggleButton toggleButton;
//    AnimatorSet mAnimationSet;
//    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;

    long live;
    long fadeLevel;
    long intervalLevel;
    long fade ;
    boolean first = true;
    long delayTime;
    private int soundID;
    final int[] music = {R.raw.birds,R.raw.river};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wake_up);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initWidgets();
        getPreference();

        mediaPlayer.setLooping(true);
        mediaPlayer.start();

//        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                soundPool.play(sampleId,1f,1f,status,-1,1);
//            }
//        });


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(fade);
//        alphaAnimation.setRepeatCount(1);
//        alphaAnimation.setRepeatMode(Animation.REVERSE);
        relativeLayout.startAnimation(alphaAnimation);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mediaPlayer.stop();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, 600000);



//
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(relativeLayout, "alpha", 0.000f, 1.000f);
//        fadeIn.setInterpolator(new DecelerateInterpolator());
////        fadeIn.setStartDelay(5000);
//        fadeIn.setDuration(fade);
//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(relativeLayout, "alpha",  1f, 0.0f);
//        fadeOut.setInterpolator(new AccelerateInterpolator());
//        fadeOut.setStartDelay(1000);
//        fadeOut.setDuration(1000);
//        ObjectAnimator peak = ObjectAnimator.ofFloat(relativeLayout, "alpha",  1.0f, 1.0f);
//        peak.setInterpolator(new AccelerateInterpolator());
//        peak.setDuration(1000);
//        ObjectAnimator delay = ObjectAnimator.ofFloat(relativeLayout, "alpha", 0f, 0f);
//        delay.setDuration(5000);

//        mAnimationSet = new AnimatorSet();
//        mAnimationSet.play(fadeIn);
       // mAnimationSet.playSequentially( fadeIn, fadeOut);
//        mAnimationSet.play(fadeOut).after(fadeIn);
//        mAnimationSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//
//                live += fadeIn.getDuration() + fadeOut.getDuration();
//                if (first) {
//                    fadeIn.setStartDelay(15000);
//                    first = false;
//                }else {
////                if(fadeLevel!=0) {
////                    if (fadeLevel == 1) {
////                        fade = (long) (fade / 1.5f);
////                    } else {
////                        fade /= (fadeLevel);
////                    }
////                }
////                    if (fade < 1000) {
////                        fade = 1000;
////                    }
////                    fadeIn.setDuration(fade);
//                    if (intervalLevel != 0) {
//                        if (intervalLevel == 1) {
//                            delayTime = (long) (fadeIn.getStartDelay() / 1.5f);
//                        } else {
//                            delayTime = fadeIn.getStartDelay() / intervalLevel;
//                        }
//                        fadeIn.setStartDelay(delayTime);
//                        delayTime = fadeIn.getStartDelay();
//                        delayTime = fadeOut.getStartDelay();
//                    }
//                }
//                    if (fadeIn.getStartDelay() <= 1 || live > 600000) {
//                        relativeLayout.setAlpha(1f);
//                        soundPool.stop(soundID);
//                        mAnimationSet.removeAllListeners();
////                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                        startActivity(intent);
//                    }
//                    if (fadeIn.getStartDelay() > 1) {
//                        mAnimationSet.start();
//                    }
//                }
//        });
//        if(fadeIn.getStartDelay() >300){
//            mAnimationSet.start();
//        }

        }


    public void initWidgets(){
        relativeLayout= findViewById(R.id.layoutWakeUp);
        //relativeLayout.setAlpha(0f);
        toggleButton = findViewById(R.id.on_off);
//        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mediaPlayer = new MediaPlayer();
        timer = new Timer();
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
//        soundPool.stop(soundID);
//        soundPool.release();
        mediaPlayer.stop();
//        mAnimationSet.removeAllListeners();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    public void pause(View view) {
        if(toggleButton.isChecked()){
            mediaPlayer.pause();
//            soundPool.pause(soundID);
        }
        if (!toggleButton.isChecked()){
            mediaPlayer.start();
//            soundPool.resume(soundID);
        }
    }

    private void getPreference() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        if (sp.contains("fadeInLevels")) {
//            fadeLevel = Long.parseLong(sp.getString("fadeInLevels", "1"));
//        }
//        if (sp.contains("dayIntervalLevels")) {
//            intervalLevel = Long.parseLong(sp.getString("dayIntervalLevels", "1"));
//        }
        if (sp.contains("fadeInTime")) {
            fade = Long.parseLong(sp.getString("fadeInTime", null));
        }
        if (sp.contains("wake_up_sounds")) {
            if (sp.getString("wake_up_sounds", null).equals("2")) {
                mediaPlayer = MediaPlayer.create(this,music[1]);
//                soundID = soundPool.load(this, music[1],1);
            } else if (sp.getString("wake_up_sounds", null).equals("1")) {
                mediaPlayer = MediaPlayer.create(this,music[0]);
//                soundID = soundPool.load(this, music[0],1);
            }
        }
        if(sp.contains("backGround")){
            if (sp.getString("backGround", null).equals("1")) {
                relativeLayout.setBackgroundResource(R.drawable.cyan);
            } else if (sp.getString("backGround", null).equals("2")) {
                relativeLayout.setBackgroundResource(R.drawable.turquoise);
            }
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
//        soundPool.autoPause();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mAnimationSet.pause();
//        }
    }

    @Override
    protected void onResume(){
        super.onResume();
//        soundPool.autoResume();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mAnimationSet.resume();
//        }
    }
    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        soundPool.release();
//        soundPool = null;
//        mAnimationSet.removeAllListeners();
//        mAnimationSet = null;
    }

}
