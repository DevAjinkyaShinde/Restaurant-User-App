package com.efficacious.restaurantuserapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.restaurantuserapp.Fragments.RecycleViewFragment;
import com.efficacious.restaurantuserapp.Fragments.SearchMenuFragment;
import com.efficacious.restaurantuserapp.Model.MenuCategoryDetail;
import com.efficacious.restaurantuserapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder> {

    List<MenuCategoryDetail> menuCategoryDetails;
    Context context;
    int isSelect=-1;
    int firstTime = 0;

    public SearchCategoryAdapter(List<MenuCategoryDetail> menuCategoryDetails) {
        this.menuCategoryDetails = menuCategoryDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.categoryName.setText(menuCategoryDetails.get(position).getCatName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SearchMenuFragment();
                Bundle bundle = new Bundle();
                bundle.putString("CategoryId", menuCategoryDetails.get(position).getCatId().toString());
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                        .addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuCategoryDetails.size();
    }

    public void updateList(List<MenuCategoryDetail> temp) {
        menuCategoryDetails = temp;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        CardView view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            view = itemView.findViewById(R.id.cardView);
        }
    }
}
