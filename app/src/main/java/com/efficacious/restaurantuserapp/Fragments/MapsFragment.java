package com.efficacious.restaurantuserapp.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efficacious.restaurantuserapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsFragment extends Fragment {

//    Double lat=19.8347,log=75.8816;
    String lat,log,userLat,userLog;
    FusedLocationProviderClient fusedLocationProviderClient;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            mapView(googleMap);

//            Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
//                    .clickable(true)
//                    .add(
//                            new LatLng(-35.016, 143.321),
//                            new LatLng(-34.747, 145.592),
//                            new LatLng(-34.364, 147.891),
//                            new LatLng(-33.501, 150.217),
//                            new LatLng(-32.306, 149.248),
//                            new LatLng(-32.491, 147.309)));


        }
    };

    private void mapView(GoogleMap googleMap) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("+919623921310");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lat = String.valueOf(snapshot.child("latitude").getValue());
                log = String.valueOf(snapshot.child("longitude").getValue());
                LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(log));
                LatLng userLocation = new LatLng(Double.parseDouble(userLat), Double.parseDouble(userLog));
                MarkerOptions markerOptionsUser = new MarkerOptions().position(sydney).title("Your location");
                markerOptionsUser.icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
                googleMap.addMarker(markerOptionsUser);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("Delivery boy");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_marker));
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                googleMap.setMinZoomPreference(12f);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (lat!=null){

        }else {
            LatLng latLng = new LatLng(20.5937, 78.9629);
            googleMap.addMarker(new MarkerOptions().position(latLng).title("India"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.setMinZoomPreference(12f);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},44);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location!=null){
                        userLat = String.valueOf(location.getLatitude());
                        userLog = String.valueOf(location.getLongitude());
                        Log.d(ContentValues.TAG, "long & lat : " + location.getLongitude() + " " + location.getLatitude() );
                    }else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location location1 = locationResult.getLastLocation();
                                userLat = String.valueOf(location1.getLatitude());
                                userLog = String.valueOf(location1.getLongitude());
                                Log.d(ContentValues.TAG, "long & lat : " + location1.getLongitude() + " " + location1.getLatitude() );

                            }
                        };

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}