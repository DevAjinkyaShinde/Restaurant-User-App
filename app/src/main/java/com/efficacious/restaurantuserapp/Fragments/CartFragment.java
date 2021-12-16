package com.efficacious.restaurantuserapp.Fragments;

import static com.airbnb.lottie.L.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Activity.NoConnectionActivity;
import com.efficacious.restaurantuserapp.Activity.WelcomeActivity;
import com.efficacious.restaurantuserapp.Adapter.CartAdapter;
import com.efficacious.restaurantuserapp.Model.GetTakeAwayOrderId;
import com.efficacious.restaurantuserapp.Model.OrderDetails;
import com.efficacious.restaurantuserapp.Model.TakeAwayOrderIdResponse;
import com.efficacious.restaurantuserapp.Model.TakeOrderDetail;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;
import com.efficacious.restaurantuserapp.WebService.RetrofitClient;
import com.efficacious.restaurantuserapp.util.CheckInternetConnection;
import com.efficacious.restaurantuserapp.util.Constant;
import com.efficacious.restaurantuserapp.util.SharedPrefManger;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    MenuDatabase menuDatabase;
    List<MenuData> menuData;
    ProgressBar progressBar;
    Button btnOrder;
    TextView emptyTxt;
    ImageView empty;
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

        btnOrder = view.findViewById(R.id.btnOrder);
        progressBar = view.findViewById(R.id.loader);
        empty = view.findViewById(R.id.empty);
        emptyTxt = view.findViewById(R.id.emptyTxt);

        int size = menuData.size();

        if (size==0){
            btnOrder.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            emptyTxt.setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.btnOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
                Boolean Available = sharedPreferences.getBoolean(Constant.ADDRESS_AVAILABLE,true);

                if (Available){
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,new CheckoutFragment())
                            .addToBackStack(null)
                            .commit();
                }else {
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.address_not_found_dialog);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    Button btnYes = dialog.findViewById(R.id.btnYes);
                    TextView btnCancel = dialog.findViewById(R.id.btnCancel);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           getFragmentManager().beginTransaction().replace(R.id.fragment_container,new UpdateProfileFragment())
                                   .addToBackStack(null).commit();
                            dialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
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
    }

}