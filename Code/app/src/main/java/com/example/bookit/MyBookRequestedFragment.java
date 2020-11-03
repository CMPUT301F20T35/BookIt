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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class MyBookRequestedFragment extends Fragment {
    private Button acceptedButton;
    private Button availableButton;
    private Button borrowedButton;
    FireStoreHelper fs;
    private RecyclerView rv;
    private BookAdapter bAdapter;
    private FloatingActionButton addButton;

    @Override
    /**
     * fragment used for displaying requested books of the owner
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_mybook_requested
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mybook_requested, container, false);
        // get view of four buttons
        acceptedButton = view.findViewById(R.id.button_accepted);
        availableButton = view.findViewById(R.id.button_available);
        borrowedButton = view.findViewById(R.id.button_borrowed);
        fs=new FireStoreHelper(getActivity());
        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAcceptedFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_requested_to_mybook_accepted);
            }
        });

        availableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookAvailableFragment
                Navigation.findNavController(view).navigate(R.id.action_mybook_requested_to_mybook_available);
            }
        });

        borrowedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to MyBookBorrowedFragment


                Navigation.findNavController(view).navigate(R.id.action_mybook_requested_to_mybook_borrowed);
            }
        });



        // Inflate the layout for this fragment
        //View root = inflater.inflate(R.layout.fragment_mybook, container, false);
        rv = view.findViewById(R.id.rv_1);

        //initilize test array and adapter

        final ArrayList<Book> testList = new ArrayList<Book>();

        //set up manager and adapter to contain data
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setting the separate line
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);

        addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Book book = new Book("ybs","YBS","123","optional","ybs",null);
                testList.add(book);
                bAdapter.notifyDataSetChanged();

                //this is xiu's code, the above code is used for testing, delete them later and replace with the bottom cde
                //Navigation.findNavController(view).navigate(R.id.action_mybook_to_newbook);
            }
        });

        bAdapter = new BookAdapter(getActivity(), testList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                //on item click listener
                Toast.makeText(getActivity(),"Testing"+pos, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_mybook_toRequestList);

            }


        });
        rv.setAdapter(bAdapter);
        fs.fetch_MyBook("REQUESTED", new dbCallback() {
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
                }

///////////////////////////////////////////////////////////////////////////////////////////////////////b

        );



        //set swipe delete function
        enableSwipeToDeleteAndUndo();
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


    private void enableSwipeToDeleteAndUndo(){
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Book item = bAdapter.getBookData().get(position);
                bAdapter.removeItem(position);


//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
//
//
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rv);
    }
}