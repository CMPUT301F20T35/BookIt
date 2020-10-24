package com.example.bookit;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BorrowFragment extends Fragment {

    private RecyclerView rv;
    private BookAdapter borrowAdapter;

    CoordinatorLayout coordinatorLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_borrow, container, false);
        rv = root.findViewById(R.id.rv_2);

        //initilize test array and adapter
        Book book = new Book("TestBookBorrow","YBS","123456",null,
                null,"Perfect","YBS");
        Book book1 = new Book("TestBook1Borrow","YBS","123456",null,
                null,"Perfect","YBS");
        final ArrayList<Book> testList = new ArrayList<Book>();
        testList.add(book);
        testList.add(book1);
        //set up manager and adapter to contain data
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setting the separate line
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        //setting onclick , implementing intent at here.
        borrowAdapter = new BookAdapter(getActivity(), testList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                Toast.makeText(getActivity(),"Testing"+pos, Toast.LENGTH_SHORT).show();
            }


        });
        rv.setAdapter(borrowAdapter);

        return root;
    }
}