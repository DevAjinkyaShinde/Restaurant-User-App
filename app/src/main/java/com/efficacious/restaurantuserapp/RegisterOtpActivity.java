package com.efficacious.restaurantuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterOtpActivity extends AppCompatActivity {

    Button mBtnVerifyOtp;
    EditText mGetOtp;
    TextView mMobileNumberTxt;
    String MobileNumber,OtpId,Name;
    ProgressDialog progressDialog;

//    FirebaseAuth firebaseAuth;
//    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otp);

        mMobileNumberTxt = findViewById(R.id.mobileNumberTxt);
        mBtnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        mGetOtp = findViewById(R.id.getOtp);
        progressBar = findViewById(R.id.loader);

//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        MobileNumber = getIntent().getStringExtra("MobileNumber");
        mMobileNumberTxt.setText("Otp send on your " + MobileNumber );
        Name = getIntent().getStringExtra("Name");
//        InitiateOtp();

        mBtnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGetOtp.getText().toString().isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                    mGetOtp.setError("Please enter otp");
                }else if (mGetOtp.getText().toString().length()!=6){
//                    Toast.makeText(getApplicationContext(), "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    mGetOtp.setError("Short OTP");
                }else {
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OtpId,mGetOtp.getText().toString());
//                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

//    private void InitiateOtp() {
//        progressDialog.dismiss();
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
//                        progressDialog.dismiss();
//                        Toast.makeText(RegisterOtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
//                        if (task.isSuccessful()) {
//                            HashMap<String, Object> map = new HashMap<>();
//                            map.put("MobileNumber",MobileNumber);
//                            map.put("UserId",firebaseAuth.getCurrentUser().getUid());
//                            map.put("UserName",Name);
//                            firebaseFirestore.collection("MobileNumber").document(firebaseAuth.getCurrentUser().getUid())
//                                    .set(map);
//                            firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
//                                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    progressDialog.dismiss();
//                                    progressBar.setVisibility(View.INVISIBLE);
//                                    Intent intent = new Intent(RegisterOtpActivity.this, MainActivity.class);
//                                    intent.putExtra("Mobile",MobileNumber);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            });
//                        } else {
//                            progressDialog.dismiss();
//                            progressBar.setVisibility(View.INVISIBLE);
//                            mBtnVerifyOtp.setVisibility(View.VISIBLE);
////                            Toast.makeText(UserLoginOtp.this, "Error...", Toast.LENGTH_SHORT).show();
//                            mGetOtp.setError("Incorrect OTP");
//                        }
//                    }
//                });
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
                        startActivity(new Intent(RegisterOtpActivity.this,LoginActivity.class));
                        finish();
                    }
                });
            }
        }.start();
    }

}