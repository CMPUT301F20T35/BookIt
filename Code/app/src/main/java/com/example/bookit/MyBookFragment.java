package com.example.bookit;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyBookFragment extends Fragment {

    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mybook, container, false);
        rv = root.findViewById(R.id.rv_1);

        //initilize test array and adapter
        Book book = new Book("TestBook","YBS","123456",null,
                null,"Perfect","YBS");
        ArrayList<Book> testList = new ArrayList<Book>();
        testList.add(book);
        //set up manager and adapter to contain data
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new BookAdapter(getActivity(),testList));
        return root;
    }
}