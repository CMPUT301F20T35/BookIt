package com.example.bookit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class BookReturnFragment extends Fragment {
    private TextView ownerDetail;
    private Button returnBook;
    private ImageView backButton;

    @Override
    /**
     * fragment used for returning book
     * @see fragment corresponding to layout file fragment_book_return
     * @return view of the fragment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book_return, container, false);

        returnBook = view.findViewById(R.id.button_return);
        returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // return functionality need to be implemented here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                getActivity().onBackPressed();

            }
        });

        ownerDetail = (TextView) view.findViewById(R.id.owner_name);
        ownerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_book_detail_to_owner_detail);
            }
        });

        backButton = view.findViewById(R.id.bt_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
