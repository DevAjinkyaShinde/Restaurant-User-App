package com.efficacious.restaurantuserapp.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.efficacious.restaurantuserapp.Activity.MainActivity;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.FavoriteMenu;
import com.efficacious.restaurantuserapp.RoomDatabase.FavoriteMenuDatabase;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;
import com.efficacious.restaurantuserapp.util.Constant;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    List<FavoriteMenu> menuData;
    Context context;
    FavoriteMenuDatabase favoriteMenuDatabase;
    MenuDatabase menuDatabase;

    public FavoriteAdapter(List<FavoriteMenu> menuData) {
        this.menuData = menuData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_menu_view,parent,false);
        context = parent.getContext();
        setUpDB();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.foodName.setText(menuData.get(position).getMenuName());
        holder.foodPrice.setText(String.valueOf("₹ " + menuData.get(position).getPrice()));

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_DATA_SHARED_PREF,0);
                int RegisterId = sharedPreferences.getInt(Constant.REGISTER_ID,0);

                MenuData menu = new MenuData(0,menuData.get(position).getCatName(),
                        menuData.get(position).getMenuName(), Constant.TAKEAWAY,RegisterId,0,Integer.valueOf(menuData.get(position).getPrice()),1
                        ,null,Constant.TAKEAWAY, Constant.TAKEAWAY);
                menuDatabase.dao().menuDataList(menu);

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_cart_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btnAddMore = dialog.findViewById(R.id.btnAddMore);
                btnAddMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppCompatActivity activity = (AppCompatActivity) context;
                        activity.startActivity(new Intent(context, MainActivity.class));
                        dialog.dismiss();
                    }
                });

                ImageView btnClose = dialog.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove form favorite list");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        favoriteMenuDatabase.favorite_menu_dao().removeMenu(menuData.get(position).getMenuName());
                        Toast.makeText(context, "Remove successfully !!", Toast.LENGTH_SHORT).show();
                        menuData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,menuData.size());
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImg,btnRemove;
        TextView foodName,foodPrice;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImg = itemView.findViewById(R.id.foodImg);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);

        }
    }

    private void setUpDB(){
        menuDatabase = Room.databaseBuilder(context, MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
        favoriteMenuDatabase = Room.databaseBuilder(context, FavoriteMenuDatabase.class,"FavoriteMenuDB")
                .allowMainThreadQueries().build();
    }

}
