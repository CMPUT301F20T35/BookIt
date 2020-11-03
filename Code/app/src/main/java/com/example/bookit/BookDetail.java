package com.example.bookit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;


public class BookDetail extends Fragment {


    private static final int PICK_IMAGE = 1;
    private Uri MediaUri;
    private ArrayList<Uri> imgArrayList;
    private ArrayList<Bitmap> imgArrayList2;
    //private ViewPager bookPager;
    private BookImageAdapter bookImgAdapter;
    private ImageButton editImage;
    private Button finish;
    protected ImageView imageView;
    FireStoreHelper db;
    private String isbn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_book_detail, container, false);
        editImage=v.findViewById(R.id.editimage2);
        //bookPager=v.findViewById(R.id.BookPager);
        imgArrayList = new ArrayList<>();
        imgArrayList2 = new ArrayList<>();

        Bundle b=getArguments();
        isbn=b.getString("isbn");

        imageView = v.findViewById(R.id.imageView4);

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

        finish=v.findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add image to firestore if the image list is changed
                getActivity().onBackPressed();
            }
        });
        return v;

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