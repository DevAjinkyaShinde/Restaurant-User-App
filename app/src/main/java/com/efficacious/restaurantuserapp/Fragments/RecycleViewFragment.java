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
import com.efficacious.restaurantuserapp.Model.MenuDetail;
import com.efficacious.restaurantuserapp.Model.MenuResponse;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleViewFragment extends Fragment {

    List<MenuDetail> menuDetail;
    MenuAdapter menuAdapter;
    RecyclerView menuRecyclerView;
    CheckInternetConnection checkInternetConnection;
    String CategoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view, container, false);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            CategoryId = bundle.getString("CategoryId");
        }

        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(getContext(), NoConnectionActivity.class));
            getActivity().finish();
        }else{
            menuDetail = new ArrayList<>();
            if (!TextUtils.isEmpty(CategoryId)){
                menuList(CategoryId);
            }
            menuRecyclerView = view.findViewById(R.id.menuRecycleView);
            menuRecyclerView.setHasFixedSize(true);
            menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        }

        return view;
    }

    private void menuList(String id) {
        try {
            Call<MenuResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getMenu("CategoryDrp",id,"1");

            call.enqueue(new Callback<MenuResponse>() {
                @Override
                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                    if (response.isSuccessful()){
                        menuDetail = response.body().getMenuDetails();
                        menuAdapter = new MenuAdapter(menuDetail);
                        menuRecyclerView.setAdapter(menuAdapter);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MenuResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}