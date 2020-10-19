package com.example.bookit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class SignUp extends AppCompatActivity {
    FireStoreHelper fs;
    ProgressBar progressBar;


    private boolean textCheck(EditText text) {

        if (text.getText().toString().trim().isEmpty()) {
            text.setError("Invalid text");
            return false;
        }
        if (text == findViewById(R.id.passwordSignUp) &&
                text.getText().toString().trim().length()<6) {
            text.setError("too short");
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressBar = findViewById(R.id.progressBarSignUp);
        progressBar.setVisibility(View.GONE);
        fs=new FireStoreHelper(this);

        final EditText signUpEmail  = findViewById(R.id.emailTextSignUp);
        final EditText signUpPassword = findViewById(R.id.passwordSignUp);
        final EditText signUpUsername = findViewById(R.id.userNameSignUp);
        final EditText signUpNumber = findViewById(R.id.contactSignUp);

        Button signUp = findViewById(R.id.newUserSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();
                String username = signUpUsername.getText().toString().trim();
                String number = signUpNumber.getText().toString().trim();

                if (!textCheck(signUpEmail) || !textCheck(signUpPassword)
                        || !textCheck(signUpUsername) || !textCheck(signUpNumber)) {
                    return;
                }

                fs.signUp(email, password, username, number, progressBar);

            }
        });
    }
}