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


public class BookDetail extends Fragment {

    private boolean alertReady;

    private static final int PICK_IMAGE = 1;
    private Uri MediaUri;
    private ArrayList<Uri> imgArrayList;
    private ArrayList<Bitmap> imgArrayList2;
    //private ViewPager bookPager;
    private BookImageAdapter bookImgAdapter;
    private ImageButton editImage;
    private ImageButton delete;
    private ImageButton editInfo;
    private Button finish;
    private TextView titleView;
    private TextView ownerView;
    private TextView isbnView;
    private TextView descriptionView;
    private TextView authorView;

    private ImageView imageView;
    private FireStoreHelper db;

    private String isbn;
    private String title;
    private String description;
    private String owner;
    private String author;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_book_detail, container, false);
        editImage=v.findViewById(R.id.editimage2);
        delete=v.findViewById(R.id.deleteButton);
        titleView=v.findViewById(R.id.title);
        ownerView=v.findViewById(R.id.Ownername);
        isbnView=v.findViewById(R.id.isbn_text);
        descriptionView=v.findViewById(R.id.description);
        authorView=v.findViewById(R.id.author_text);
        editInfo=v.findViewById(R.id.editbookinfo);

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

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delete_book_image(isbn);
                imageView.setImageResource(R.drawable.add_img);
            }
        });
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Update your Book");
                View v = LayoutInflater.from(getContext()).inflate(R.layout.edit_book_alertdialog, null, false);
                builder.setView(v);
                EditText editTitle=v.findViewById(R.id.edit_title);
                EditText editAuthor=v.findViewById(R.id.edit_author);
                EditText editDes=v.findViewById(R.id.edit_des);
                EditText editIsbn=v.findViewById(R.id.edit_isbn);

                editAuthor.setText(authorView.getText());
                editDes.setText(descriptionView.getText());
                editIsbn.setText(isbnView.getText());
                editTitle.setText(titleView.getText());

                builder.setPositiveButton("update", null);
                builder.setNegativeButton("cancel", null);

                final AlertDialog alertDialog = builder.create();
                alertReady = false;

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        if (!alertReady){
                            Button buttonPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            buttonPositive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String a=editAuthor.getText().toString().trim();
                                    String d=editDes.getText().toString().trim();
                                    String i=editIsbn.getText().toString().trim();
                                    String o=ownerView.getText().toString().trim();
                                    String t=editTitle.getText().toString().trim();
                                    Book book=new Book(t,a,i,d,o,null);
                                    Boolean isValid=bookInfoValidator(book);
                                    if (!isValid){
                                        Toast.makeText(getContext(), "wrong input please check!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        try {
                                            db.update_book_info(isbn,i,a,d,o,t);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        authorView.setText(a);
                                        descriptionView.setText(d);
                                        isbnView.setText(i);
                                        ownerView.setText(o);
                                        titleView.setText(t);
                                        alertReady=true;

                                    }
                                    if(alertReady){
                                        dialogInterface.dismiss();
                                    }
                                }
                            });
                            Button buttonNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            buttonNegative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                    }
                });
                alertDialog.show();

            }
        });


        finish=v.findViewById(R.id.Back);
        finish.setOnClickListener(new View.OnClickListener() {
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