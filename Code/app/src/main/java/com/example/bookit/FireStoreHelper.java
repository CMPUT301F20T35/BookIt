package com.example.bookit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
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


import java.io.ByteArrayOutputStream;
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
import com.google.firebase.storage.UploadTask;


import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

import static android.content.ContentValues.TAG;

public class
FireStoreHelper {
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private Context context;
    private boolean isSuccessful=false;
    private StorageReference mstore;
    private String username;
    private int i;

    /**
     * first constructor
     * @param profileFragment
     */
    public FireStoreHelper(ProfileFragment profileFragment) {
    }

    /**
     * second constructor
     * @param context
     */
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
    });
        return isSuccessful;
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
     * @param book
     * */
    public void addBook(final Book book, dbCallback callback){
        db = FirebaseFirestore.getInstance();
        final String stateId;
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        DocumentReference docRef = db.collection("User")
                .document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot d = task.getResult();
                if (d.exists()) {
                    book.setOwnerName(d.get("username").toString());

                    DocumentReference bookReference = db.collection("Book")
                            .document(book.getISBN());
                    bookReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (! task.getResult().exists()) {
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
                                                        Map<String, String> returnMap = new HashMap<>();
                                                        callback.onCallback(returnMap);
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
                                } else {
                                    Toast.makeText(context.getApplicationContext(),"this book already existed, change a book",Toast
                                            .LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(context.getApplicationContext(),task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

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


                            mstore= FirebaseStorage.getInstance().getReference();
                            String name="current";
                            StorageReference storageReference=mstore.child("images/"+user.getUid()+"/"+name+".jpg");
                            Uri uri = Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/"+R.drawable.default_profile);
                            storageReference.putFile(uri);

                            Toast.makeText(context, "Sign up Successfully", Toast.LENGTH_SHORT).show();
                            ((Activity) context).finish();
                        }else {
                            Toast.makeText(context, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                            }
                        });

    }

    /**
     * fetch the current user information
     * @param callback
     * @param dialog
     */
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
                        dialog.dismiss();
                        Log.d(TAG, "No such document");
                    }
                } else {
                    dialog.dismiss();
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    /**
     * fetch user info according to the username
     * @param userName
     * @param callback
     */
    public void fetchUser(String userName, final dbCallback callback){
        //FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("User")
                .whereEqualTo("username", userName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, String> returnMap = new HashMap<>();
                        String id = document.getData().get("id").toString();
                        String email = document.getData().get("email").toString();
                        String number = document.getData().get("number").toString();
                        String username = document.getData().get("username").toString();
                        String name;
                        try{
                            name = document.getData().get("name").toString();
                        }catch (NullPointerException e){
                            name = "";
                        }
                        returnMap.put("id", id);
                        returnMap.put("email", email);
                        returnMap.put("name", name);
                        returnMap.put("number", number);
                        returnMap.put("username", username);
                        callback.onCallback(returnMap);
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    /**
     * update the info of the user
     * @param username
     * @param contactIfo
     */
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

    /**
     * update requestor with acceptor name
     * @param acceptor
     * @param ISBN
     */

    public void updateRequestor(String acceptor,String ISBN){
        db = FirebaseFirestore.getInstance();
        db.collection("Book").document(ISBN)
                .update("acceptedRequestor",acceptor
                        );
    }

    /**
     * update the requestor with isbn
     * @param ISBN
     * @param list
     */
    public void updateRequestors(String ISBN, ArrayList<String> list) {
        db = FirebaseFirestore.getInstance();
        db.collection("Book").document(ISBN)
                .update("requestors", list
                );
        if (list.size()==0){
            db.collection("Book").document(ISBN)
                    .update("state.bookStatus", "AVAILABLE");
        }

    }

    /**
     * add a new Notification object to the firestore.
     * @param noti
     */
    public void addNotification(Notification noti){
        String isbn=noti.getISBN();
        db = FirebaseFirestore.getInstance();
        db.collection("Notifications").document(isbn).set(noti);

    }

    /**
     *delete the notification associated with the provided isbn
     * @param isbn
     */
    public void deleteNotification(String isbn){
        db = FirebaseFirestore.getInstance();
        db.collection("Notifications").document(isbn).update("acceptedUser","");
    }

    /**
     * retrieve the notification after owner accepted the request of the borrower
     * @param callback
     */
    public void fetchBorrowerNotification(final dbCallback callback){
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db.collection("User").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                db.collection("Notifications")
                                        .whereEqualTo("acceptedUser", document.getData().get("username"))
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, String> returnMap = new HashMap<>();
                                                String isbn=document.getData().get("isbn").toString();
                                                String owner=document.getData().get("ownerName").toString();
                                                String title=document.getData().get("title").toString();
                                                String Type=document.getData().get("notificationType").toString();
                                                returnMap.put("isbn", isbn);
                                                returnMap.put("owner", owner);
                                                returnMap.put("title",title);
                                                returnMap.put("Type",Type);
                                                callback.onCallback(returnMap);
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    /**
     * remove book objects and images from firebase
     * @param book
     */
    public void removeBook(Book book) {
        String isbn = book.getISBN();
        db = FirebaseFirestore.getInstance();
        db.collection("Book").document(isbn)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        delete_book_image(isbn);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error occurs", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * update the book info on the firestore
     * @param old_isbn
     * @param i
     * @param a
     * @param d
     * @param o
     * @param t
     * @throws IOException
     */
    public void update_book_info(String old_isbn,String i,String a,String d,String o,String t) throws IOException {
        db = FirebaseFirestore.getInstance();
        mstore= FirebaseStorage.getInstance().getReference();
        if (i.equals(old_isbn)){//if user did not update isbn we can just update corrsponding fields in fireStore
            db.collection("Book").document(i)
                    .update("author",a,
                            "description",d,
                            "ownerName",o,
                            "title",t)
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

        }
        else{
            Log.d(TAG,old_isbn);
            db.collection("Book").document(old_isbn)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Map<String,Object> m=document.getData();
                            m.put("author",a);
                            m.put("ownerName",o);
                            m.put("title",t);
                            m.put("description",d);
                            m.put("ISBN",i);
                            db.collection("Book").document(i)
                                    .set(m)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("afafffa", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("afafffa", "Error writing document", e);
                                        }
                                    });
                            db.collection("Book").document(old_isbn)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });


                        } else {
                            Log.d(TAG, "No such documentfd");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            StorageReference listRef=mstore.child("book_images/"+old_isbn+"/"+"image1.jpg");
            File f = File.createTempFile("image", "jpg");
            listRef.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    StorageReference listRef2=mstore.child("book_images/"+i+"/"+"image1.jpg");
                    UploadTask uploadTask = listRef2.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });


                }
            });
            StorageReference ref=mstore.child("book_images/"+old_isbn+"/image1.jpg");
            ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });


        }
    }

    /**
     * used if the user uploads the profile image
     * @param u, the image to be uploaded
     */
    public void image_update(Uri u){
        //ImageView image=v.findViewById(R.id.imageView5);
        mstore= FirebaseStorage.getInstance().getReference();
        FirebaseUser user = fAuth.getCurrentUser();
        String name="current";
        StorageReference storageReference=mstore.child("images/"+user.getUid()+"/"+name+".jpg");
        storageReference.putFile(u);
    }

    /**
     * used to update the location to hand over the book
     * @param g
     * @param isbn
     */
    public void location_update(GeoPoint g,String isbn){
        db = FirebaseFirestore.getInstance();
        db.collection("Book").document(isbn)
                .update("state.location",g)
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
        db.collection("Book").document(isbn)
                .update("state.bookStatus","ACCEPTED")
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
    }

    /**
     * used when the owner wants to change the book image
     * @param u
     * @param isbn
     */
    public void book_image_update(Uri u,String isbn){
        mstore= FirebaseStorage.getInstance().getReference();
        StorageReference storageReference=mstore.child("book_images/"+isbn+"/"+"image1.jpg");
        storageReference.putFile(u);
    }

    /**
     * used when the owner wants to upload the book image first time
     * @param isbn, indicating what isbn is used to choose the book
     * @param callback
     * @throws IOException
     */
    public void load_book_image(String isbn,final dbCallback callback)throws IOException{
        FirebaseStorage mstore = FirebaseStorage.getInstance();
        StorageReference listRef=mstore.getReference().child("book_images/"+isbn+"/image1.jpg");

        final File f = File.createTempFile("image", "jpg");
        listRef.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] by = baos.toByteArray();
                String imageEncoded = Base64.encodeToString(by, Base64.DEFAULT);
                Map<String, String> returnMap = new HashMap<>();
                returnMap.put("bookimage", imageEncoded);

                callback.onCallback(returnMap);

            }
        });



    }
    public void delete_book_image(String isbn){
        FirebaseStorage mstore = FirebaseStorage.getInstance();
        StorageReference ref=mstore.getReference().child("book_images/"+isbn+"/image1.jpg");
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Toast.makeText(context, "Remove Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

    }




    /**
     * for adding images to the firebase
     * @param u,book
     */
    public void book_image_add(Uri u,Book book, dbCallback callback) {
        if (u!= null){
            mstore= FirebaseStorage.getInstance().getReference();
            StorageReference storageReference;
            storageReference=mstore.child("book_images/"+book.getISBN()+"/image1"+".jpg");
            storageReference
                    .putFile(u)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, String> returnMap = new HashMap<>();
                                Toast.makeText(context, "finish", Toast.LENGTH_SHORT).show();
                                callback.onCallback(returnMap);
                            }
                        }
                    });
        }
    }

    /**
     * get the user's information from firebase by the username
     * @param username, indicating what username is used to fetch
     * @param callback
     */
    public void fetch_user_withUsername(String username, final dbCallback callback){
        db = FirebaseFirestore.getInstance();
        db.collection("User").whereEqualTo("username",username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                        if(task1.isSuccessful()){
                            for (QueryDocumentSnapshot document:task1.getResult()){
                                Map<String, String> returnMap = new HashMap<>();
                                String email = document.getData().get("email").toString();
                                String number = document.getData().get("number").toString();
                                String id = document.getData().get("id").toString();
                                returnMap.put("email", email);
                                returnMap.put("number", number);
                                returnMap.put("id", id);
                                callback.onCallback(returnMap);
                            }
                        }else{}

                    }
                });
    }

    /**
     * get the user image by the id
     * @param id
     * @param callback
     * @throws IOException
     */
    public void load_image_with_id(String id,final dbCallback callback) throws IOException {

        fAuth = FirebaseAuth.getInstance();
        FirebaseStorage mstore = FirebaseStorage.getInstance();
        String name = "current";
        StorageReference storageReference=mstore.getReferenceFromUrl("gs://bookit-fc94f.appspot.com/").child("images/"+id+"/"+name+".jpg");

        final File f = File.createTempFile("image", "jpg");
        storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] by = baos.toByteArray();
                String imageEncoded = Base64.encodeToString(by, Base64.DEFAULT);
                Map<String, String> returnMap = new HashMap<>();
                returnMap.put("userimg", imageEncoded);
                callback.onCallback(returnMap);

            }
        });

    }


    /**
     * fetch the current user's image
     * @param callback
     * @throws IOException
     */
    public void load_image(final dbCallback callback) throws IOException {

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
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] by = baos.toByteArray();
                    String imageEncoded = Base64.encodeToString(by, Base64.DEFAULT);
                    Map<String, String> returnMap = new HashMap<>();
                    returnMap.put("userimg", imageEncoded);
                    callback.onCallback(returnMap);

                }
            });

    }

    /**
     * fetch books in owner's page
     * @param which, indicating which kind of book to fetch: requested, accepted, borrowed, available
     * @param callback
     */
    public void fetch_MyBook(String which ,final dbCallback callback){
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name= document.get("username").toString();
                        db.collection("Book")
                                .whereEqualTo("ownerName",name)
                                .whereEqualTo("state.bookStatus",which)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if(task1.isSuccessful()){
                                            for (QueryDocumentSnapshot document:task1.getResult()){
                                                Map<String, String> returnMap = new HashMap<>();

                                                if (which.equals("ACCEPTED") && (Boolean) document.getData().get("borrowProcess")) {
                                                    continue;
                                                }
                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();
                                                String acceptedUser=document.getData().get("acceptedRequestor").toString();

                                                a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                returnMap.put("acceptedRequestor",acceptedUser);
                                                callback.onCallback(returnMap);
                                            }
                                        }else{}

                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * fetch books the owner has confirmed to hand over but not received by the borrower
     * @param callback
     */
    public void fetch_owner_confirmed_book(final dbCallback callback) {
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name= document.get("username").toString();
                        db.collection("Book")
                                .whereEqualTo("ownerName",name)
                                .whereEqualTo("state.bookStatus", "ACCEPTED")
                                .whereEqualTo("borrowProcess", true)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if(task1.isSuccessful()){
                                            for (QueryDocumentSnapshot document:task1.getResult()){
                                                Map<String, String> returnMap = new HashMap<>();

                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();
                                                String borrower = document.getData().get("acceptedRequestor").toString();

                                                a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                returnMap.put("borrower", borrower);
                                                callback.onCallback(returnMap);
                                            }
                                        }else{}

                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * fetch borrowed books the borrower has confirmed to hand over but not received by the owner
     * @param callback
     */
    public void fetch_borrower_confirmed_book(final dbCallback callback) {
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name= document.get("username").toString();
                        db.collection("Book")
                                .whereEqualTo("acceptedRequestor",name)
                                .whereEqualTo("state.bookStatus", "BORROWED")
                                .whereEqualTo("returnProcess", true)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if(task1.isSuccessful()){
                                            for (QueryDocumentSnapshot document:task1.getResult()){
                                                Map<String, String> returnMap = new HashMap<>();

                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();

                                                a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                callback.onCallback(returnMap);
                                            }
                                        }else{}

                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * fetch the books requested by the current user
     * @param callback
     */
    public void fetch_RequestBook(final dbCallback callback){
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name= document.get("username").toString();
                        db.collection("Book")
                                .whereArrayContains("requestors",name)
                                .whereEqualTo("acceptedRequestor","")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if(task1.isSuccessful()){
                                            for (QueryDocumentSnapshot document:task1.getResult()){
                                                Map<String, String> returnMap = new HashMap<>();

                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();
                                                a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                callback.onCallback(returnMap);
                                            }
                                        }else{}

                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * fetch the books whose requester is the current user
     * @param callback
     */
    public void fetch_AcceptedBook(final dbCallback callback){
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name= document.get("username").toString();
                        db.collection("Book")
                                .whereEqualTo("acceptedRequestor",name)
                                .whereEqualTo("state.bookStatus","ACCEPTED")
                                .whereArrayContains("requestors",name)
                                //.whereEqualTo("state.bookStatus",which)//
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if(task1.isSuccessful()){
                                            for (QueryDocumentSnapshot document:task1.getResult()){
                                                Map<String, String> returnMap = new HashMap<>();

                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();
                                                //a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                callback.onCallback(returnMap);
                                            }
                                        }else{}

                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * fetch the book borrowed by the current user
     * @param callback
     */
    public void fetch_BorrowedBook(final dbCallback callback) {
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name = document.get("username").toString();
                        db.collection("Book")
                                .whereEqualTo("acceptedRequestor", name)
                                .whereEqualTo("state.bookStatus", "BORROWED")
                                .whereEqualTo("returnProcess", false)
                                .whereArrayContains("requestors", name)
                                //.whereEqualTo("state.bookStatus",which)//
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                                Map<String, String> returnMap = new HashMap<>();

                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();
                                                //a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                callback.onCallback(returnMap);
                                            }
                                        } else {
                                        }

                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * fetch the books which are available to the current user
     * @param callback
     * @param pb, progressbar
     */
    public void fetch_AvailableBook(final dbCallback callback, ProgressBar pb) {
        if (pb != null) { pb.setVisibility(View.VISIBLE); }
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name = document.get("username").toString();
                        db.collection("Book")
                                //.whereEqualTo("acceptedRequestor", name)
                                .whereEqualTo("state.bookStatus", "AVAILABLE")
                                //.whereArrayContains("requestors", name)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                                Map<String, String> returnMap = new HashMap<>();

                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();
                                                //a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                if (name.equals(ownerName)) {
                                                    continue;
                                                }
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                callback.onCallback(returnMap);
                                            }
                                        }
                                        if (pb != null) { pb.setVisibility(View.GONE); }
                                    }
                                });

                    } else {
                        if (pb != null) { pb.setVisibility(View.GONE); }
                        Log.d(TAG, "No such document");
                    }
                } else {
                    if (pb != null) { pb.setVisibility(View.GONE); }
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * fetch the books requested by others but available to the current usr
     * @param callback
     * @param pb
     */
    public void fetch_RequestBorrowedBook(final dbCallback callback, ProgressBar pb) {
        if (pb != null) { pb.setVisibility(View.VISIBLE); }
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name = document.get("username").toString();
                        db.collection("Book")
                                //.whereEqualTo("acceptedRequestor", name)
                                .whereEqualTo("state.bookStatus", "REQUESTED")
                                //.whereArrayContains("requestors", name)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                                Map<String, String> returnMap = new HashMap<>();

                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();
                                                ArrayList<String> requestors = (ArrayList<String>) document.getData().get("requestors");
                                                if (requestors.contains(name) || name.equals(ownerName)) {
                                                    continue;
                                                }
                                                //a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                callback.onCallback(returnMap);
                                                if (pb != null) { pb.setVisibility(View.GONE); }
                                            }
                                        } else {
                                            if (pb != null) { pb.setVisibility(View.GONE); }
                                        }

                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                        if (pb != null) { pb.setVisibility(View.GONE); }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    if (pb != null) { pb.setVisibility(View.GONE); }
                }
            }
        });
    }
    public void fetch_RequestRequestedBook(final dbCallback callback) {
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name = document.get("username").toString();
                        db.collection("Book")
                                //.whereEqualTo("acceptedRequestor", name)
                                //.whereEqualTo("state.bookStatus", "REQUESTED")
                                .whereArrayContains("requestors", name)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                                Map<String, String> returnMap = new HashMap<>();

                                                //do something here
                                                String title = document.getData().get("title").toString();
                                                String author = document.getData().get("author").toString();
                                                String ISBN = document.getData().get("ISBN").toString();
                                                String description = document.getData().get("description").toString();
                                                String ownerName = document.getData().get("ownerName").toString();
                                                //a.add(new Book(title,author,ISBN,description,ownerName,null));
                                                returnMap.put("title", title);
                                                returnMap.put("author", author);
                                                returnMap.put("ISBN", ISBN);
                                                returnMap.put("description", description);
                                                returnMap.put("ownerName", ownerName);
                                                callback.onCallback(returnMap);
                                            }
                                        } else {
                                        }

                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * fetch the request handler of one book by title
     * @param title
     * @param callback
     */
    public void fetch_MyBookRequest(String title ,final dbCallback callback) {
        fAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Book")
                .whereEqualTo("title", title)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                if (task1.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task1.getResult()) {
                        //YBS's editing
                        Object object = document.getData().get("requestors");
                        ArrayList<String> requestors = (ArrayList<String>) object;
                        String acceptedRequestor = document.getData().get("acceptedRequestor").toString();
                        String state = document.getData().get("state").toString();
                        BookState bookstate = new BookState(state, null, null);
                        RequestHandler rh = new RequestHandler(bookstate, requestors, acceptedRequestor,
                               false, false);

                        Map<String, RequestHandler> returnRequestMap = new HashMap<>();
                        returnRequestMap.put("requestHandler", rh);
                        callback.onCallback(returnRequestMap);
                    }
                } else {
                }

            }
        });
    }

    /**
     * fetch the book's current state
     * @param ISBN, indicating which book to fetch
     * @param callback
     */
    public void fetch_MyBookState(String ISBN ,final dbCallback callback) {
        //FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Book")
                .whereEqualTo("ISBN", ISBN)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                if (task1.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task1.getResult()) {
                        //YBS's editing
                        Map state = (Map) document.getData().get("state");
                        String temp = state.get("bookStatus").toString();
                        Map<String, String> returnStateMap = new HashMap<>();
                        returnStateMap.put("state", temp);
                        callback.onCallback(returnStateMap);
                    }
                } else {
                }

            }
        });
    }

    /**
     * fetch the location of one book
     * @param ISBN, indicating which book to fetch
     * @param callback
     */
    public void fetch_location(String ISBN ,final dbCallback callback){
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //ArrayList<Book> a= new ArrayList<>();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name= document.get("username").toString();
                        db.collection("Book")
                                .whereEqualTo("ISBN",ISBN)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {

                                        if(task1.isSuccessful()){
                                            for (QueryDocumentSnapshot document:task1.getResult()){
                                                Map<String, GeoPoint> returnMap = new HashMap<>();
                                                Map m = (Map)document.getData().get("state");
                                                GeoPoint g = (GeoPoint) m.get("location");
                                                returnMap.put("location", g);
                                                callback.onCallback(returnMap);
                                            }
                                        }else{}

                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }


    /**
     * responsible for updating book status when borrower request a certain book
     * @param ISBN isbn of the book which I want to update its info
     */
    public void borrowerRequestBook(String ISBN,String title,String ownerName,Notification n){
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(user.getUid());

        // get user name of current user
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.get("username").toString();
                        // update requestors
                        db.collection("Book")
                                .document(ISBN)
                                .update("requestors", FieldValue.arrayUnion(name));
                        // update book status
                        db.collection("Book")
                                .document(ISBN)
                                .update("state.bookStatus", "REQUESTED");

                        db.collection("Notifications").document(ISBN).set(n);

                        db.collection("Notifications")
                                .document(ISBN)
                                .update("acceptedUser",ownerName);
                        db.collection("Notifications")
                                .document(ISBN)
                                .update("isbn",ISBN);
                        db.collection("Notifications")
                                .document(ISBN)
                                .update("notificationType","REQUEST_SENT");
                        db.collection("Notifications")
                                .document(ISBN)
                                .update("ownerName",name);
                        db.collection("Notifications")
                                .document(ISBN)
                                .update("requesters", FieldValue.arrayUnion(name));
                        db.collection("Notifications")
                                .document(ISBN)
                                .update("title", title);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void fetch_Ownernotify(final dbCallback callback){
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db.collection("User").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                db.collection("Notifications")
                                        .whereEqualTo("ownerName", document.getData().get("username"))
                                        .whereEqualTo("notificationType","REQUEST_SENT")
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, String> returnMap = new HashMap<>();
                                                String isbn=document.getData().get("isbn").toString();
                                                String owner=document.getData().get("ownerName").toString();
                                                String title=document.getData().get("title").toString();
                                                returnMap.put("isbn", isbn);
                                                returnMap.put("owner", owner);
                                                returnMap.put("title",title);
                                                callback.onCallback(returnMap);
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


    }

    /**
     * responsible for updating borrow process status when borrower/owner scan the ISBN
     * @param person, indicating who is scanning the book
     * @param ISBN, indicating the book to change state
     * @param callback
     */
    public void updateBorrowProcess(String person, String ISBN, dbCallback callback) {
        db = FirebaseFirestore.getInstance();
        if (person.equals("owner")) {
            db.collection("Book")
                    .document(ISBN)
                    .update("borrowProcess", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Map<String, String> returnMap = new HashMap<>();
                            Toast.makeText(context, "Book hand over confirmed", Toast.LENGTH_SHORT).show();
                            callback.onCallback(returnMap);
                        }
                    });
        } else {
            db.collection("Book")
                    .document(ISBN)
                    .update("borrowProcess", false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
            db.collection("Book")
                    .document(ISBN)
                    .update("state.bookStatus", "BORROWED")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Map<String, String> returnMap = new HashMap<>();
                            Toast.makeText(context, "Book received confirmed", Toast.LENGTH_SHORT).show();
                            callback.onCallback(returnMap);
                        }
                    });
        }
    }

    /**
     * responsible for updating return process status when borrower/owner scan the ISBN
     * @param person, indicating who is scanning the book
     * @param ISBN, indicating the book to change state
     * @param callback
     */
    public void updateReturnProcess(String person, String ISBN, dbCallback callback) {
        db = FirebaseFirestore.getInstance();
        if (person.equals("borrower")) {
            db.collection("Book")
                    .document(ISBN)
                    .update("returnProcess", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Map<String, String> returnMap = new HashMap<>();
                            Toast.makeText(context, "Book return confirmed", Toast.LENGTH_SHORT).show();
                            callback.onCallback(returnMap);
                        }
                    });
        } else {
            BookState state = new BookState();

            db.collection("Book")
                    .document(ISBN)
                    .update(
                            "acceptedRequestor", "",
                            "requestors", new ArrayList<String>(),
                            "state", state,
                            "returnProcess", false
                            )
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Map<String, String> returnMap = new HashMap<>();
                                Toast.makeText(context, "Book received confirmed", Toast.LENGTH_SHORT).show();
                                callback.onCallback(returnMap);
                            }
                        }
                    });
        }
    }


    /**
     * responsible for checking if one book is in borrow handing over process
     * @param ISBN, indicating the book to change state
     * @param callback
     */
    public void checkHandProcess(String ISBN, dbCallback callback) {
        db = FirebaseFirestore.getInstance();
        db.collection("Book")
                .document(ISBN)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Boolean borrowProcess = document.getBoolean("borrowProcess");
                                Map<String, Boolean> returnMap = new HashMap<>();
                                returnMap.put("borrowProcess", borrowProcess);
                                callback.onCallback(returnMap);
                            }
                        }
                    }
                });
    }

    /**
     * responsible for checking if one book is in return handing over process
     * @param ISBN, indicating the book to change state
     * @param callback
     */
    public void checkReturnProcess(String ISBN, dbCallback callback) {
        db = FirebaseFirestore.getInstance();
        db.collection("Book")
                .document(ISBN)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Boolean returnProcess = document.getBoolean("returnProcess");
                                Map<String, Boolean> returnMap = new HashMap<>();
                                returnMap.put("returnProcess", returnProcess);
                                callback.onCallback(returnMap);
                            }
                        }
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
