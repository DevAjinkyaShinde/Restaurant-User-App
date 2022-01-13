package com.efficacious.restaurantuserapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.MainActivity;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.FavoriteMenu;
import com.efficacious.restaurantuserapp.RoomDatabase.FavoriteMenuDatabase;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;
import com.efficacious.restaurantuserapp.util.Constant;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ViewFoodFragment extends Fragment {


    String MenuName,Price,Category,MenuId;
    TextView foodName,foodPrice,header;
    Button btnAddToCart,btnAddToFavorite;
    MenuDatabase menuDatabase;
    FavoriteMenuDatabase favoriteMenuDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_food, container, false);

        setLocalDatabase();

        foodName = view.findViewById(R.id.foodName);
        foodPrice = view.findViewById(R.id.foodPrice);
        header = view.findViewById(R.id.header_title);
        btnAddToFavorite = view.findViewById(R.id.btnAddToFavorite);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            MenuName = bundle.getString("MenuName");
            Price = bundle.getString("Price");
            Category = bundle.getString("Category");
            MenuId = bundle.getString("MenuId");
            foodName.setText(MenuName);
            foodPrice.setText("₹ " +Price);
            header.setText(Category);
        }

        view.findViewById(R.id.btnAddToFavorite).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String btnTxt = btnAddToFavorite.getText().toString();

                if (btnTxt.equalsIgnoreCase("Remove to favorite")){
                    favoriteMenuDatabase.favorite_menu_dao().removeMenu(MenuName);
                    btnAddToFavorite.setText("Add to favorite");
                    btnAddToFavorite.setTextColor(ContextCompat.getColor(getContext(),R.color.secondary));
                    Toast.makeText(getContext(), "Remove from favorite", Toast.LENGTH_SHORT).show();
                }else {
                    FavoriteMenu favoriteMenu = new FavoriteMenu(Category,Integer.parseInt(MenuId),"",MenuName,Integer.parseInt(Price),1,0);
                    favoriteMenuDatabase.favorite_menu_dao().favoriteMenuList(favoriteMenu);
                    btnAddToFavorite.setText("Remove to favorite");
                    btnAddToFavorite.setTextColor(ContextCompat.getColor(getContext(),R.color.primary));
                    Toast.makeText(getContext(), "Add successfully !!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        view.findViewById(R.id.btnAddToCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
                int RegisterId = sharedPreferences.getInt(Constant.REGISTER_ID,0);

                MenuData menuData = new MenuData(0,Category,
                        MenuName, Constant.TAKEAWAY,RegisterId,0,Integer.valueOf(Price),1
                        ,null,Constant.TAKEAWAY, Constant.TAKEAWAY);
                menuDatabase.dao().menuDataList(menuData);

                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_cart_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btnAddMore = dialog.findViewById(R.id.btnAddMore);
                btnAddMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment())
//                                .addToBackStack(null)
//                                .commit();
                        startActivity(new Intent(getContext(), MainActivity.class));
                        dialog.dismiss();
                    }
                });

                ImageView btnClose = dialog.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                });

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

    private void setLocalDatabase(){
        menuDatabase = Room.databaseBuilder(getContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
        favoriteMenuDatabase = Room.databaseBuilder(getContext(), FavoriteMenuDatabase.class,"FavoriteMenuDB")
                .allowMainThreadQueries().build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment())
                        .disallowAddToBackStack()
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}