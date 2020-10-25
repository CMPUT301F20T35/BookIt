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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    FireStoreHelper fs;
    ProgressBar progressBar;


    private boolean textCheck(EditText text) {
        Context context = getApplicationContext();
        if (text.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, text.getHint() + " cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (text == findViewById(R.id.passwordSignUp) &&
                text.getText().toString().trim().length()<6) {
            Toast.makeText(context, "password too short", Toast.LENGTH_SHORT).show();
            return false;
        } else if (text == findViewById(R.id.contactSignUp) ) {
            Pattern pattern = Pattern.compile("^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");
            Matcher matcher = pattern.matcher(text.getText().toString());
            if (!matcher.matches()) {
                Toast.makeText(context, "wrong phone format, must be (xxx) xxx-xxx", Toast.LENGTH_SHORT).show();
                return false;
            }
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

        signUpNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Button signUp = findViewById(R.id.newUserSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();
                String username = signUpUsername.getText().toString().trim();
                String number = signUpNumber.getText().toString().trim();
                System.out.println(number);
                if (!textCheck(signUpEmail) || !textCheck(signUpPassword)
                        || !textCheck(signUpUsername) || !textCheck(signUpNumber)) {
                    return;
                }

                fs.signUp(email, password, username, number, progressBar);

            }
        });
    }
}