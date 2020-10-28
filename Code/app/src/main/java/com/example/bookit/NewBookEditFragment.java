package com.example.bookit;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class NewBookEditFragment extends Fragment {

    private ViewPager bookPager;
    private ArrayList<Integer> imgArrayList;
    private BookImageAdapter bookImgAdapter;

    private EditText newBookTitleET;
    private EditText newBookAuthorET;
    private EditText newBookISBNET;
    private EditText newBookDescriptionET;
    private Button addNewBook;
    AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_book_edit, container, false);
        bookPager = view.findViewById(R.id.newBookPager);
        loadCards();

        newBookTitleET = view.findViewById(R.id.newBookTitle);
        newBookAuthorET = view.findViewById(R.id.newBookAuthor);
        newBookISBNET = view.findViewById(R.id.newBookISBN);
        newBookDescriptionET = view.findViewById(R.id.newBookDescription);
        addNewBook = view.findViewById(R.id.addNewBookBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
        builder.setCancelable(true);
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));

        dialog = builder.create();

        addNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // try to add new book, if success, go back mybook page
                // if fail, Toast message
            }
        });

        return view;
    }

    private void loadCards() {
        imgArrayList = new ArrayList<>();
        imgArrayList.add(R.drawable.avator);
        imgArrayList.add(R.drawable.add_img);

//        imgArrayList.add(R.drawable.background);
        bookImgAdapter = new BookImageAdapter(getContext(), imgArrayList);
        System.out.println(imgArrayList);
        bookPager.setAdapter(bookImgAdapter);
        bookPager.setPadding(100, 0, 100, 0);
    }
}