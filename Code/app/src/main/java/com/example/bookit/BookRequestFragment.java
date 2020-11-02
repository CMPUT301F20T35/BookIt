package com.example.bookit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class BookRequestFragment extends Fragment {
    private Button requestButton;
    private TextView ownerDetail;
    private ImageView backButton;

    @Override
    /**
     * fragment used for requesting book
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_book_request
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book_request, container, false);
        // find view by ids
        requestButton = view.findViewById(R.id.bt_request);
        ownerDetail = view. findViewById(R.id.tv_owner_name);
        backButton = view.findViewById(R.id.bt_back);

        // set listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        // set listener for owner name
        ownerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to owner detail fragment
                Navigation.findNavController(view).navigate(R.id.action_book_request_to_owner_detail);
            }
        });

        // set listener for request button
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request functionality need to be implemented here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
