package com.efficacious.restaurantuserapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.restaurantuserapp.Model.OrderStatusData;
import com.efficacious.restaurantuserapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {

    List<OrderStatusData> orderStatusData;
    Context context;

    public OrderStatusAdapter(List<OrderStatusData> orderStatusData) {
        this.orderStatusData = orderStatusData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_status,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        long timeStamp = orderStatusData.get(position).getTimeStamp();
        Date d = new Date(timeStamp);
        DateFormat dateFormat1 = new SimpleDateFormat("hh : mm a");
        String time = dateFormat1.format(d.getTime());
        holder.time.setText(time);
        if (orderStatusData.get(position).getStatus().equalsIgnoreCase("Request")){
            holder.icon.setImageResource(R.drawable.request);
            holder.status.setText("Request send.");
        }else if (orderStatusData.get(position).getStatus().equalsIgnoreCase("Accept")){
            holder.icon.setImageResource(R.drawable.accept);
            holder.status.setText("Request accept.");
        }else if (orderStatusData.get(position).getStatus().equalsIgnoreCase("Order Prepared")){
            holder.icon.setImageResource(R.drawable.parcel);
            holder.status.setText("Your order prepared.");
        }else if (orderStatusData.get(position).getStatus().equalsIgnoreCase("Order Shipped")){
            holder.icon.setImageResource(R.drawable.bike);
            holder.status.setText("Order on the way..");
        }else if (orderStatusData.get(position).getStatus().equalsIgnoreCase("Order Complete")){
            holder.icon.setImageResource(R.drawable.correct);
            holder.status.setText("Your order successfully completed.");
        }
    }

    @Override
    public int getItemCount() {
        return orderStatusData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView status;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);
        }
    }
}
