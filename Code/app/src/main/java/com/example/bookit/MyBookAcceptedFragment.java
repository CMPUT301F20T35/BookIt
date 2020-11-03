package com.example.bookit;

import android.app.AlertDialog;
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
    FireStoreHelper fs;
    private RecyclerView rv;
    private BookAdapter bAdapter;
    private FloatingActionButton addButton;
    private ImageButton scanBtn;

    @Override
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

        scanBtn = view.findViewById(R.id.ownerScanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
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

        //adapter operation
        bAdapter = new BookAdapter(getActivity(), testList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                Toast.makeText(getActivity(),"Testing"+pos, Toast.LENGTH_SHORT).show();
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


    private void scan() {

        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(MyBookAcceptedFragment.this);
        integrator.setCaptureActivity(CodeCapture.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scanning");
        integrator.initiateScan();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(123);
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(result.getContents());
                builder.setPositiveButton("scan again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scan();
                    }
                });
                builder.setNegativeButton("finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        getActivity().onBackPressed();
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
            }

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