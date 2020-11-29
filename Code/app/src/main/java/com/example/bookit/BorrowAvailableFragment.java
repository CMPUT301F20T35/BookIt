package com.example.bookit;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class BorrowAvailableFragment extends Fragment {
    private Button acceptedButton;
    private Button borrowedButton;
    private Button requestedButton;
    private RecyclerView rv;
    FireStoreHelper fs;
    private BookAdapter bAdapter;
    private ImageButton searchButton;
  
    @Override
    /**
     * fragment used for displaying books being available and borrower wants to borrow
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_borrow_available
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_borrow_available, container, false);
        // get view of four buttons
        acceptedButton = view.findViewById(R.id.button_accepted);
        borrowedButton = view.findViewById(R.id.button_borrowed);
        requestedButton = view.findViewById(R.id.button_requested);
        searchButton = view.findViewById(R.id.button_search);
        fs=new FireStoreHelper(getActivity());

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

        //set search button function
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // switch to BorrowSearchFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_available_to_borrow_search);
            }
        });

        // Inflate the layout for this fragment
        //View root = inflater.inflate(R.layout.fragment_mybook, container, false);
        rv = view.findViewById(R.id.rv_1);
        //initilize test array and adapter
        final ArrayList<Book> testList = new ArrayList<Book>();
        //testList.add(new Book("title", "author", "ISBN", "descr", "ybs", null));
        //set up manager and adapter to contain data
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //setting the separate line
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        //adapter operation
        bAdapter = new BookAdapter(getActivity(), testList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                if (bAdapter.getBookObject(pos).getRequests() != null && bAdapter.getBookObject(pos).getRequests().getReturnProcess()) {
                    Toast.makeText(getActivity(),"not finished return process yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(),"Testing"+pos, Toast.LENGTH_SHORT).show();

                // get current clicked book info
                Book bookGet = bAdapter.getBookObject(pos);
                String isbn = bookGet.getISBN();
                String des = bookGet.getDescription();
                String title = bookGet.getTitle();
                String owner = bookGet.getOwnerName();
                String author = bookGet.getAuthor();
                RequestHandler rh = new RequestHandler();

                // put info into bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("rh",rh);
                bundle.putString("isbn",isbn);
                bundle.putString("description",des);
                bundle.putString("title",title);
                bundle.putString("author",author);
                bundle.putString("owner",owner);

                Navigation.findNavController(view).navigate(R.id.action_borrow_available_to_book_request, bundle);
            }
        });
        rv.setAdapter(bAdapter);

        fs.fetch_AvailableBook( new dbCallback(){
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
        }, null);

        fs.fetch_RequestBorrowedBook( new dbCallback(){
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
        }, null);


        fs.fetch_borrower_confirmed_book(new dbCallback() {
            @Override
            public void onCallback(Map map) {
                String title=map.get("title").toString();
                String ISBN=map.get("ISBN").toString();
                String author=map.get("author").toString();
                String description=map.get("description").toString();
                String ownerName=map.get("ownerName").toString();
                Book b= new Book(title,author,ISBN,description,ownerName,new RequestHandler(new BookState(),new ArrayList<String>(), "", false, true));
                testList.add(b);
                bAdapter.notifyDataSetChanged();
            }
        });


        //set search button function
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // switch to BorrowSearchFragment
                Navigation.findNavController(view).navigate(R.id.action_borrow_available_to_borrow_search);
            }
        });

        return view;
    }

    


}