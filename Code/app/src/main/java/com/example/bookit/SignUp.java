package com.example.bookit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private FireStoreHelper fs;
    private ProgressBar progressBar;


    @Override
    /**
     * activity used for user signing up
     * @see activity corresponding to layout file activity_sign_up
     */
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

        signUpNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Button signUp = findViewById(R.id.newUserSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();
                String username = signUpUsername.getText().toString().trim();
                String number = signUpNumber.getText().toString().trim();

                signUpValidator validator = new signUpValidator(password, username, email, number);

                Context context = getApplicationContext();

                Map<String, Object> newUser = new LinkedHashMap<>();
                newUser.put("email", email);
                newUser.put("password", password);
                newUser.put("username", username);
                newUser.put("number", number);

                for (Map.Entry<String, Object> entry : newUser.entrySet()) {
                    if(!validator.emptyCheck(entry.getValue())) {
                        Toast.makeText(context, entry.getKey() +
                                " cannot be empty",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (!validator.numberCheck()) {
                    Toast.makeText(context, "wrong phone format, must be (xxx) xxx-xxx",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (!validator.passwordCheck()) {
                    Toast.makeText(context, "password too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                newUser.put("name", "");

                validator.uniqueUsername(newUser, progressBar, fs, context);

            }
        });
    }
}