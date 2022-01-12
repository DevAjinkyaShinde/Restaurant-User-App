package com.efficacious.restaurantuserapp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.WelcomeActivity;
import com.efficacious.restaurantuserapp.Model.CustomerDetailsResponse;
import com.efficacious.restaurantuserapp.Model.GetCustomer;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;
import com.efficacious.restaurantuserapp.util.Constant;
import com.efficacious.restaurantuserapp.util.SharedPrefManger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    TextView mBtnEdit;
    String UserId;

    ImageView mProfileImg;
    TextView mUserName,mMobileNumber;

    TextView mAddress,mLogout;
    SharedPreferences sharedPreferences;
    CheckInternetConnection checkInternetConnection;
    List<GetCustomer> getCustomers;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    Uri profileImgUri;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mBtnEdit = view.findViewById(R.id.btnEdit);
        mProfileImg = view.findViewById(R.id.profile);
        mUserName = view.findViewById(R.id.userName);
        mMobileNumber = view.findViewById(R.id.mobileNumber);

        mAddress = view.findViewById(R.id.address);
        mLogout = view.findViewById(R.id.logOut);
        progressDialog = new ProgressDialog(getContext());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        checkInternetConnection = new CheckInternetConnection(getContext());

        sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
        String Name = sharedPreferences.getString(Constant.NAME,null);
        String MobileNumber = sharedPreferences.getString(Constant.MOBILE_NUMBER,null);
        mUserName.setText(Name);
        mMobileNumber.setText("Mo.No : " + MobileNumber);
        firebaseFirestore.collection("UserData").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            Picasso.get().load(ProfileUrl).noPlaceholder().noFade().into(mProfileImg);
                        }
                    }
                });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.logout_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button btnYes = dialog.findViewById(R.id.btnYes);
                TextView btnCancel = dialog.findViewById(R.id.btnCancel);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPrefManger sharedPrefManger = new SharedPrefManger(getContext());
                        sharedPrefManger.logout();
                        startActivity(new Intent(getContext(), WelcomeActivity.class));
                        getActivity().finish();
                        dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new UpdateProfileFragment())
                        .addToBackStack(null).commit();
            }
        });

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            try {
                Call<CustomerDetailsResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .getCustomerDetails("select","1",MobileNumber);


                call.enqueue(new Callback<CustomerDetailsResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<CustomerDetailsResponse> call, Response<CustomerDetailsResponse> response) {
                        if (response.isSuccessful()){
                            getCustomers = response.body().getGetCustomer();
                            mUserName.setText(getCustomers.get(0).getFirstName() + " " + getCustomers.get(0).getLastName());
                            mAddress.setText(getCustomers.get(0).getAddress1() + ", " + getCustomers.get(0).getAddress2() + ", " + getCustomers.get(0).getAddress3());
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

        view.findViewById(R.id.personalDetailsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new UpdateProfileFragment())
                        .addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.header_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        mProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImg();
            }
        });

        return view;
    }

    private void UploadImg() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(40)
                .setAspectRatio(1,1)
                .start(getActivity(),this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       try {
           if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
               CropImage.ActivityResult result = CropImage.getActivityResult(data);
               if (resultCode == RESULT_OK) {
                   profileImgUri = result.getUri();
                   mProfileImg.setImageURI(profileImgUri);
                   AddImg();

               } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                   Exception error = result.getError();
                   Toast.makeText(getContext(), "Error : " + error, Toast.LENGTH_SHORT).show();
               }

           }
       }catch (Exception e){
           Toast.makeText(getContext(),  e.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    private void AddImg() {

        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();
        StorageReference profileImgPath = storageReference.child("Profile").child(UserId + ".jpg");

        profileImgPath.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImgPath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        String ProfileUri = task.getResult().toString();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("ProfileImgUrl" , ProfileUri);

                        firebaseFirestore.collection("UserData").document(UserId).update(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Upload Successfully..", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Storage error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}