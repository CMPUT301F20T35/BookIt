package com.example.bookit;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class MyBookBorrowedFragment extends Fragment {
    private Button acceptedButton;
    private Button availableButton;
    private Button requestedButton;
    FireStoreHelper fs;
    private RecyclerView rv;
    private BookAdapter bAdapter;
    private FloatingActionButton addButton;

    @Override
    /**
     * fragment used for displaying borrowed books of the owner
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_mybook_borrowed
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mybook_borrowed, container, false);
        // get view of four buttons
        acceptedButton = view.findViewById(R.id.button_accepted);
        availableButton = view.findViewById(R.id.button_available);
        requestedButton = view.findViewById(R.id.button_requested);
        fs=new FireStoreHelper(getActivity());
        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAcceptedFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_borrowed_to_mybook_accepted);
            }
        });

        availableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAvailableFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_borrowed_to_mybook_available);
            }
        });

        requestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to MyBookRequestedFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_borrowed_to_mybook_requested);
            }
        });

        addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mybook_to_newbook);
            }
        });

        // Inflate the layout for this fragment
        //View root = inflater.inflate(R.layout.fragment_mybook, container, false);
        rv = view.findViewById(R.id.rv_1);
        //initilize test array and adapter
        final ArrayList<Book> dataList = new ArrayList<Book>();
        //set up manager and adapter to contain data
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //setting the separate line
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        //adapter operation
        bAdapter = new BookAdapter(getActivity(), dataList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                Book bookGet = bAdapter.getBookObject(pos);
                String isbn=bookGet.getISBN();
                String des=bookGet.getDescription();
                String title=bookGet.getTitle();
                String owner=bookGet.getOwnerName();
                String author = bookGet.getAuthor();
                String aceepted=bookGet.getRequests().getAcceptedRequestor();
                RequestHandler rh = new RequestHandler();
                Bundle bundle=new Bundle();

                fs.fetch_MyBookRequest(title, new dbCallback() {
                    @Override
                    public void onCallback(Map map) {
                        final RequestHandler rh = (RequestHandler) map.get("requestHandler");
                        bundle.putSerializable("rh",rh);
                        bundle.putString("isbn",isbn);
                        bundle.putString("description",des);
                        bundle.putString("title",title);
                        bundle.putString("author",author);
                        bundle.putString("borrower",aceepted);
                        bundle.putString("owner",owner);
                        bundle.putSerializable("rh", rh);
                        bundle.putString("is_borrowed","false");

                        Navigation.findNavController(view).navigate(R.id.action_mybook_borrowed_to_book_return,bundle);
                    }
                });

            }

        });
        rv.setAdapter(bAdapter);

        fs.fetch_MyBook("BORROWED", new dbCallback() {
                    @Override
                    public void onCallback(Map map) {
                        String title=map.get("title").toString();
                        String ISBN=map.get("ISBN").toString();
                        String author=map.get("author").toString();
                        String description=map.get("description").toString();
                        String ownerName=map.get("ownerName").toString();
                        String borrower=map.get("acceptedRequestor").toString();

                        Book b= new Book(title,author,ISBN,description,ownerName,new RequestHandler(new BookState(),new ArrayList<String>(),borrower));
                        dataList.add(b);
                        bAdapter.notifyDataSetChanged();
                    }
                }
        );

        fs.fetch_owner_confirmed_book(new dbCallback() {
            @Override
            public void onCallback(Map map) {
                String title=map.get("title").toString();
                String ISBN=map.get("ISBN").toString();
                String author=map.get("author").toString();
                String description=map.get("description").toString();
                String ownerName=map.get("ownerName").toString();
                Book b= new Book(title,author,ISBN,description,ownerName,null);
                dataList.add(b);
                bAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }


    //decoration part for recycler view
    class MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }

}