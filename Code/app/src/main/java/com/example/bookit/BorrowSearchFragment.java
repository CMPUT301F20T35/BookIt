package com.example.bookit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BorrowSearchFragment extends Fragment {
    private ImageView backButton;
    private ImageView searchButton;
    private EditText searchText;
    private RecyclerView rv;
    private BookAdapter bAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_borrow_search, container, false);
        // get views by their ids
        backButton = view.findViewById(R.id.button_back);
        searchButton = view.findViewById(R.id.button_search);
        searchText = view.findViewById(R.id.text_search);
        rv = view.findViewById(R.id.rv_search);




        // set listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        // set listener for searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // search matched books
                String searInfo = searchText.getText().toString();
            }
        });

        return view;
    }
}
