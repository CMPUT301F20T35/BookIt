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
        System.out.println("very start");
        bookPager = view.findViewById(R.id.newBookPager);
        loadCards();


        return view;
    }

    private void loadCards() {
        imgArrayList = new ArrayList<>();
        System.out.println("very 1");
        imgArrayList.add(R.drawable.avator);
        imgArrayList.add(R.drawable.background);
        System.out.println("very 2");
        bookImgAdapter = new BookImageAdapter(getContext(), imgArrayList);
        System.out.println(imgArrayList);
        bookPager.setAdapter(bookImgAdapter);
        System.out.println("very 4");
        bookPager.setPadding(100, 0, 100, 0);
    }
}