package com.efficacious.restaurantuserapp.Fragments;

import static com.airbnb.lottie.L.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.NoConnectionActivity;
import com.efficacious.restaurantuserapp.Model.CustomerDetailsResponse;
import com.efficacious.restaurantuserapp.Model.GetCustomer;
import com.efficacious.restaurantuserapp.Model.GetTakeAwayOrderId;
import com.efficacious.restaurantuserapp.Model.OrderDetails;
import com.efficacious.restaurantuserapp.Model.TakeAwayOrderIdResponse;
import com.efficacious.restaurantuserapp.Model.TakeOrderDetail;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;
import com.efficacious.restaurantuserapp.util.Constant;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckoutFragment extends Fragment {

    TextView mUserName,mMobileNumber;

    TextView mAddress;
    CheckInternetConnection checkInternetConnection;
    SharedPreferences sharedPreferences;
    List<GetCustomer> getCustomers;
    Button btnOrder;
    long OrderId;
    String TimeStamp;
    ProgressBar progressBar;
    List<MenuData> menuData;

    MenuDatabase menuDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        setLocalDatabase();

        menuData = menuDatabase.dao().getMenuListData();
        mUserName = view.findViewById(R.id.userName);
        mMobileNumber = view.findViewById(R.id.mobileNumber);
        btnOrder = view.findViewById(R.id.btnConfirmOrder);

        mAddress = view.findViewById(R.id.address);
        progressBar = view.findViewById(R.id.loader);

        checkInternetConnection = new CheckInternetConnection(getContext());

        sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
        String MobileNumber = sharedPreferences.getString(Constant.MOBILE_NUMBER,null);

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
                            mMobileNumber.setText(getCustomers.get(0).getMobileNo());
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

        view.findViewById(R.id.btnConfirmOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
                int RegisterId = sharedPreferences.getInt(Constant.REGISTER_ID,0);
                String timeStamp = String.valueOf(System.currentTimeMillis());
                Log.d(TAG, "timeStamp: " + timeStamp);
                TakeOrderDetail takeOrderDetail = new TakeOrderDetail(Constant.TAKEAWAY,RegisterId,0,null,1,0,1,null,"No", Constant.TAKEAWAY,timeStamp);


                if (!checkInternetConnection.isConnectingToInternet()){
                    startActivity(new Intent(getContext(), NoConnectionActivity.class));
                    getActivity().finish();
                }else {
                    try {
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .getOrder("insert",takeOrderDetail);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    Log.d(TAG, "onResponse: " + response.body().toString());
                                    TakeAwayOrderId(timeStamp);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(getContext(), "Api Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        view.findViewById(R.id.header_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void TakeAwayOrderId(String timeStamp) {
        Log.d(TAG, "timeStamp2: " + timeStamp);
        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(getContext(), NoConnectionActivity.class));
            getActivity().finish();
        }else {
            try {
                Call<TakeAwayOrderIdResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .getTakeAwayOrderId("select","1",timeStamp);

                call.enqueue(new Callback<TakeAwayOrderIdResponse>() {
                    @Override
                    public void onResponse(Call<TakeAwayOrderIdResponse> call, Response<TakeAwayOrderIdResponse> response) {
                        if (response.isSuccessful()){
                            List<GetTakeAwayOrderId> takeAwayOrderId = response.body().getGetTakeAwayOrder();
                            int size = takeAwayOrderId.size();
                            if (size>0){
                                OrderId = takeAwayOrderId.get(0).getOrderId();
                                TimeStamp = takeAwayOrderId.get(0).getTimeStamp();
                                Log.d(TAG, "OrderId: " + OrderId);
                                confirmOrder(menuData,checkInternetConnection);
                            }else {
                                Toast.makeText(getContext(), "Empty..", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<TakeAwayOrderIdResponse> call, Throwable t) {

                    }
                });

            }catch (Exception e){
                Toast.makeText(getContext(), "API Error : " + e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void confirmOrder(List<MenuData> menuData, CheckInternetConnection checkInternetConnection) {

        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(getContext(), NoConnectionActivity.class));
            getActivity().finish();
        }else {
            String takeAwayOrderId = String.valueOf(OrderId);
            int listSize = menuData.size();
            for (int i=0;i<menuData.size();i++){
                btnOrder.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                OrderDetails orderDetailsData = new OrderDetails(Integer.valueOf(takeAwayOrderId),menuData.get(i).getCategoryName(),
                        menuData.get(i).getMenuName(),Constant.TAKEAWAY,
                        0,menuData.get(i).getEmployeeId(),menuData.get(i).getPrice(),menuData.get(i).getQty()
                        ,null,Constant.TAKEAWAY,Constant.TAKEAWAY,TimeStamp);

                try {
                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .getOrderDetails("insert",orderDetailsData);

                    int finalI = i;

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (finalI == listSize-1){
                                Dialog dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.order_confirm_dialog);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();

                                Button btnAddMore = dialog.findViewById(R.id.btnCheckStatus);
                                btnAddMore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HistoryFragment())
                                                .disallowAddToBackStack()
                                                .commit();
                                        dialog.dismiss();
                                    }
                                });
                                progressBar.setVisibility(View.GONE);
                                btnOrder.setVisibility(View.VISIBLE);
                                menuDatabase.dao().deleteAllData();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btnOrder.setVisibility(View.VISIBLE);
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getContext(), "API Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    btnOrder.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void setLocalDatabase(){
        menuDatabase = Room.databaseBuilder(getContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }
}