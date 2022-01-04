package com.efficacious.restaurantuserapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.NoConnectionActivity;
import com.efficacious.restaurantuserapp.Activity.WelcomeActivity;
import com.efficacious.restaurantuserapp.Adapter.OrderAdapter;
import com.efficacious.restaurantuserapp.Model.GetUserWiseTakeAwayOrder;
import com.efficacious.restaurantuserapp.Model.GetUserWiseTakeAwayOrderResponse;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;
import com.efficacious.restaurantuserapp.util.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    CheckInternetConnection checkInternetConnection;
    List<GetUserWiseTakeAwayOrder> userWiseTakeAwayOrders;
    OrderAdapter orderAdapter;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    ImageView empty;
    TextView emptyTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        empty = view.findViewById(R.id.empty);
        emptyTxt = view.findViewById(R.id.emptyTxt);

        sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(getContext(), NoConnectionActivity.class));
            getActivity().finish();
        }else {
            userWiseTakeAwayOrders = new ArrayList<>();
            historyData();
            recyclerView = view.findViewById(R.id.recycleView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        view.findViewById(R.id.header_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment())
                        .disallowAddToBackStack().commit();
            }
        });

        return view;
    }

    private void historyData() {
        try {
            String RegisterId = String.valueOf(sharedPreferences.getInt(Constant.REGISTER_ID,0));
            Call<GetUserWiseTakeAwayOrderResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getOrderHistory("select","1",RegisterId);

            call.enqueue(new Callback<GetUserWiseTakeAwayOrderResponse>() {
                @Override
                public void onResponse(Call<GetUserWiseTakeAwayOrderResponse> call, Response<GetUserWiseTakeAwayOrderResponse> response) {
                    if (response.isSuccessful()){
                        userWiseTakeAwayOrders = response.body().getGetUserWiseTakeAwayOrder();
                        int size = userWiseTakeAwayOrders.size();
                        if (size==0){
                            empty.setVisibility(View.VISIBLE);
                            emptyTxt.setVisibility(View.VISIBLE);
                        }
                        orderAdapter = new OrderAdapter(userWiseTakeAwayOrders);
                        recyclerView.setAdapter(orderAdapter);

                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetUserWiseTakeAwayOrderResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}