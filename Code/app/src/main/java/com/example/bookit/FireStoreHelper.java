package com.example.bookit;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FireStoreHelper {
    FirebaseAuth fAuth;
    Context context;



    public FireStoreHelper() {
    }

    public FireStoreHelper( Context context) {
        this.context =context;
    }
    /**
     * used to authenticate the email and password
     * @param email,password
     * */
    public void loginAuth(String email,String password)
    {

        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Toast.makeText(context, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context,MainActivity.class));
            }else {
                Toast.makeText(context, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    });
    }
    /**
     * used to sign out of the user
     * */
    public void logout() {
        fAuth=FirebaseAuth.getInstance();
        fAuth.signOut();//logout
        context.startActivity(new Intent(context,Login.class));
    }


    public FirebaseAuth getfAuth() {
        return fAuth;
    }

    public void setfAuth(FirebaseAuth fAuth) {
        this.fAuth = fAuth;
    }

}
