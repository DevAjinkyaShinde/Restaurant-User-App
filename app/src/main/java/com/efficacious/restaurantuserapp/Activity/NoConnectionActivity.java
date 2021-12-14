package com.efficacious.restaurantuserapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;

import java.util.Timer;
import java.util.TimerTask;

public class NoConnectionActivity extends AppCompatActivity {

    CheckInternetConnection checkInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        checkInternetConnection = new CheckInternetConnection(getApplicationContext());

        findViewById(R.id.btnTryAgain)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkInternetConnection.isConnectingToInternet()){
                            startActivity(new Intent(NoConnectionActivity.this,WelcomeActivity.class));
                            finish();
                        }else {
                            LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
                            lottieAnimationView.playAnimation();
                        }
                    }
                });

        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}