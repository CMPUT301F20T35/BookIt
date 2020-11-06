package com.example.bookit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BorrowerRequestDetail extends Fragment {

    boolean alertReady;
    private static final int PICK_IMAGE = 1;
    private Uri MediaUri;
    private ArrayList<Uri> imgArrayList;
    private ArrayList<Bitmap> imgArrayList2;
    //private ViewPager bookPager;
    private BookImageAdapter bookImgAdapter;
    private ImageButton editImage;
    private ImageButton delete;
    private ImageButton editInfo;
    private Button Back;
    private TextView titleView;
    private TextView ownerView;
    private TextView isbnView;
    private TextView descriptionView;
    private TextView authorView;
    protected ImageView imageView;
    FireStoreHelper db;
    private String isbn;
    private String title;
    private String description;
    private String owner;
    private String author;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_borrow_requestdetail, container, false);
        titleView=v.findViewById(R.id.title);
        ownerView=v.findViewById(R.id.Ownername);
        isbnView=v.findViewById(R.id.isbn_text);
        descriptionView=v.findViewById(R.id.description);
        authorView=v.findViewById(R.id.author_text);
        Bundle b=getArguments();
        isbn=b.getString("isbn");
        title=b.getString("title");
        description=b.getString("description");
        owner=b.getString("owner");
        isbn=b.getString("isbn");
        author=b.getString("author");
        //set text
        titleView.setText(title);
        ownerView.setText(owner);
        isbnView.setText(isbn);
        descriptionView.setText(description);
        authorView.setText(author);


        imageView = v.findViewById(R.id.imageView4);
        imageView.setImageResource(R.drawable.add_img);


        try {
            db=new FireStoreHelper(getActivity());
            db.load_book_image(isbn, new dbCallback() {
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
                    imageView.setImageBitmap(img);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }






        Back=v.findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add image to firestore if the image list is changed
                getActivity().onBackPressed();
            }
        });
        return v;

    }

    /**
     * update the info format checker
     * @param book
     * @return if the updated info is correct format
     */
    private boolean bookInfoValidator(Book book) {
        String regex = "^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(book.getISBN());
        if (book.getAuthor().isEmpty() || book.getISBN().isEmpty() || book.getTitle().isEmpty()|| book.getOwnerName().isEmpty()) {
            return false;
        }
        return true;
    }
    //this method is for choosing a new image for profile
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
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
                db.book_image_update(MediaUri,isbn);
                imageView.setImageURI(MediaUri);
                SharedPreferences.Editor prefEditor = getContext().
                        getSharedPreferences("Profile", Context.MODE_PRIVATE)
                        .edit();
                prefEditor.clear().commit();
                Toast.makeText(getActivity(), "upload image successfully", Toast.LENGTH_LONG).show();
            }
        }

    }

}
