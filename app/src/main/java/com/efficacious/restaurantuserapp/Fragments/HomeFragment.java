package com.efficacious.restaurantuserapp.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.NoConnectionActivity;
import com.efficacious.restaurantuserapp.Activity.WelcomeActivity;
import com.efficacious.restaurantuserapp.Adapter.MenuAdapter;
import com.efficacious.restaurantuserapp.Adapter.MenuCategoryAdapter;
import com.efficacious.restaurantuserapp.Model.MenuCategoryDetail;
import com.efficacious.restaurantuserapp.Model.MenuCategoryResponse;
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

public class HomeFragment extends Fragment {

    List<MenuCategoryDetail> menuCategoryDetails;
    MenuCategoryAdapter adapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            startActivity(new Intent(getContext(), NoConnectionActivity.class));
            getActivity().finish();
        }else {
            menuCategoryDetails = new ArrayList<>();
            categoryList();
            recyclerView = view.findViewById(R.id.categoryRecycleView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

//            CategoryId = getArguments().getString("CategoryId");

        }


        FragmentManager manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.recycleFragment, new RecycleViewFragment());
        transaction.commit();

        return view;
    }

    private void categoryList() {
        try {
            Call<MenuCategoryResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getCategoryList("Category","1");

            call.enqueue(new Callback<MenuCategoryResponse>() {
                @Override
                public void onResponse(Call<MenuCategoryResponse> call, Response<MenuCategoryResponse> response) {

                    if (response.isSuccessful()){
                        menuCategoryDetails = response.body().getMenuCategoryDetails();
                        adapter = new MenuCategoryAdapter(menuCategoryDetails);
                        recyclerView.setAdapter(adapter);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MenuCategoryResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Are you sure to exit ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                                getActivity().moveTaskToBack(true);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}