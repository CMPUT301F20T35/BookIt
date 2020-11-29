package com.example.bookit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class BorrowAcceptedFragment extends Fragment {
    private Button availableButton;
    private Button borrowedButton;
    private Button requestedButton;
    private RecyclerView rv;
    FireStoreHelper fs;
    private BookAdapter bAdapter;
    private ImageButton searchButton;

    private TextView booktitle;
    private TextView ownername;
    private TextView ISBNnumber;
    private TextView bookdescription;
    @Override
    /**
     * fragment used for displaying books being accepted and borrower wants to borrow
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_borrow_accepted
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_borrow_accepted, container, false);
        // get view of four buttons
        availableButton = view.findViewById(R.id.button_available);
        borrowedButton = view.findViewById(R.id.button_borrowed);
        requestedButton = view.findViewById(R.id.button_requested);
        searchButton = view.findViewById(R.id.button_search);
        fs=new FireStoreHelper(getActivity());

        availableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAvailableFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_accepted_to_borrow_available);
            }
        });

        borrowedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookBorrowedFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_accepted_to_borrow_borrowed);
            }
        });

        requestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to MyBookRequestedFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_accepted_to_borrow_requested);

            }
        });

        // set up recycler view
        rv = view.findViewById(R.id.rv_1);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);
        final ArrayList<Book> testList = new ArrayList<Book>();

        bAdapter = new BookAdapter(getActivity(), testList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                Book bookCliced=testList.get(pos);
                String isbn=bookCliced.getISBN();
                String des=bookCliced.getDescription();
                String title=bookCliced.getTitle();
                String author=bookCliced.getAuthor();
                String owner=bookCliced.getOwnerName();

                Bundle bundle=new Bundle();
                bundle.putString("isbn",isbn);
                bundle.putString("description",des);
                bundle.putString("title",title);
                bundle.putString("author",author);
                bundle.putString("owner",owner);
                Navigation.findNavController(view).navigate(R.id.action_borrow_accepted_to_borrow_location,bundle);
            }


        });

        rv.setAdapter(bAdapter);
        fs.fetch_AcceptedBook( new dbCallback(){
            @Override
            public void onCallback(Map map) {
                String title=map.get("title").toString();
                String ISBN=map.get("ISBN").toString();
                String author=map.get("author").toString();
                String description=map.get("description").toString();
                String ownerName=map.get("ownerName").toString();
                //System.out.println(title);
                Book b= new Book(title,author,ISBN,description,ownerName,null);
                testList.add(b);
                bAdapter.notifyDataSetChanged();

            }
        });



        //set search button function
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // switch to BorrowSearchFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_accepted_to_borrow_search);
            }
        });

        return view;
    }





}