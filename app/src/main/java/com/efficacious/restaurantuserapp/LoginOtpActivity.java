package com.efficacious.restaurantuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    Button mBtnVerifyOtp;
    EditText mGetOtp;
    String MobileNumber,OtpId,Name;
    TextView mMobileNumberTxt;

    public int counter;

//    FirebaseAuth firebaseAuth;
//    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        mBtnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        mGetOtp = findViewById(R.id.getOtp);
        mMobileNumberTxt = findViewById(R.id.mobileNumberTxt);
        progressBar = findViewById(R.id.loader);

//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseFirestore = FirebaseFirestore.getInstance();

        MobileNumber = getIntent().getStringExtra("MobileNumber");
        mMobileNumberTxt.setText("Otp send on your " + MobileNumber);
//        InitiateOtp();

        mBtnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGetOtp.getText().toString().isEmpty()){
                    mGetOtp.setError("Please enter otp");
                }else if (mGetOtp.getText().toString().length()!=6){
                    mGetOtp.setError("Short OTP");
                }else {
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OtpId,mGetOtp.getText().toString());
//                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

//    private void InitiateOtp() {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                MobileNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        OtpId = s;
//                    }
//
//                    @Override
//                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                        signInWithPhoneAuthCredential(phoneAuthCredential);
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                        Toast.makeText(LoginOtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });        // OnVerificationStateChangedCallbacks
//
//    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mBtnVerifyOtp.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        progressBar.setVisibility(View.INVISIBLE);
//                        Intent intent = new Intent(LoginOtpActivity.this, MainActivity.class);
//                        intent.putExtra("Mobile",MobileNumber);
//                        startActivity(intent);
//                        finish();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                progressBar.setVisibility(View.INVISIBLE);
//                mBtnVerifyOtp.setVisibility(View.VISIBLE);
////                            Toast.makeText(UserLoginOtp.this, "Error...", Toast.LENGTH_SHORT).show();
//                mGetOtp.setError("Incorrect OTP");
//            }
//        });
//    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView textView = findViewById(R.id.timer);
        new CountDownTimer(50000, 1500){
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished){
                long sec = (millisUntilFinished / 1000) % 60;
                textView.setText(String.valueOf(sec) + " Sec");

            }
            @SuppressLint("SetTextI18n")
            public  void onFinish(){
                textView.setText("Resend OTP?");

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LoginOtpActivity.this,LoginActivity.class));
                        finish();
                    }
                });
            }
        }.start();
    }

}