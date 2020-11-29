package com.example.bookit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.Map;

public class BookReturnFragment extends Fragment {
    private TextView ownerDetail;


    private TextView title;
    private TextView owner;
    private TextView isbn;
    private TextView author;
    private TextView description;
    private TextView borrower;
    private TextView textView5;
    private Button returnBook;
    private ImageView backButton;
    protected ImageView imageView1;

    private String ISBN;
    FireStoreHelper fs;

    @Override
    /**
     * fragment used for returning book
     * @see fragment corresponding to layout file fragment_book_return
     * @return view of the fragment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book_return, container, false);
        fs=new FireStoreHelper(getActivity());
        Bundle bundle = getArguments();
        title = view.findViewById(R.id.bookTitle);
        isbn = view.findViewById(R.id.isbn_name);
        owner = view.findViewById(R.id.owner_name);
        author = view.findViewById(R.id.bookAuthor_name);
        description = view.findViewById(R.id.Description);
        borrower=view.findViewById(R.id.borrower);
        textView5=view.findViewById(R.id.textView5);
        imageView1 = view.findViewById(R.id.imageView);

        ISBN = bundle.getString("isbn");
        isbn.setText(ISBN);
        description.setText(bundle.getString("description"));
        title.setText(bundle.getString("title"));
        author.setText(bundle.getString("author"));
        owner.setText(bundle.getString("owner"));
        borrower.setText(bundle.getString("borrower"));
        if (bundle.getString("is_borrowed").equals("true")){
            textView5.setText("");
        }




        returnBook = view.findViewById(R.id.button_return);
        returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // borrower return scan function
                scan();

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

        try {
            fs.load_book_image(ISBN, new dbCallback() {
                @Override
                public void onCallback(Map map) {
                    String imageEncoded = (String) map.get("bookimage");
                    byte[] decodedByte = Base64.decode(imageEncoded, 0);
                    Bitmap img = BitmapFactory
                            .decodeByteArray(decodedByte, 0, decodedByte.length);
                    imageView1.setImageBitmap(img);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(BookReturnFragment.this);
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
        if(ISBNtoCheck.equals(ISBN)) {
            fs.updateReturnProcess("borrower", ISBNtoCheck, new dbCallback() {
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
