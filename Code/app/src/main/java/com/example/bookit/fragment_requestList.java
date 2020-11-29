package com.example.bookit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class fragment_requestList extends Fragment {


    private RecyclerView rv;
    private UserAdapter uAdapter;

    private ImageView backButton;
    private TextView ownerDetail;
    private FireStoreHelper fs;
    private RequestHandler rh;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private ImageButton editImage;
    private ImageButton delete;
    private ImageButton editInfo;
    private TextView titleView;
    private TextView ownerView;
    private TextView isbnView;
    private TextView descriptionView;
    private TextView authorView;

    protected ImageView imageView;

    private String isbn;
    private String title;
    private String description;
    private String owner;
    private String author;

    @Override
    /**
     * fragment used for owner accepting or denying requester
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_request_list
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        fs=new FireStoreHelper(getActivity());
        rv = view.findViewById(R.id.requestList);
        ownerDetail = (TextView)view.findViewById(R.id.owner_name);
        backButton = view.findViewById(R.id.bt_back2);

        //editImage=view.findViewById(R.id.editimage2);
        //delete=view.findViewById(R.id.deleteButton);
        titleView=view.findViewById(R.id.title);
        ownerView=view.findViewById(R.id.owner_name);
        isbnView=view.findViewById(R.id.ISBN);
        descriptionView=view.findViewById(R.id.Description);
        imageView = view.findViewById(R.id.imageView);
        //authorView=view.findViewById(R.id.);
        //editInfo=view.findViewById(R.id.editbookinfo);

        Bundle b=getArguments();
        isbn=b.getString("isbn");
        title=b.getString("title");
        description=b.getString("description");
        owner=b.getString("owner");
        rh = (RequestHandler) b.getSerializable("rh");
        //author=b.getString("author");
        //set text
        titleView.setText(title);
        ownerView.setText(owner);
        isbnView.setText(isbn);
        descriptionView.setText(description);
        //authorView.setText(author);

        ArrayList<String> requestorTemp = new ArrayList<String>();
        requestorTemp = rh.getRequestors();

        //initilize test array and adapter

        final ArrayList<User> testList = new ArrayList<User>();

        //set up manager and adapter to contain data
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);

        //back to last page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        //see owner detail
        ownerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putString("username",owner);
                Navigation.findNavController(view).navigate(R.id.action_fragment_requestList_to_owner_detail,b);
            }
        });


//        setting the separate line
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL);
        rv.addItemDecoration(divider);


        try {
            fs.load_book_image(isbn, new dbCallback() {
                @Override
                public void onCallback(Map map) {
                    String imageEncoded = (String) map.get("bookimage");
                    byte[] decodedByte = Base64.decode(imageEncoded, 0);
                    Bitmap img = BitmapFactory
                            .decodeByteArray(decodedByte, 0, decodedByte.length);
                    imageView.setImageBitmap(img);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        int i;
        for (i=0;i<requestorTemp.size();i++){
            String requestName = requestorTemp.get(i);
            fs.fetchUser(requestName, new dbCallback() {
                @Override
                public void onCallback(Map map) {
                    String id = map.get("id").toString();
                    String email = map.get("email").toString();
                    String number = map.get("number").toString();
                    String name = map.get("name").toString();
                    String username = map.get("username").toString();

                    testList.add(new User(name,username,id,email,number,"123"));
                    uAdapter.notifyDataSetChanged();
                }
            });
            //testList.add(new User("ybs","YBS","ysb","email","number","123"));
        }

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());

        //adapter operation
        ArrayList<String> finalRequestorTemp = requestorTemp;
        uAdapter = new UserAdapter(getActivity(), testList, new UserAdapter.MyClickListener() {
            @Override
            //accept button functionality
            public void onAccept(int p) {
                Toast.makeText(getActivity(),"Accept successfully", Toast.LENGTH_SHORT).show();
                User accpetRequestor = testList.get(p);
                String acceptName = testList.get(p).getUserName();
                fs.updateRequestor(acceptName,isbn);

                ArrayList<String> list = new ArrayList<String>();
                list.add(acceptName);
                fs.updateRequestors(isbn,list);
                Notification n=new Notification(title,isbn,owner,new ArrayList<String>(),acceptName,"REQUEST_ACCEPTED");
                fs.addNotification(n);

                testList.clear();
                testList.add(accpetRequestor);
                uAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"please choose a location", Toast.LENGTH_LONG).show();

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
                }


            }

            @Override
            //deny button functionality
            public void onDeny(int p) {
                String deleteName = testList.get(p).getUserName();
                finalRequestorTemp.remove(deleteName);
                testList.remove(p);
                fs.updateRequestors(isbn, finalRequestorTemp);
                uAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"Succesfully denied the request"+p, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClickPhoto(int p) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Requestor Detail: ");
                View v = LayoutInflater.from(getContext()).inflate(R.layout.request_detail_alertdialog, null, false);
                builder.setView(v);
                final TextView username=v.findViewById(R.id.name);
                final TextView email=v.findViewById(R.id.email);
                final TextView phoneNumber=v.findViewById(R.id.phonenumber);

                username.setText(testList.get(p).getUserName());
                email.setText(testList.get(p).getEmail());
                phoneNumber.setText(testList.get(p).getContactInfo());
                builder.setPositiveButton("Gotcha", null);
                builder.show();
                Toast.makeText(getActivity(),"Photo"+p, Toast.LENGTH_LONG).show();
            }
        });
        rv.setAdapter(uAdapter);



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
                        bundle.putString("isbn",isbn);
                        Navigation.findNavController(getView()).navigate(R.id.action_requestList_to_choose_location,bundle);

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
                                bundle.putString("isbn",isbn);
                                Navigation.findNavController(getView()).navigate(R.id.action_requestList_to_choose_location,bundle);
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
        if (requestCode== 100&& grantResults.length>0&&(grantResults[0]+grantResults[1]== PackageManager.PERMISSION_GRANTED)){
            getLocation();
        }
        else{
            Toast.makeText(getContext(),"permission denied",Toast.LENGTH_SHORT).show();
        }

    }

}