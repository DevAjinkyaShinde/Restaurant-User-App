package com.efficacious.restaurantuserapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.efficacious.restaurantuserapp.Adapter.CartAdapter;
import com.efficacious.restaurantuserapp.Adapter.FavoriteAdapter;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.FavoriteMenu;
import com.efficacious.restaurantuserapp.RoomDatabase.FavoriteMenuDatabase;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    FavoriteMenuDatabase favoriteMenuDatabase;
    List<FavoriteMenu> favoriteMenus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        setLocalDatabase();

        favoriteMenus = favoriteMenuDatabase.favorite_menu_dao().getFavoriteMenuList();
        if (favoriteMenus.size() == 0){
            ImageView imageView = view.findViewById(R.id.empty);
            TextView textView = view.findViewById(R.id.emptyTxt);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        FavoriteAdapter adapter = new FavoriteAdapter(favoriteMenus);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        view.findViewById(R.id.header_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void setLocalDatabase(){
        favoriteMenuDatabase = Room.databaseBuilder(getContext(), FavoriteMenuDatabase.class,"FavoriteMenuDB")
                .allowMainThreadQueries().build();
    }
}