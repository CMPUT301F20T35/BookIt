package com.example.bookit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsFragment extends Fragment {
    private LatLng latLng1;
    ImageButton confirm;
    ImageButton myLocation;
    Location location;
    public MapsFragment(){
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(final GoogleMap googleMap) {
            //default location is device's current location
            Bundle bundle = getArguments();//get the current position from mybookFragment abd make a mark on the map
            final Double lat;
            final Double lon;
            lat=bundle.getDouble("lat");
            lon=bundle.getDouble("long");
            latLng1=new LatLng(lat,lon);
            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(latLng1);
            markerOptions.title("my current Location: "+latLng1.latitude+":"+latLng1.longitude);
            googleMap.clear();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,20));
            googleMap.addMarker(markerOptions);
            //user choose to get back to the current location
            myLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    latLng1=new LatLng(lat,lon);
                    MarkerOptions markerOptions=new MarkerOptions();
                    markerOptions.position(latLng1);
                    markerOptions.title("my current Location: "+latLng1.latitude+":"+latLng1.longitude);
                    googleMap.clear();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,20));
                    googleMap.addMarker(markerOptions);
                }
            });
            // user choose on any point ont the map
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    latLng1=latLng;
                    //initialise the marker
                    MarkerOptions markerOptions=new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Location chose: "+latLng.latitude+":"+latLng.longitude);
                    googleMap.clear();
                    googleMap.addMarker(markerOptions);
                }
            });
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_maps, container, false);
        confirm=v.findViewById(R.id.confirm);
        myLocation=v.findViewById(R.id.current);

        //create a new location object after confirm
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location=new Location(latLng1.latitude,latLng1.longitude);
                Log.d("asddssa", Double.toString(location.getLongitude()));
                getActivity().onBackPressed();
            }
        });

        return v ;
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