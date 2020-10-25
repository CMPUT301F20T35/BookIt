package com.example.bookit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FireStoreHelper {
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    Context context;
    boolean isSuccessful=false;

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

    public void signUp(final String email, final String password, final String username,
                       final String number, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        fAuth = FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> newUser = new HashMap<>();

                        FirebaseUser user = fAuth.getCurrentUser();
                        newUser.put("id", user.getUid());
                        newUser.put("email", email);
                        newUser.put("password", password);
                        newUser.put("username", username);
                        newUser.put("number", number);
                        newUser.put("name", "");
                        db = FirebaseFirestore.getInstance();
                        final CollectionReference collectionReference = db.collection("User");
                        collectionReference.
                                document(user.getUid()).
                                set(newUser);
                        Toast.makeText(context, "Sign up Successfully", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                    }else {
                        System.out.println("f");
                        Toast.makeText(context, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });

    }
    public void Fetch(final dbCallback callback){ ;
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                System.out.println(task);
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> returnMap = new HashMap<>();
                        returnMap.put("aaa","hello");
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Log.d(TAG, "db firstName getString() is: " + document.getString("username"));
                        Log.d(TAG, "db lastName getString() is: " + document.getString("number"));

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

    //public void update(){}


    public void Fetch (){
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(user.getUid());
        //ArrayList<String> ItemList = new ArrayList<>();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> returnMap = new HashMap<>();
                        returnMap.put("aaa","hello");
                        //Map<String,String> returnuser = new HashMap<>();
                        //ItemList.add(document.getString("username"));
                    } else {
                        //Log.d(TAG, "No such document");//wait to add
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());//wait to add
                }
            }
        });

     //System.out.println(returnuser);
    }

    //public void update(){}

    public FirebaseAuth getfAuth(){
        return fAuth;
    }

    public void setfAuth(FirebaseAuth fAuth) {
        this.fAuth = fAuth;
    }

}
