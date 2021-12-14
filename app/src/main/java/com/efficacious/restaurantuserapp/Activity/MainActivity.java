package com.efficacious.restaurantuserapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.efficacious.restaurantuserapp.Fragments.CartFragment;
import com.efficacious.restaurantuserapp.Fragments.FavoriteFragment;
import com.efficacious.restaurantuserapp.Fragments.HistoryFragment;
import com.efficacious.restaurantuserapp.Fragments.HomeFragment;
import com.efficacious.restaurantuserapp.Fragments.ProfileFragment;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    MenuDatabase menuDatabase;
    List<MenuData> menuData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLocalDatabase();
        menuData = menuDatabase.dao().getMenuListData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navBar);
        BadgeDrawable badgeDrawable = bottomNavigationView.getBadge(R.id.cart);
        int size = menuData.size();

        if (badgeDrawable == null)
            bottomNavigationView.getOrCreateBadge(R.id.cart);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.cart:
                        fragment = new CartFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.history:
                        fragment = new HistoryFragment();
                        break;

                }

                if(fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                }

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

    }

    private void setLocalDatabase(){
        menuDatabase = Room.databaseBuilder(getApplicationContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }
}