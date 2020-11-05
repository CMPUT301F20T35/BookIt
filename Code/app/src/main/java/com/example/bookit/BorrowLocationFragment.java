package com.example.bookit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class BorrowLocationFragment extends Fragment {
    private Button locationButton;
    private Button borrowButton;
    private ImageView backButton;
    private TextView ownerDetail;
    FireStoreHelper fs;
    private String isbn;
    private String title;
    private String description;
    private String owner;
    private String author;
    private TextView titleView;
    private TextView ownerView;
    private TextView isbnView;
    private TextView descriptionView;
    //private TextView authorView;
    @Override
    /**
     * fragment used for borrower confirming location of the hand off
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_borrow_location
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_borrow_location, container, false);
        // find view by ids
        fs=new FireStoreHelper(getActivity());
        titleView=view.findViewById(R.id.tv_book_title);
        ownerView=view.findViewById(R.id.tv_owner_name);
        isbnView=view.findViewById(R.id.tv_IBSN_number);
        descriptionView=view.findViewById(R.id.tv_description);
        //authorView=view.findViewById(R.id.author_text);
        locationButton = view.findViewById(R.id.bt_location);
        borrowButton = view.findViewById(R.id.bt_borrow);
        backButton = view.findViewById(R.id.bt_back);
        //ownerDetail = view.findViewById(R.id.tv_owner_name);
        Bundle b=getArguments();
        isbn=b.getString("isbn");
        title=b.getString("title");
        description=b.getString("description");
        owner=b.getString("owner");
        isbn=b.getString("isbn");
        author=b.getString("author");
        titleView.setText(title);
        ownerView.setText(owner);
        isbnView.setText(isbn);
        descriptionView.setText(description);
        //authorView.setText(author);
        // set listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to previous fragment
                getActivity().onBackPressed();
            }
        });

        // set listener for borrow button
        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // scan code functionality need to be implemented here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                fs.To_borrowed(isbn);
            }
        });

        // set listener for location button
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location=new Location(-113.525995,53.523454);//hard coded edmonton location
                if(location!=null){
                    Bundle bundle = new Bundle();
                    Log.d("faedfa",Double.toString(location.getLatitude()));
                    bundle.putDouble("lat",location.getLatitude());
                    bundle.putDouble("long",location.getLongitude());
                    Navigation.findNavController(v).navigate(R.id.fragment_borrow_location_to_view_map_fragment,bundle);

                }
            }
        });

        // see owner detail
        ownerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                b.putString("username","lil-xiu");
                Navigation.findNavController(view).navigate(R.id.action_fragment_borrow_location_to_owner_detail2,b);
            }
        });

        return view;
    }
}
