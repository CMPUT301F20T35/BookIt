package com.example.bookit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class BorrowLocationFragment extends Fragment {
    private Button locationButton;
    private Button borrowButton;
    private ImageView backButton;

    @Override
    /**
     * fragment used for borrower confirming location of the hand off
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_borrow_location
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_borrow_location, container, false);
        // find view by ids
        locationButton = view.findViewById(R.id.bt_location);
        borrowButton = view.findViewById(R.id.bt_borrow);
        backButton = view.findViewById(R.id.bt_back);

        // set listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to previous fragment
                getActivity().onBackPressed();
            }
        });

        // set listener for borrow button
        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // scan code functionality need to be implemented here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        });

        // set listener for location button
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location=new Location(-113.525995,53.523454);//hard coded edmonton location
                if(location!=null){
                    Bundle bundle = new Bundle();
                    Log.d("faedfa",Double.toString(location.getLatitude()));
                    bundle.putDouble("lat",location.getLatitude());
                    bundle.putDouble("long",location.getLongitude());
                    Navigation.findNavController(v).navigate(R.id.fragment_borrow_location_to_view_map_fragment,bundle);

                }
            }
        });

        return view;
    }
}
