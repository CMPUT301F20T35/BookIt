package com.example.bookit;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
                // scan code functionality
//                fs.To_borrowed(isbn);
                fs.checkHandProcess(isbn, new dbCallback() {
                    @Override
                    public void onCallback(Map map) {
                        if ((Boolean) map.get("borrowProcess")) {
                            scan();
                        } else {
                            Toast.makeText(getActivity(), "Owner has not handed yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // set listener for location button
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fs.fetch_location(isbn, new dbCallback() {
                            @Override
                            public void onCallback(Map map) {
                                GeoPoint location= (GeoPoint) map.get("location");
                                if(location!=null){
                                    Bundle bundle = new Bundle();
                                    bundle.putDouble("lat",location.getLatitude());
                                    bundle.putDouble("long",location.getLongitude());
                                    Navigation.findNavController(v).navigate(R.id.fragment_borrow_location_to_view_map_fragment,bundle);

                                }
                            }
                        }
             );

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

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(BorrowLocationFragment.this);
        integrator.setCaptureActivity(CodeCapture.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scanning");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                checkISBN(result.getContents());

            } else {
                Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void checkISBN(String ISBNtoCheck) {
        fs=new FireStoreHelper(getActivity());
        if(ISBNtoCheck.equals(isbn)) {
            fs.updateBorrowProcess("borrower", ISBNtoCheck, new dbCallback() {
                @Override
                public void onCallback(Map map) {
                    getActivity().onBackPressed();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Wrong book", Toast.LENGTH_SHORT).show();
        }
    }

}
