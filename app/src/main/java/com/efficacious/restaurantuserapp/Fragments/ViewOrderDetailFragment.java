package com.efficacious.restaurantuserapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.NoConnectionActivity;
import com.efficacious.restaurantuserapp.Adapter.MenuAdapter;
import com.efficacious.restaurantuserapp.Adapter.OrderDetailViewAdapter;
import com.efficacious.restaurantuserapp.Model.GetExistingOrderDetail;
import com.efficacious.restaurantuserapp.Model.GetExistingOrderDetailResponse;
import com.efficacious.restaurantuserapp.Model.MenuResponse;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewOrderDetailFragment extends Fragment {

    String ResId,OrderId;
    CheckInternetConnection checkInternetConnection;
    OrderDetailViewAdapter orderDetailViewAdapter;
    List<GetExistingOrderDetail> getExistingOrderDetails;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_order_detail, container, false);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            OrderId = bundle.getString("OrderId");
            ResId = bundle.getString("ResId");
        }

        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(getContext(), NoConnectionActivity.class));
            getActivity().finish();
        }else{
            getExistingOrderDetails = new ArrayList<>();
            getOrderData(ResId,OrderId);
            recyclerView = view.findViewById(R.id.recycleView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        view.findViewById(R.id.header_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void getOrderData(String resId, String orderId) {
        try {
            Call<GetExistingOrderDetailResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getExistingOrderDetails("select",resId,orderId);

            call.enqueue(new Callback<GetExistingOrderDetailResponse>() {
                @Override
                public void onResponse(Call<GetExistingOrderDetailResponse> call, Response<GetExistingOrderDetailResponse> response) {
                    if (response.isSuccessful()){
                        getExistingOrderDetails = response.body().getGetExistingOrderDetails();
                        orderDetailViewAdapter = new OrderDetailViewAdapter(getExistingOrderDetails);
                        recyclerView.setAdapter(orderDetailViewAdapter);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetExistingOrderDetailResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}