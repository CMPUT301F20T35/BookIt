package com.example.bookit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.IOException;
import java.util.Map;

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
    private ImageView im;
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
        im=view.findViewById(R.id.iv_book);
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
        try {
            fs.load_book_image(isbn, new dbCallback() {
                @Override
                public void onCallback(Map map) {
                    String imageEncoded = (String) map.get("bookimage");
                    SharedPreferences.Editor prefEditor = getContext().
                            getSharedPreferences("Book", Context.MODE_PRIVATE).edit();

                    prefEditor.putString("bookimg", imageEncoded);
                    prefEditor.commit();

                    byte[] decodedByte = Base64.decode(imageEncoded, 0);

                    Bitmap img = BitmapFactory
                            .decodeByteArray(decodedByte, 0, decodedByte.length);

                    im.setImageBitmap(img);

                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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

                b.putString("username",owner);

                Navigation.findNavController(view).navigate(R.id.action_fragment_borrow_location_to_owner_detail2,b);
            }
        });

        return view;
    }
}
