package com.example.bookit;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signUpValidator {
    private String password;
    private String username;
    private String email;
    private String number;

    FirebaseFirestore db;

    public signUpValidator(String password, String username, String email, String number) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.number = number;
    }

    public boolean emptyCheck(Object text) {
        if (text.toString().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean numberCheck() {
        Pattern pattern = Pattern.compile("^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");
        Matcher matcher = pattern.matcher(this.number);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public boolean passwordCheck() {
        if (this.password.length() < 6) {
            return false;
        }
        return true;
    }

    public void uniqueUsername(final Map newUser, final ProgressBar progressBar,
                               final FireStoreHelper fs, final Context context) {
        progressBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        final Query usernameQuery = db.collection("User")
                .whereEqualTo("username", this.username);
        usernameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() < 1) {
                        fs.signUp(newUser, progressBar);
                    }else {
                        Toast.makeText(context,  "Error! This username is already in use" +
                                        "by another account", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, "Error ! " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
