package com.efficacious.restaurantuserapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.restaurantuserapp.Model.GetExistingOrderDetail;
import com.efficacious.restaurantuserapp.R;

import java.util.List;

public class OrderDetailViewAdapter extends RecyclerView.Adapter<OrderDetailViewAdapter.ViewHolder>{

    List<GetExistingOrderDetail> getExistingOrderDetails;

    public OrderDetailViewAdapter(List<GetExistingOrderDetail> getExistingOrderDetails) {
        this.getExistingOrderDetails = getExistingOrderDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mFoodName.setText(getExistingOrderDetails.get(position).getMenuName());
        holder.mPrice.setText("₹ " + getExistingOrderDetails.get(position).getPrice().toString());
        holder.mQty.setText("Qty : " +getExistingOrderDetails.get(position).getQty().toString());
    }

    @Override
    public int getItemCount() {
        return getExistingOrderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mFoodName,mPrice,mQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFoodName = itemView.findViewById(R.id.foodName);
            mPrice = itemView.findViewById(R.id.Price);
            mQty = itemView.findViewById(R.id.qty);
        }
    }
}
