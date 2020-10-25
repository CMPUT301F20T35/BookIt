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

public class MyBookFragment extends Fragment {
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLng;
    ArrayList<Double> loc;
    MyBookFragment myBook;
    MapsFragment map;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_mybook, container, false);
        Button choose = v.findViewById(R.id.location);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());

        //final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    loc=new ArrayList<Double>();
                    getLocation();
                }
                else{
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
                }



            }
        });

        return v;
    }
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
                        /*FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction2.replace(R.id.mybook, map, "mapfragment")
                                .addToBackStack(null)
                                .commit();*/
                        //latLng=new LatLng(location.getLatitude(),location.getLongitude());
                        Log.d("lat",String.valueOf(location.getLatitude()));
                        Log.d("long",String.valueOf(location.getLongitude()));

                    }else{
                        LocationRequest locationRequest=new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback=new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1=locationResult.getLastLocation();
                                latLng=new LatLng(location1.getLatitude(),location1.getLongitude());
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }

}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if (requestCode== 100&& grantResults.length>0&&(grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED)){
            getLocation();
        }
        else{
            Toast.makeText(getContext(),"permisssion denied",Toast.LENGTH_SHORT).show();
        }
    }

}