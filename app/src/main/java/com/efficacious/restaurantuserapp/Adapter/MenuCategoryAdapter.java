package com.efficacious.restaurantuserapp.Adapter;

import static android.graphics.Color.RED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.restaurantuserapp.Fragments.HomeFragment;
import com.efficacious.restaurantuserapp.Fragments.RecycleViewFragment;
import com.efficacious.restaurantuserapp.Model.MenuCategoryDetail;
import com.efficacious.restaurantuserapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MenuCategoryAdapter extends RecyclerView.Adapter<MenuCategoryAdapter.ViewHolder> {

    List<MenuCategoryDetail> menuCategoryDetails;
    Context context;
    int isSelect=-1;
    int firstTime = 0;

    public MenuCategoryAdapter(List<MenuCategoryDetail> menuCategoryDetails) {
        this.menuCategoryDetails = menuCategoryDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final MenuCategoryDetail menuCategoryDetail = menuCategoryDetails.get(position);
        holder.categoryName.setText(menuCategoryDetail.getCatName());
        holder.categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelect = menuCategoryDetail.getCatId();
                firstTime=-1;
                notifyDataSetChanged();
                Fragment fragment = new RecycleViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("CategoryId", String.valueOf(isSelect));
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.recycleFragment,fragment)
                        .addToBackStack(null).commit();
            }
        });

        if(menuCategoryDetail.getCatId()== isSelect ){
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.primary));
            holder.view.setVisibility(View.VISIBLE);

        }else {
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.hint));
            holder.view.setVisibility(View.GONE);
        }

        if (position==firstTime){
            Fragment fragment = new RecycleViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("CategoryId", String.valueOf(menuCategoryDetails.get(position).getCatId()));
            fragment.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.recycleFragment,fragment)
                    .addToBackStack(null).commit();
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.primary));
            holder.view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return menuCategoryDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            view = itemView.findViewById(R.id.tabIndicator);
        }
    }
}
