package com.example.bookit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity {

    private FireStoreHelper fs;
    private ProgressBar progressBar;//show the progress during the login
    @Override
    /**
     * activity used for user login
     * @see activity corresponding to layout file login_activity
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        final EditText Email=findViewById(R.id. emailText);
        final EditText Password=findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fs=new FireStoreHelper(this);
        Button login=findViewById(R.id.Login);
        progressBar.setVisibility(View.GONE);
        SharedPreferences.Editor prefEditor =
                getSharedPreferences("Profile", Context.MODE_PRIVATE).edit();
        prefEditor.clear().commit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                if(email.isEmpty()){
                    Email.setError("Email is Required.");
                    return;
                }

                if(password.isEmpty()){
                    Password.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    Password.setError("Password at least 6 Characters");
                    return;
                }
                fs.loginAuth(email, password,progressBar);
            }
        });
        Button signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignUp.class);
                startActivity(intent);
            }
        });


    }

}
