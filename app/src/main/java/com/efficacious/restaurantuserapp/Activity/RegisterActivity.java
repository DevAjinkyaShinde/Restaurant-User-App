package com.efficacious.restaurantuserapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {

    CountryCodePicker mCpp;
    EditText mMobileNumber,mName;
    Button mBtnContinue;
    TextView mBtnLogin;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseFirestore = FirebaseFirestore.getInstance();

        mCpp = findViewById(R.id.cpp);
        mMobileNumber = findViewById(R.id.mobileNumber);
        mName = findViewById(R.id.name);
        mBtnContinue = findViewById(R.id.btnContinue);
        mBtnLogin = findViewById(R.id.btnLogin);

        mCpp.registerCarrierNumberEditText(mMobileNumber);
        ProgressBar progressBar = findViewById(R.id.loader);
        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String mobileNumber = mMobileNumber.getText().toString();
                if (TextUtils.isEmpty(mobileNumber)){
                    mMobileNumber.setError("Mobile Number");
                }else if (TextUtils.isEmpty(name)){
                    mName.setError("Name");
                }else {

                    firebaseFirestore.collection("UserData")
                            .whereEqualTo("MobileNumber",mCpp.getFullNumberWithPlus())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (!value.isEmpty()){
                                        mBtnContinue.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        mMobileNumber.setError("Mobile number already register");
                                    }else {
                                        Intent intent = new Intent(RegisterActivity.this, RegisterOtpActivity.class);
                                        intent.putExtra("MobileNumber",mCpp.getFullNumberWithPlus().replace(" ",""));
                                        intent.putExtra("WithoutCCMobile", mobileNumber);
                                        intent.putExtra("Name",name);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
}