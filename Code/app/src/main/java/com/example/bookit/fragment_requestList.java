package com.example.bookit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class fragment_requestList extends Fragment {

    RecyclerView rv;
    UserAdapter uAdapter;


    @Override
    /**
     * fragment used for owner accepting or denying requester
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_request_list
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);


        rv = view.findViewById(R.id.requestList);

        //initilize test array and adapter

        final ArrayList<User> testList = new ArrayList<User>();

        //set up manager and adapter to contain data
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);

//        setting the separate line
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL);
        rv.addItemDecoration(divider);

        //adapter operation
        uAdapter = new UserAdapter(getActivity(), testList);
        rv.setAdapter(uAdapter);

        testList.add(new User("YBS","YBS","123","123@13","123","123"));
        testList.add(new User("YBS","YBS","123","123@13","123","123"));
        testList.add(new User("YBS","YBS","123","123@13","123","123"));
        testList.add(new User("YBS","YBS","123","123@13","123","123"));
        uAdapter.notifyDataSetChanged();

        return view;

    }
}