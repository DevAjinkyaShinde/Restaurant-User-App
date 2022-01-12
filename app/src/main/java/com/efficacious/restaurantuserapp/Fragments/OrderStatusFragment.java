package com.efficacious.restaurantuserapp.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.Adapter.OrderStatusAdapter;
import com.efficacious.restaurantuserapp.Model.OrderStatusData;
import com.efficacious.restaurantuserapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    List<OrderStatusData> orderStatusData;
    OrderStatusAdapter orderStatusAdapter;
    String OrderId;
    Button btnOrderTrackOnMap;
    Double lat,log;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_status, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        btnOrderTrackOnMap = view.findViewById(R.id.btnTrackOnMap);

        btnOrderTrackOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new MapsFragment())
                        .addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle bundle = this.getArguments();

        if(bundle != null){
            OrderId = bundle.getString("OrderId");
        }

        orderStatusData = new ArrayList<>();
        orderStatusAdapter = new OrderStatusAdapter(orderStatusData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(orderStatusAdapter);

        firebaseFirestore.collection("Orders").document(OrderId).collection("OrderStatus")
                .orderBy("TimeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){

                                OrderStatusData mData = doc.getDocument().toObject(OrderStatusData.class);
                                orderStatusData.add(mData);
                                orderStatusAdapter.notifyDataSetChanged();
                            }
                        }
                    }});

        firebaseFirestore.collection("Orders").document(OrderId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String status = task.getResult().getString("Status");
                    try {
                        if (status.equalsIgnoreCase("On the way")){
                            btnOrderTrackOnMap.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){

                    }
                }
            }
        });

    }
}