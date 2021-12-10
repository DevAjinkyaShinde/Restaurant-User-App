package com.efficacious.restaurantuserapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efficacious.restaurantuserapp.Adapter.CartAdapter;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;

import java.util.List;


public class CartFragment extends Fragment {

    MenuDatabase menuDatabase;
    List<MenuData> menuData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        setLocalDatabase();
        menuData = menuDatabase.dao().getMenuListData();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cartRecycleView);
        CartAdapter adapter = new CartAdapter(menuData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    private void setLocalDatabase(){
        menuDatabase = Room.databaseBuilder(getContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }

}