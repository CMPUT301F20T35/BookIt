package com.example.bookit;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import androidx.navigation.Navigation;

public class MyBookFragment extends Fragment {
    private Button acceptedButton;
    private Button availableButton;
    private Button borrowedButton;
    private Button requestedButton;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLng;
    MyBookFragment myBook;
    MapsFragment map;

    @Override
    /**
     * fragment used for displaying options for owner
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_mybook
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mybook, container, false);
        // get view of four buttons
        acceptedButton = view.findViewById(R.id.button_accepted);
        availableButton = view.findViewById(R.id.button_available);
        borrowedButton = view.findViewById(R.id.button_borrowed);
        requestedButton = view.findViewById(R.id.button_requested);

        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAcceptedFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_to_mybook_accepted);
            }
        });

        availableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAvailableFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_to_mybook_available);
            }
        });

        borrowedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookBorrowedFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_to_mybook_borrowed);
            }
        });

        requestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to MyBookRequestedFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_to_mybook_requested);
            }
        });


        //choose a location to lend to borrower
        Button choose = view.findViewById(R.id.location);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());

        //final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
                }



            }
        });

        return view;
    }
    /**
     * this function is for getting the current location(lat and long) of the device and pass it into the map fragment
     * */
    @SuppressLint("MissingPermission")
    private void getLocation(){
        LocationManager locationManage=(LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        if (locationManage.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManage.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                @Override
                public void onComplete(@NonNull Task<android.location.Location> task) {
                    android.location.Location location=task.getResult();
                    if(location!=null){
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat",location.getLatitude());
                        bundle.putDouble("long",location.getLongitude());
                        Navigation.findNavController(getView()).navigate(R.id.action_from_mybook_to_mybook_nav,bundle);

                    }else{
                        LocationRequest locationRequest=new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback=new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location=locationResult.getLastLocation();
                                Bundle bundle = new Bundle();
                                bundle.putDouble("lat",location.getLatitude());
                                bundle.putDouble("long",location.getLongitude());
                                Navigation.findNavController(getView()).navigate(R.id.action_from_mybook_to_mybook_nav,bundle);
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }else{Toast.makeText(getContext(),"please turn on the data or GPS",Toast.LENGTH_SHORT).show();}

}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if (requestCode== 100&& grantResults.length>0&&(grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED)){
            getLocation();
        }
        else{
            Toast.makeText(getContext(),"permission denied",Toast.LENGTH_SHORT).show();
        }

    }

}