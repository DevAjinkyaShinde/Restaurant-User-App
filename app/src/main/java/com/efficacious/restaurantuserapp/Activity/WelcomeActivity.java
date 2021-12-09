package com.efficacious.restaurantuserapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;
import com.efficacious.restaurantuserapp.util.SharedPrefManger;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    Button button;
    ProgressBar loader;
    SharedPrefManger sharedPrefManger;
    CheckInternetConnection checkInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPrefManger = new SharedPrefManger(getApplicationContext());
        checkInternetConnection = new CheckInternetConnection(getApplicationContext());

        button = findViewById(R.id.btnStart);
        loader = findViewById(R.id.loader);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(WelcomeActivity.this,NoConnectionActivity.class));
            finish();
        }else {
            if (sharedPrefManger.isLoggedIn()){
                button.setVisibility(View.GONE);
                loader.setVisibility(View.VISIBLE);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                        finish();
                    }
                },2000);
            }
        }

    }
}