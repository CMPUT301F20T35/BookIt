package com.example.bookit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class NewBookEditFragment extends Fragment {

    private ViewPager bookPager;
    private ArrayList<Integer> imgArrayList;
    private BookImageAdapter bookImgAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_book_edit, container, false);
        bookPager = view.findViewById(R.id.newBookPager);
        loadCards();


        return view;
    }

    private void loadCards() {
        imgArrayList = new ArrayList<>();
        imgArrayList.add(R.drawable.avator);
        imgArrayList.add(R.drawable.background);
        bookImgAdapter = new BookImageAdapter(getContext(), imgArrayList);
        System.out.println(imgArrayList);
        bookPager.setAdapter(bookImgAdapter);
        bookPager.setPadding(100, 0, 100, 0);
    }
}