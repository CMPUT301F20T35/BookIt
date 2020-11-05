package com.example.bookit;

import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class
FireStoreHelper {
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    Context context;
    boolean isSuccessful=false;
    private StorageReference mstore;
    private String username;
    int i;
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
     * @param book
     * */
    public void addBook(final Book book){
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
                    book.setOwnerName((String) d.get("username"));
                } else {
                    Toast.makeText(context.getApplicationContext(),task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    } else {
                        Toast.makeText(context.getApplicationContext(),"this book already existed, change a book",Toast
                                .LENGTH_SHORT).show();
                    }
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
    public void image_update(Uri u){
        //ImageView image=v.findViewById(R.id.imageView5);
        fAuth = FirebaseAuth.getInstance();
        mstore= FirebaseStorage.getInstance().getReference();
        FirebaseUser user = fAuth.getCurrentUser();
        String name="current";
        StorageReference storageReference=mstore.child("images/"+user.getUid()+"/"+name+".jpg");
        storageReference.putFile(u);
    }

    public void book_image_update(Uri u,String isbn){
        fAuth = FirebaseAuth.getInstance();
        mstore= FirebaseStorage.getInstance().getReference();
        StorageReference storageReference=mstore.child("book_images/"+isbn+"/"+"image1.jpg");
        storageReference.putFile(u);
    }

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
    public void book_image_add(Uri u,Book book) {
        if (u!= null){
            mstore= FirebaseStorage.getInstance().getReference();
            StorageReference storageReference;
            storageReference=mstore.child("book_images/"+book.getISBN()+"/image1"+".jpg");
            storageReference.putFile(u);
        }
    }




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


    //public void update(){}

    public FirebaseAuth getfAuth(){
        return fAuth;
    }

    public void setfAuth(FirebaseAuth fAuth) {
        this.fAuth = fAuth;
    }


}
