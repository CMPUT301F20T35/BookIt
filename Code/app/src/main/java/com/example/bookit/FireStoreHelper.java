package com.example.bookit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ForwardingListeningExecutorService;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FireStoreHelper {
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    Context context;
    boolean isSuccessful=false;
    private StorageReference mstore;

    public FireStoreHelper(ProfileFragment profileFragment) {
    }

    public FireStoreHelper( Context context) {
        this.context =context;
    }
    /**
     * used to authenticate the email and password
     * @param email,password,progressBar
     * @return a bool indicating if login successfully
     * */
    public boolean loginAuth(String email, String password, final ProgressBar progressBar)
    {

        progressBar.setVisibility(View.VISIBLE);

        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Toast.makeText(context, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context,MainActivity.class));
                ((Activity) context).finish();
                isSuccessful=true;
            }else {
                Toast.makeText(context, "Error ! Wrong Email or password !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                isSuccessful=false;
            }
        }
    });return isSuccessful;
    }

    /**
     * used to sign out of the user
     * */
    public void logout() {
        fAuth=FirebaseAuth.getInstance();
        fAuth.signOut();//logout
        context.startActivity(new Intent(context,Login.class));
        ((Activity) context).finish();//end the MainActivity so that user is unable to go back
    }

    /**
     * used to add a valid book to the firestore
     * */
    public void addBook(Book book){
        db = FirebaseFirestore.getInstance();
        final String stateId;

        Map<String, Object> bookHash = new HashMap<>();
        bookHash.put("author",book.getAuthor());
        bookHash.put("ISBN",book.getISBN());
        bookHash.put("description",book.getDescription());
        bookHash.put("ownerName",book.getOwnerName());
        bookHash.put("title",book.getTitle());
        final RequestHandler r=book.getRequests();


        try{
            db.collection("Book").document(book.getISBN()).set(r);
            db.collection("Book").document(book.getISBN()).update(bookHash)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
        }catch (IllegalArgumentException e){
            Toast.makeText(context.getApplicationContext(),"invalid argument,fail to add new book",Toast
                    .LENGTH_SHORT).show();
        }

    }

    public void signUp(final Map newUser, final ProgressBar progressBar) {
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fAuth.createUserWithEmailAndPassword(newUser.get("email").toString()
                , newUser.get("password").toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = fAuth.getCurrentUser();
                            newUser.put("id", user.getUid());

                            final CollectionReference collectionReference =
                                    db.collection("User");
                            collectionReference.
                                    document(user.getUid()).
                                    set(newUser);
                            Toast.makeText(context, "Sign up Successfully", Toast.LENGTH_SHORT).show();
                            ((Activity) context).finish();
                        }else {
                            Toast.makeText(context, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                            }
                        });

    }
    public void Fetch(final dbCallback callback, AlertDialog dialog){
        dialog.show();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, String> returnMap = new HashMap<>();
                        returnMap.put("username", (String) document.get("username"));
                        returnMap.put("contactInfo", (String) document.get("number"));
                        returnMap.put("email", (String) document.get("email"));

                        callback.onCallback(returnMap);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void update(String username,String contactIfo){
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        if (username.equals("")){}
        else{

            final CollectionReference collectionReference = db.collection("User");
            collectionReference.
                    document(user.getUid()).update("username",username,"number",contactIfo);

        }

    }
    public void image_update(Uri u){
        //ImageView image=v.findViewById(R.id.imageView5);
        fAuth = FirebaseAuth.getInstance();
        mstore= FirebaseStorage.getInstance().getReference();
        FirebaseUser user = fAuth.getCurrentUser();
        String name="current";
        StorageReference storageReference=mstore.child("images/"+user.getUid()+"/"+name+".jpg");
        storageReference.putFile(u);
    }

    /**
     * for adding images to the firebase
     * @param u,book
     */
    public void book_image_add(ArrayList<Uri> u,Book book) {
        mstore= FirebaseStorage.getInstance().getReference();
        StorageReference storageReference;
        int k=0;
        if (u.size()!=0){
            for (Uri i:u){
                k++;
                storageReference=mstore.child("book_images/"+book.getISBN()+"/image"+k+".jpg");
                storageReference.putFile(i);
            }

        }}


    public void load_image(final ImageView v) throws IOException {

        fAuth = FirebaseAuth.getInstance();
        FirebaseStorage mstore = FirebaseStorage.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        String name = "current";
        StorageReference storageReference=mstore.getReferenceFromUrl("gs://bookit-fc94f.appspot.com/").child("images/"+user.getUid()+"/"+name+".jpg");

            final File f = File.createTempFile("image", "jpg");
            storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
                    v.setImageBitmap(b);
                }
            });

    }





    //public void update(){}

    public FirebaseAuth getfAuth(){
        return fAuth;
    }

    public void setfAuth(FirebaseAuth fAuth) {
        this.fAuth = fAuth;
    }


}
