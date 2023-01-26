package com.treasurehunter.lnf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    SessionManager sessionManager;
    ImageView img;
    TextView TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_splash_screen);

        sessionManager = new SessionManager(getApplicationContext());

        img = findViewById(R.id.imgSplash);
        TV  = findViewById(R.id.idTV);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        img.startAnimation(anim);
        TV.startAnimation(anim2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

                if(isFirstTime){

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent aftersplash = new Intent(SplashScreen.this,OnBoarding.class);
                    startActivity(aftersplash);
                } else {
                    //Intent aftersplash = new Intent(SplashScreen.this, LoginActivity.class);
                    //startActivity(aftersplash);
                    sessionManager.checkLogin2();
                }
                finish();
            }
        },500);


        /*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //startActivity(new Intent(getApplicationContext(), OnBoarding.class));
                sessionManager.checkLogin();
                finish();


            }
        }, 500);*/ //1000 L = 5 detik
    }
}