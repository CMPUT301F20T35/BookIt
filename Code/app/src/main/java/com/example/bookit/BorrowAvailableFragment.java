package com.example.bookit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BorrowAvailableFragment extends Fragment {
    private Button acceptedButton;
    private Button borrowedButton;
    private Button requestedButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_borrow_available, container, false);
        // get view of four buttons
        acceptedButton = view.findViewById(R.id.button_accepted);
        borrowedButton = view.findViewById(R.id.button_borrowed);
        requestedButton = view.findViewById(R.id.button_requested);

        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAcceptedFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_available_to_borrow_accepted);
            }
        });

        borrowedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookBorrowedFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_available_to_borrow_borrowed);
            }
        });

        requestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to MyBookRequestedFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_available_to_borrow_requested);
            }
        });

        return view;
    }
}