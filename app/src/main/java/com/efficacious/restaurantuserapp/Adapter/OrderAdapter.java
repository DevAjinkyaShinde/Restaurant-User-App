package com.efficacious.restaurantuserapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.restaurantuserapp.Model.GetUserWiseTakeAwayOrder;
import com.efficacious.restaurantuserapp.R;
import com.efficacious.restaurantuserapp.util.Constant;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    List<GetUserWiseTakeAwayOrder> userWiseTakeAwayOrders;
    Context context;

    public OrderAdapter(List<GetUserWiseTakeAwayOrder> userWiseTakeAwayOrders) {
        this.userWiseTakeAwayOrders = userWiseTakeAwayOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mOrderId.setText("#" + userWiseTakeAwayOrders.get(position).getOrderId());
        holder.mDate.setText(userWiseTakeAwayOrders.get(position).getCreatedDate());
        String status = userWiseTakeAwayOrders.get(position).getStatus();

        if (status.equalsIgnoreCase(Constant.TAKEAWAY)){
            holder.mStatus.setText("Request Pending");
        }else if (status.equalsIgnoreCase(Constant.KITCHEN_STATUS)){
            holder.mStatus.setText("Accept");
        }else if (status.equalsIgnoreCase(Constant.DISPATCH_STATUS)){
            holder.mStatus.setText("Dispatch Order");
        }else if (status.equalsIgnoreCase(Constant.BILL_STATUS)){
            holder.mStatus.setText("Billing");
        }else if (status.equalsIgnoreCase(Constant.CLOSE_STATUS)){
            holder.mIcon.setImageResource(R.drawable.correct);
            holder.mStatus.setTextColor(Color.parseColor("#2E8B57"));
            holder.mStatus.setText("Complete Order");
        }

        String total = String.valueOf(userWiseTakeAwayOrders.get(position).getTotal());

        if (total.equalsIgnoreCase("")){
            holder.mTotal.setText("₹ 00");
        }else {
            holder.mTotal.setText("₹" + total);
        }

    }

    @Override
    public int getItemCount() {
        return userWiseTakeAwayOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTotal;
        TextView mDate,mOrderId;
        TextView mStatus;
        ImageView mIcon;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTotal = itemView.findViewById(R.id.Total);
            mDate = itemView.findViewById(R.id.date);
            mOrderId = itemView.findViewById(R.id.orderId);
            mStatus = itemView.findViewById(R.id.status);
            mIcon = itemView.findViewById(R.id.icon);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
