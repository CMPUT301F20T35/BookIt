package com.example.bookit;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Map;

public class MyBookAcceptedFragment extends Fragment {
    private Button availableButton;
    private Button borrowedButton;
    private Button requestedButton;
    private FireStoreHelper fs;
    private RecyclerView rv;
    private BookAdapter bAdapter;
    private FloatingActionButton addButton;


    @Override
    /**
     * fragment used for displaying accepted books of the owner
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_mybook_accepted
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mybook_accepted, container, false);
        // get view of four buttons
        availableButton = view.findViewById(R.id.button_available);
        borrowedButton = view.findViewById(R.id.button_borrowed);
        requestedButton = view.findViewById(R.id.button_requested);
        fs=new FireStoreHelper(getActivity());
        availableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAvailableFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_accepted_to_mybook_available);
            }
        });

        borrowedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookBorrowedFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_accepted_to_mybook_borrowed);
            }
        });

        requestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to MyBookRequestedFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_accepted_to_mybook_requested);
            }
        });

        addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mybook_to_newbook);
            }
        });



        // Inflate the layout for this fragment
        //View root = inflater.inflate(R.layout.fragment_mybook, container, false);
        rv = view.findViewById(R.id.rv_1);
        //initilize test array and adapter
        final ArrayList<Book> dataList = new ArrayList<>();
        //set up manager and adapter to contain data
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //setting the separate line
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        //adapter operation
        bAdapter = new BookAdapter(getActivity(), dataList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                Book bookCliced=dataList.get(pos);
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
                Navigation.findNavController(view).navigate(R.id.action_mybook_accepted_to_confirm,bundle);
            }
        });
        rv.setAdapter(bAdapter);


        fs.fetch_MyBook("ACCEPTED", new dbCallback() {
                    @Override
                    public void onCallback(Map map) {
                        String title=map.get("title").toString();
                        String ISBN=map.get("ISBN").toString();
                        String author=map.get("author").toString();
                        String description=map.get("description").toString();
                        String ownerName=map.get("ownerName").toString();
                        //System.out.println(title);
                        Book b= new Book(title,author,ISBN,description,ownerName,null);
                        dataList.add(b);
                        bAdapter.notifyDataSetChanged();
                    }
                }
        );
        

        return view;
    }







}