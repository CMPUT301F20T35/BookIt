package com.example.bookit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OwnerHandOverFragment extends Fragment {
    private Button handOverBtn;
    private ImageView backButton;
    private TextView authorView;
    FireStoreHelper fs;
    private String isbn;
    private String title;
    private String description;
    private String author;
    private ImageView im;
    private TextView titleView;
    private TextView isbnView;
    private TextView descriptionView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner_hand_over, container, false);
        // find view by ids
        fs=new FireStoreHelper(getActivity());
        backButton = view.findViewById(R.id.bt_back);
        titleView=view.findViewById(R.id.tv_book_title);
        isbnView=view.findViewById(R.id.tv_IBSN_number);
        descriptionView=view.findViewById(R.id.tv_description);
        //authorView=view.findViewById(R.id.author_text);
        handOverBtn = view.findViewById(R.id.confirm);
        im=view.findViewById(R.id.iv_book);
        authorView = view.findViewById(R.id.authorName);
        Bundle b=getArguments();
        isbn=b.getString("isbn");
        title=b.getString("title");
        description=b.getString("description");
        isbn=b.getString("isbn");
        author=b.getString("author");
        titleView.setText(title);
        isbnView.setText(isbn);
        descriptionView.setText(description);
        authorView.setText(author);


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
            public void onClick(View view) {
                // back to previous fragment
                getActivity().onBackPressed();
            }
        });

        handOverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
        return view;
    }

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(OwnerHandOverFragment.this);
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
            fs.updateBorrowProcess("owner", ISBNtoCheck, new dbCallback() {
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