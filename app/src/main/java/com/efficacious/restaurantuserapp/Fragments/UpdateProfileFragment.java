package com.efficacious.restaurantuserapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.NoConnectionActivity;
import com.efficacious.restaurantuserapp.Model.CustomerDetailsResponse;
import com.efficacious.restaurantuserapp.Model.GetCustomer;
import com.efficacious.restaurantuserapp.Model.RegisterUserDetails;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;
import com.efficacious.restaurantuserapp.util.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileFragment extends Fragment {

    EditText mFirstName,mLastName,mEmail,mMobileNumber,mAddress1,mAddress2,mAddress3;
    Button mBtnRegister;

    SharedPreferences sharedPreferences;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    CheckInternetConnection checkInternetConnection;
    SharedPreferences.Editor editor;
    List<GetCustomer> getCustomers;
    String mobileNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        checkInternetConnection = new CheckInternetConnection(getContext());
        sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
        editor = sharedPreferences.edit();
        mFirstName = view.findViewById(R.id.firstName);
        mLastName = view.findViewById(R.id.lastName);
        mEmail = view.findViewById(R.id.email);
        mMobileNumber = view.findViewById(R.id.mobileNumber);
        mAddress1 = view.findViewById(R.id.address1);
        mAddress2 = view.findViewById(R.id.address2);
        mAddress3 = view.findViewById(R.id.address3);
        mBtnRegister = view.findViewById(R.id.btnSubmit);
        getCustomers = new ArrayList<>();

        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(getContext(), NoConnectionActivity.class));
            getActivity().finish();
        }else {
            firebaseFirestore.collection("UserData")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        mobileNumber = task.getResult().getString("WithoutCCMobile");
                        String name = task.getResult().getString("Name");
                        mMobileNumber.setText(mobileNumber);
                        mFirstName.setText(name);

                        try {
                            Call<CustomerDetailsResponse> call = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .getCustomerDetails("select","1",mobileNumber);


                            call.enqueue(new Callback<CustomerDetailsResponse>() {
                                @Override
                                public void onResponse(Call<CustomerDetailsResponse> call, Response<CustomerDetailsResponse> response) {
                                    if (response.isSuccessful()){
                                        getCustomers = response.body().getGetCustomer();
                                        mFirstName.setText(getCustomers.get(0).getFirstName());
                                        mLastName.setText(getCustomers.get(0).getLastName());
                                        mEmail.setText(getCustomers.get(0).getEmailId());
                                        mAddress1.setText(getCustomers.get(0).getAddress1());
                                        mAddress2.setText(getCustomers.get(0).getAddress2());
                                        mAddress3.setText(getCustomers.get(0).getAddress3());
                                    }
                                }

                                @Override
                                public void onFailure(Call<CustomerDetailsResponse> call, Throwable t) {
                                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String email = mEmail.getText().toString();
                String mobileNumber = mMobileNumber.getText().toString();
                String address1 = mAddress1.getText().toString();
                String address2 = mAddress2.getText().toString();
                String address3 = mAddress3.getText().toString();

                if (TextUtils.isEmpty(firstName)){
                    mFirstName.setError("Empty field");
                }else if (TextUtils.isEmpty(lastName)){
                    mLastName.setError("Empty field");
                }else if (TextUtils.isEmpty(email)){
                    mEmail.setError("Empty field");
                }else if (TextUtils.isEmpty(mobileNumber)){
                    mMobileNumber.setError("Empty field");
                }else if (TextUtils.isEmpty(address1)){
                    mAddress1.setError("Empty field");
                }else if (TextUtils.isEmpty(address2)){
                    mAddress2.setError("Empty field");
                }else if (TextUtils.isEmpty(address3)){
                    mAddress3.setError("Empty field");
                }else {

                    if (!checkInternetConnection.isConnectingToInternet()){
                        startActivity(new Intent(getContext(), NoConnectionActivity.class));
                        getActivity().finish();
                    }else {
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("Name",firstName);
                        firebaseFirestore.collection("UserData")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .update(map);
                        RegisterUserDetails registerUserDetails = new RegisterUserDetails(firstName,null,lastName,mobileNumber,email,address1,address2,address3,1,null,null);

                        try {
                            Call<ResponseBody> call = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .registerUser("insert",registerUserDetails);

                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()){
                                        editor.putBoolean(Constant.ADDRESS_AVAILABLE,true);
                                        editor.apply();
                                        editor.commit();
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment())
                                                .disallowAddToBackStack().commit();
                                        Toast.makeText(getContext(), "Update profile successfully..!!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(), "Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){

                        }

                    }

                }
            }
        });

        return view;
    }
}