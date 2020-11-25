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


    private TextView title;
    private TextView owner;
    private TextView isbn;
    private TextView author;
    private TextView description;
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
        Bundle bundle = getArguments();
        title = view.findViewById(R.id.bookTitle);
        isbn = view.findViewById(R.id.isbn_name);
        owner = view.findViewById(R.id.owner_name);
        author = view.findViewById(R.id.bookAuthor_name);
        description = view.findViewById(R.id.Description);

        isbn.setText(bundle.getString("isbn"));
        description.setText(bundle.getString("description"));
        title.setText(bundle.getString("title"));
        author.setText(bundle.getString("author"));
        owner.setText(bundle.getString("owner"));

        returnBook = view.findViewById(R.id.button_return);
        returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // return functionality need to be implemented here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                getActivity().onBackPressed();

            }
        });

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", owner.getText().toString());
                Navigation.findNavController(view).navigate(R.id.action_book_detail_to_owner_detail, bundle);

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
