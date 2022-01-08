package com.efficacious.restaurantuserapp.Fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    FloatingActionButton btnTrackOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_order_detail, container, false);

        btnTrackOrder = view.findViewById(R.id.btnTrackOrder);
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

        btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new OrderStatusFragment();
                Bundle bundle = new Bundle();
                bundle.putString("OrderId",OrderId);
                fragment.setArguments(bundle);
//                convertAddress(strAddress);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                        .addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void convertAddress(String address) {
        if (address != null && !address.isEmpty()) {
            try {
                Geocoder geocoder = new Geocoder(getContext());
                List<Address> addressList = geocoder.getFromLocationName(address, 1);
                if (addressList != null && addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q="+lat+"," +lng+"&mode=l"));
                    intent.setPackage("com.google.android.apps.maps");

                    if (intent.resolveActivity(getActivity().getPackageManager())!=null){
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } // end catch
        } // end if
    } // end convertAddress

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