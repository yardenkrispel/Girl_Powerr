package com.example.Girl_Power.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Girl_Power.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private final String API_KEY ="AIzaSyCWF9UHDB8dNORrgCneZHBI2FKozHXJ0eQ";
    private MapView fMap_MAP_map;
    private GoogleMap myMap;
    private AppCompatActivity context;

    public MapFragment(AppCompatActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        findViews(view, savedInstanceState);

        return view;
    }

    private void findViews(View view, Bundle savedInstanceState) {
        fMap_MAP_map = view.findViewById(R.id.fMap_MAP_map);

        fMap_MAP_map.onCreate(savedInstanceState != null ? savedInstanceState.getBundle(API_KEY): null);
        fMap_MAP_map.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        this.myMap = googleMap;
        this.myMap.setMyLocationEnabled(true);
        this.myMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void find(double lat, double lon){
        LatLng location = new LatLng(lat, lon);
        myMap.addMarker(new MarkerOptions().position(location).title("Record Location"));
    }

    @Override
    public void onPause() {
        super.onPause();
        fMap_MAP_map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        fMap_MAP_map.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        fMap_MAP_map.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        fMap_MAP_map.onStop();
    }
}