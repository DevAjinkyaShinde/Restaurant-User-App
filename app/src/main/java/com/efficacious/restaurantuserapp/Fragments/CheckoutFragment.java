package com.efficacious.restaurantuserapp.Fragments;

import static com.airbnb.lottie.L.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.MainActivity;
import com.efficacious.restaurantuserapp.Activity.NoConnectionActivity;
import com.efficacious.restaurantuserapp.Model.CustomerDetailsResponse;
import com.efficacious.restaurantuserapp.Model.GetCustomer;
import com.efficacious.restaurantuserapp.Model.GetFCM;
import com.efficacious.restaurantuserapp.Model.GetFCMTokenResponse;
import com.efficacious.restaurantuserapp.Model.GetTakeAwayOrderId;
import com.efficacious.restaurantuserapp.Model.OrderDetails;
import com.efficacious.restaurantuserapp.Model.TakeAwayOrderIdResponse;
import com.efficacious.restaurantuserapp.Model.TakeOrderDetail;
import com.efficacious.restaurantuserapp.Notification.APIService;
import com.efficacious.restaurantuserapp.Notification.Client;
import com.efficacious.restaurantuserapp.Notification.Data;
import com.efficacious.restaurantuserapp.Notification.NotificationSender;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;
import com.efficacious.restaurantuserapp.util.Constant;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
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
    List<GetFCM> getFCM;
    Button btnOrder;
    long OrderId;
    String TimeStamp;
    ProgressBar progressBar;
    List<MenuData> menuData;
    String DeliveryStatus = Constant.PICK_UP;
    RadioButton doorDeliveryRDB,pickUpRDB;
    MenuDatabase menuDatabase;
    int RegisterId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        setLocalDatabase();

        menuData = menuDatabase.dao().getMenuListData();
        mUserName = view.findViewById(R.id.userName);
        mMobileNumber = view.findViewById(R.id.mobileNumber);
        btnOrder = view.findViewById(R.id.btnConfirmOrder);
        doorDeliveryRDB = view.findViewById(R.id.doorDelivery);
        pickUpRDB = view.findViewById(R.id.pickUp);

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

        view.findViewById(R.id.btnEditAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new UpdateProfileFragment())
                        .addToBackStack(null).commit();
            }
        });

        //RadioButton

        doorDeliveryRDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveryStatus = Constant.DOOR_DELIVERY;
            }
        });

        pickUpRDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveryStatus = Constant.PICK_UP;
            }
        });

        view.findViewById(R.id.btnConfirmOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = 0;
                for (int i=0;i<menuData.size();i++){
                    total += menuData.get(i).getPrice() * menuData.get(i).getQty();
                }

                if (DeliveryStatus.equalsIgnoreCase(Constant.PICK_UP)){
                    bookOrder();
                }else if (DeliveryStatus.equalsIgnoreCase(Constant.DOOR_DELIVERY)){
                    if (total<299){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Order is below ₹299 !!");
                        builder.setMessage("₹99 delivery charges apply on order.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bookOrder();
                            }
                        });
                        builder.setNegativeButton("Cancel",null);
                        builder.show();
                    }else {
                        bookOrder();
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

    private void bookOrder() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
        RegisterId = sharedPreferences.getInt(Constant.REGISTER_ID,0);
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
                        menuData.get(i).getMenuName(),Constant.TAKEAWAY,RegisterId
                        ,menuData.get(i).getEmployeeId(),menuData.get(i).getPrice(),menuData.get(i).getQty()
                        ,DeliveryStatus,Constant.TAKEAWAY,Constant.TAKEAWAY,TimeStamp);


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

                                try {
                                    Call<GetFCMTokenResponse> FCM_CALL = RetrofitClient
                                            .getInstance()
                                            .getApi()
                                            .getFCMToken("getFCM","1","Manager");

                                    FCM_CALL.enqueue(new Callback<GetFCMTokenResponse>() {
                                        @Override
                                        public void onResponse(Call<GetFCMTokenResponse> call, Response<GetFCMTokenResponse> response) {
                                            getFCM = response.body().getGetFCM();
                                            String FCMToken = getFCM.get(0).getVchFcmToken();
                                            String Title = "New Takeaway order #" + OrderId + " arrived";
                                            String Msg = "Tap to view";
                                            String flag = Constant.TAKEAWAY_NEW_ORDER;
                                            sendNotification(FCMToken,Title,Msg,flag);

                                        }

                                        @Override
                                        public void onFailure(Call<GetFCMTokenResponse> call, Throwable t) {

                                        }
                                    });
                                }catch (Exception e){

                                }

                                Dialog dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.order_confirm_dialog);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();

                                Button btnCheckout = dialog.findViewById(R.id.btnCheckStatus);
                                btnCheckout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HistoryFragment())
//                                                .disallowAddToBackStack()
//                                                .commit();



                                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
                                        String token = sharedPreferences.getString(Constant.FCM_TOKEN,null);

                                        HashMap<String,Object> map1 = new HashMap<>();
                                        map1.put("DeliveryStatus",DeliveryStatus);
                                        map1.put("OrderId",takeAwayOrderId);
                                        map1.put("Status","TakeAway");
                                        map1.put("RegisterId",RegisterId);
                                        map1.put("FCMToken",token);
                                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                        firebaseFirestore.collection("TakeAway")
                                                .document(takeAwayOrderId).set(map1);

                                        HashMap<String,Object> map = new HashMap<>();
                                        map.put("OrderId",Integer.parseInt(takeAwayOrderId));
                                        map.put("TimeStamp",System.currentTimeMillis());
                                        map.put("Status","Request");
                                        map.put("DeliveryStatus",DeliveryStatus);
                                        map.put("RegisterId",RegisterId);
                                        map.put("FCMToken",token);
                                        firebaseFirestore.collection("Orders").document(String.valueOf(OrderId))
                                                .collection("OrderStatus")
                                                .add(map);
                                        startActivity(new Intent(getContext(),MainActivity.class));
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

    private void sendNotification(String token, String title, String msg,String flag) {
        Data data = new Data(title,msg,flag);
        NotificationSender notificationSender = new NotificationSender(data,token);

        APIService apiService = Client.getRetrofit().create(APIService.class);

        apiService.sendNotification(notificationSender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setLocalDatabase(){
        menuDatabase = Room.databaseBuilder(getContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }
}