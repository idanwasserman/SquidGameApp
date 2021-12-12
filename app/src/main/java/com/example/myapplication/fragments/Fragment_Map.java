package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment {

    private GoogleMap mMap;
    private MarkerOptions markerOptions;
    private final int ZOOM = 15;
    private final LatLng collegeLocation = new LatLng(32.115139,34.817804);
    private boolean isMapReady = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        // Async map
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            isMapReady = true;

            // Set map to zoom on college location at first
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(collegeLocation, ZOOM));

            // Initialize marker options
            markerOptions = new MarkerOptions();
            markerOptions.position(collegeLocation);
            // Add marker on map
            mMap.clear();
            mMap.addMarker(markerOptions);
        });

        return view;
    }

    public void setFocusOnMapByLocation(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
        markerOptions.position(latLng);
        // Add marker on map
        mMap.clear();
        mMap.addMarker(markerOptions);
    }

}
