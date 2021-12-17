package com.efficacious.restaurantuserapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Model.GetUserDetailResponse;
import com.efficacious.restaurantuserapp.Model.GetUserDetails;
import com.efficacious.restaurantuserapp.Model.RegisterUserDetails;
import com.efficacious.restaurantuserapp.Model.UserDetail;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;
import com.efficacious.restaurantuserapp.util.SharedPrefManger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterOtpActivity extends AppCompatActivity {

    Button mBtnVerifyOtp;
    EditText mGetOtp;
    TextView mMobileNumberTxt;
    String MobileNumber,OtpId,Name,WithoutCCMobile;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBar;
    SharedPrefManger sharedPrefManger;
    CheckInternetConnection checkInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otp);

        TextView textView = findViewById(R.id.timer);
        countDown(textView);

        sharedPrefManger = new SharedPrefManger(getApplicationContext());
        checkInternetConnection = new CheckInternetConnection(getApplicationContext());

        mMobileNumberTxt = findViewById(R.id.mobileNumberTxt);
        mBtnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        mGetOtp = findViewById(R.id.getOtp);
        progressBar = findViewById(R.id.loader);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        MobileNumber = getIntent().getStringExtra("MobileNumber");
        WithoutCCMobile = getIntent().getStringExtra("WithoutCCMobile");
        mMobileNumberTxt.setText("Otp send on your " + MobileNumber );
        Name = getIntent().getStringExtra("Name");
        InitiateOtp();

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
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OtpId,mGetOtp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void InitiateOtp() {
        progressDialog.dismiss();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                MobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        OtpId = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterOtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mBtnVerifyOtp.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("MobileNumber",MobileNumber);
                            map.put("Name",Name);
                            map.put("WithoutCCMobile",WithoutCCMobile);

                            firebaseFirestore.collection("UserData")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .set(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                registerUser(MobileNumber,Name,WithoutCCMobile);
                                            }
                                        }
                                    });

                        } else {
                            progressDialog.dismiss();
                            progressBar.setVisibility(View.INVISIBLE);
                            mBtnVerifyOtp.setVisibility(View.VISIBLE);
                            mGetOtp.setError("Incorrect OTP");
                        }
                    }
                });
    }

    private void registerUser(String mobileNumber, String name, String withoutCCMobile) {

        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(RegisterOtpActivity.this,NoConnectionActivity.class));
            finish();
        }else {
            RegisterUserDetails registerUserDetails = new RegisterUserDetails(name,null,null,withoutCCMobile,null,null,null,null,1,null,null);

            try {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .registerUser("insert",registerUserDetails);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            //This function : Add data on shared pref
                            addDataOnSharedPref(withoutCCMobile);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){

            }
        }
    }

    private void addDataOnSharedPref(String withoutCCMobile) {

        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(RegisterOtpActivity.this,NoConnectionActivity.class));
            finish();
        }else{
            try {
                Call<GetUserDetailResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .getUserDetails("select","1",withoutCCMobile);


                call.enqueue(new Callback<GetUserDetailResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                        if (response.isSuccessful()){
                            List<GetUserDetails> getUserDetails = response.body().getGetUserDetails();

                            UserDetail userDetail = new UserDetail(getUserDetails.get(0).getRegisterId(),getUserDetails.get(0).getMobileNo(),
                                    true,getUserDetails.get(0).getFirstName(),"",withoutCCMobile);
                            List<UserDetail> userDetails = new ArrayList<>();
                            userDetails.addAll(Collections.singleton(userDetail));
                            sharedPrefManger.saveUserDetail(userDetails);
                            Intent intent = new Intent(RegisterOtpActivity.this, MainActivity.class);
                            intent.putExtra("Mobile",MobileNumber);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

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

    private void countDown(TextView textView) {
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
                        resendOtp(textView);
                    }
                });
            }
        }.start();
    }

    private void resendOtp(TextView textView) {
        InitiateOtp();
        countDown(textView);
        Toast.makeText(RegisterOtpActivity.this, "OTP resend..", Toast.LENGTH_SHORT).show();
    }

}