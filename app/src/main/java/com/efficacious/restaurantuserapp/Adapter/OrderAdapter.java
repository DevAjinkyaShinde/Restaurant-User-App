package com.efficacious.restaurantuserapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.restaurantuserapp.Fragments.ViewOrderDetailFragment;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mOrderId.setText("Order ID : #" + userWiseTakeAwayOrders.get(position).getOrderId());
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

        if (!TextUtils.isEmpty(total) && total!=null && !total.equals("null")){
            holder.mTotal.setText("₹" + total);
        }else {
            holder.mTotal.setVisibility(View.GONE);
            holder.mTextTotal.setVisibility(View.GONE);
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment fragment = new ViewOrderDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("OrderId", String.valueOf(userWiseTakeAwayOrders.get(position).getOrderId()));
                bundle.putString("ResId", String.valueOf(userWiseTakeAwayOrders.get(position).getResId()));
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userWiseTakeAwayOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTotal,mTextTotal;
        TextView mDate,mOrderId;
        TextView mStatus;
        ImageView mIcon;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTotal = itemView.findViewById(R.id.Total);
            mTextTotal = itemView.findViewById(R.id.textTotal);
            mDate = itemView.findViewById(R.id.date);
            mOrderId = itemView.findViewById(R.id.orderId);
            mStatus = itemView.findViewById(R.id.status);
            mIcon = itemView.findViewById(R.id.icon);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
