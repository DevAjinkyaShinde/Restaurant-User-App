package com.efficacious.restaurantuserapp.Activity;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.efficacious.restaurantuserapp.Fragments.CartFragment;
import com.efficacious.restaurantuserapp.Fragments.HistoryFragment;
import com.efficacious.restaurantuserapp.Fragments.HomeFragment;
import com.efficacious.restaurantuserapp.Fragments.ProfileFragment;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;
import com.efficacious.restaurantuserapp.util.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    Fragment fragment;
    MenuDatabase menuDatabase;
    List<MenuData> menuData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLocalDatabase();
        menuData = menuDatabase.dao().getMenuListData();


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        SharedPreferences sharedPreferences = getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
                        String mobileNumber = sharedPreferences.getString(Constant.WITHOUT_CC_MOBILE_NUMBER,null);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constant.FCM_TOKEN,token);
                        editor.apply();
                        editor.commit();

                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        HashMap<String,Object> map = new HashMap<>();
                        firebaseFirestore.collection("Tokens")
                                .document(mobileNumber).set(map);

                        // Log and toast
                        Log.d(TAG, "Token : "+ token);
                    }
                });

        BottomNavigationView bottomNavigationView = findViewById(R.id.navBar);
        BadgeDrawable badgeDrawable = bottomNavigationView.getBadge(R.id.cart);
        int size = menuData.size();
        if (size>0){
            if (badgeDrawable == null)
                bottomNavigationView.getOrCreateBadge(R.id.cart).setNumber(size);
        }

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

        //notification intent extra data
        String flag = getIntent().getStringExtra("flag");
        if (!TextUtils.isEmpty(flag)){
            if (flag.equalsIgnoreCase(Constant.USER_HISTORY)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment())
                        .addToBackStack(null).commit();
            }
        }
    }

    private void setLocalDatabase(){
        menuDatabase = Room.databaseBuilder(getApplicationContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }

}