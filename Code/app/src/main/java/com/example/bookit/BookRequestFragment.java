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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BookRequestFragment extends Fragment {
    private Button requestButton;
    private ImageView backButton;
    private TextView bookTitle;
    private TextView ownerName;
    private TextView bookISBN;
    private TextView bookDescription;

    private String isbn;
    private String des;
    private String title;
    private String owner;
    private String author;
    private RequestHandler rh;

    FireStoreHelper fs;

    @Override
    /**
     * fragment used for requesting book
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_book_request
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book_request, container, false);
        // find view by ids
        requestButton = view.findViewById(R.id.bt_request);
        backButton = view.findViewById(R.id.bt_back);
        bookTitle = view.findViewById(R.id.tv_book_title);
        ownerName = view.findViewById(R.id.tv_owner_name);
        bookISBN = view.findViewById(R.id.tv_IBSN_number);
        bookDescription = view.findViewById(R.id.tv_description);

        // get current book info from bundle
        Bundle b = getArguments();
        isbn = b.getString("isbn");
        des = b.getString("des");
        title = b.getString("title");
        owner = b.getString("owner");
        author = b.getString("author");
        rh = (RequestHandler) b.getSerializable("rh");

        // set texts
        bookTitle.setText(title);
        ownerName.setText(owner);
        bookISBN.setText(isbn);
        bookDescription.setText(des);

        fs = new FireStoreHelper(getActivity());

        // set listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        // set listener for owner name
        ownerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to owner detail fragment
                Bundle bundle = new Bundle();
                bundle.putString("username", ownerName.getText().toString());
                Navigation.findNavController(view).navigate(R.id.action_book_request_to_owner_detail, bundle);
            }
        });

        // set listener for request button
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // borrower request a book
                Notification n=new Notification(title,isbn,owner,new ArrayList<String>(),"","REQUEST_ACCEPTED");
                fs.borrowerRequestBook(bookISBN.getText().toString(),bookTitle.getText().toString(),ownerName.getText().toString(),n);
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
