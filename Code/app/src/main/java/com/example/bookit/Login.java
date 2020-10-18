package com.example.bookit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FireStoreHelper fs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        final EditText Email=findViewById(R.id. emailText);
        final EditText Password=findViewById(R.id.password);
        fs=new FireStoreHelper(this);
        Button login=findViewById(R.id.Login);
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
                fs.loginAuth(email, password);

            }
        });



    }

}
