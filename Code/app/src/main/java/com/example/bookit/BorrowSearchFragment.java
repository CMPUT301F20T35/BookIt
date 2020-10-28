package com.example.bookit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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

        //set up recycler view for book data
        final ArrayList<Book> testList = new ArrayList<Book>();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);
        bAdapter = new BookAdapter(getActivity(), testList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                // switch to book request fragment
                Toast.makeText(getActivity(),"Testing"+pos, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_borrow_search_to_book_request);
            }
        });
        rv.setAdapter(bAdapter);


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
                // searching method need to be implemented here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                testList.add(new Book(searInfo, "zhengyao", "123", "haha", "bingshen", null));
                bAdapter.notifyDataSetChanged();
                searchText.setText("");
            }
        });

        return view;
    }
}
