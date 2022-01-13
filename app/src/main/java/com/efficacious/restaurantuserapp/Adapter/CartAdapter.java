package com.efficacious.restaurantuserapp.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.efficacious.restaurantuserapp.Fragments.CartFragment;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuData;
import com.efficacious.restaurantuserapp.RoomDatabase.MenuDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<MenuData> menuData;
    Context context;
    MenuDatabase menuDatabase;
    int count = 1;

    public CartAdapter(List<MenuData> menuData) {
        this.menuData = menuData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view,parent,false);
        context = parent.getContext();
        setUpDB();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.foodName.setText(menuData.get(position).getMenuName());
        holder.foodPrice.setText(String.valueOf("₹ " + menuData.get(position).getPrice()));
        holder.btnAdd.setText(String.valueOf(menuData.get(position).getQty()));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.btnAdd.getText().toString());
                int price = menuData.get(position).getPrice();
                count++;
                int totalPrice = count*price;
                holder.btnAdd.setText(String.valueOf(count));
                menuDatabase.dao().updateMenuList(menuData.get(position).getPrice(),count,menuData.get(position).getMenuName());
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.btnAdd.getText().toString());
                if (count>1){
                    int price = menuData.get(position).getPrice();
                    count--;
                    int totalPrice = count*price;
                    holder.btnAdd.setText(String.valueOf(count));
                    menuDatabase.dao().updateMenuList(menuData.get(position).getPrice(),count,menuData.get(position).getMenuName());

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Remove form cart !!");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int size = menuData.size();
                            AppCompatActivity activity = (AppCompatActivity) context;
                            menuDatabase.dao().deleteMenu(menuData.get(position).getMenuName());
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment())
                                    .commit();
                            Toast.makeText(context, "Remove successfully !!", Toast.LENGTH_SHORT).show();
                            menuData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,menuData.size());
                        }
                    });
                    builder.setNegativeButton("Cancel",null);
                    builder.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImg,btnMinus;
        TextView foodName,foodPrice,btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImg = itemView.findViewById(R.id.foodImg);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnMinus = itemView.findViewById(R.id.btnMinus);

        }
    }

    private void setUpDB(){
        menuDatabase = Room.databaseBuilder(context, MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }

}
