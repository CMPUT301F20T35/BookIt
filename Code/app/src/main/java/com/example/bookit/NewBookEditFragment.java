package com.example.bookit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.okhttp.Protocol;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewBookEditFragment extends Fragment {

    private ViewPager bookPager;
    private ArrayList<Uri> imgArrayList;
    private BookImageAdapter bookImgAdapter;
    public static final int PICK_IMAGE = 1;
    private Uri MediaUri;

    private EditText newBookTitleET;
    private EditText newBookAuthorET;
    private EditText newBookISBNET;
    private EditText newBookDescriptionET;
    private FloatingActionButton addNewBook;
    private Button addImage;
    AlertDialog dialog;
    FireStoreHelper db;

    private ImageButton scanBtn;

    @Override
    /**
     * fragment used for
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_new_book_edit
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_book_edit, container, false);
        bookPager = view.findViewById(R.id.newBookPager);
        //loadCards();
        imgArrayList = new ArrayList<>();
        addImage=view.findViewById(R.id.add_book_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        newBookTitleET = view.findViewById(R.id.newBookTitle);
        newBookAuthorET = view.findViewById(R.id.newBookAuthor);
        newBookISBNET = view.findViewById(R.id.newBookISBN);
        newBookDescriptionET = view.findViewById(R.id.newBookDescription);
        addNewBook = view.findViewById(R.id.addNewBookBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
        builder.setCancelable(true);
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));

        dialog = builder.create();
        db=new FireStoreHelper(getContext());

        scanBtn = view.findViewById(R.id.ownerScanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });

        addNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // try to add new book, if success, go back mybook page
                // if fail, Toast message
                String title=newBookTitleET.getText().toString().trim();
                String author=newBookAuthorET.getText().toString().trim();
                String ISBN=newBookISBNET.getText().toString().trim();
                String desc=newBookDescriptionET.getText().toString().trim();
                String owner="";
                RequestHandler requestHandler=new RequestHandler();
                Book book= new Book(title,author,ISBN,desc,owner,requestHandler);
                if (bookInfoValidator(book)) {
                    db.addBook(book);
                    db.book_image_add(MediaUri,book);//add the image array to the firebase storage
                    getActivity().onBackPressed();
                }
                else{
                    Toast.makeText(getContext(), "wrong input please check your input!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private boolean bookInfoValidator(Book book) {
        String regex = "^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(book.getISBN());
        if (book.getAuthor().isEmpty() || book.getISBN().isEmpty() || book.getTitle().isEmpty()) {
            return false;
        }

        return true;
    }

    //this method is for choosing a new image for profile
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE);
    }

    private void scan() {

        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(NewBookEditFragment.this);
        integrator.setCaptureActivity(CodeCapture.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scanning");
        integrator.initiateScan();

    }

    private void fetchBook(dbCallback callback, String ISBN) {
        String queryString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN;
        try {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(queryString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000); // 5 seconds
                connection.setConnectTimeout(5000); // 5 seconds
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            int responseCode = connection.getResponseCode();
//            if (responseCode != 200) {
//                Toast.makeText(getActivity(), "google api fail", Toast.LENGTH_LONG).show();
//                return;
//            }
        } catch (Error e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data==null){
                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_LONG).show();
            }
            else {
                MediaUri = data.getData();
                bookImgAdapter = new BookImageAdapter(getContext(),MediaUri);
                bookPager.setAdapter(bookImgAdapter);
                bookPager.setPadding(100, 0, 100, 0);
                Toast.makeText(getActivity(), "upload image successfully", Toast.LENGTH_LONG).show();
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                    fetchBook(new dbCallback() {
                        @Override
                        public void onCallback(Map map) {

                        }
                    }, result.getContents());
                    builder.setMessage(result.getContents());
                    builder.setPositiveButton("scan again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            scan();
                        }
                    });
                    builder.setNegativeButton("finish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                        getActivity().onBackPressed();
                            dialogInterface.dismiss();
                        }
                    });
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }
}
