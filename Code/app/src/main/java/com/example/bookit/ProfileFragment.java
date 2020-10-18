package com.example.bookit;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static java.lang.Integer.parseInt;

public class ProfileFragment extends Fragment {
    boolean alertReady;
    FireStoreHelper fs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);
        final User testUser = new User("testName", "testUser", "testID",
                "testEmail", 123456, "123456abc");
        fs=new FireStoreHelper(getActivity());
        final TextView userNameView = v.findViewById(R.id.userName);
        final TextView contactInfoView = v.findViewById(R.id.contactInfo);
        userNameView.setText(testUser.getUserName());
        contactInfoView.setText(Integer.toString(testUser.getContactInfo()));
        Button signOut=v.findViewById(R.id.logoutButton);
        ImageButton edit=v.findViewById(R.id.editButton);
        //update the profile using alertDialog
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Update your profile");
                View v = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_alertdialog, null, false);
                builder.setView(v);
                final EditText username=v.findViewById(R.id.usernameedit);
                final EditText contactInfo=v.findViewById(R.id.contactinfoedit);
                username.setText(testUser.getUserName());
                contactInfo.setText(Integer.toString(testUser.getContactInfo()));
                builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                final AlertDialog alertDialog = builder.create();
                alertReady = false;

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        if (!alertReady){
                        Button buttonPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        buttonPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (username.getText().toString().trim().equals("")||contactInfo.getText().toString().trim().equals("")){
                                    if (username.getText().toString().trim().equals("")){
                                        username.setError("username is required");
                                    }
                                    if (contactInfo.getText().toString().trim().equals("")){
                                        contactInfo.setError("contact info is required");
                                    }
                                }
                                else{
                                    //need to update the firestore too
                                    testUser.setContactInfo(parseInt(contactInfo.getText().toString().trim()));
                                    testUser.setUserName(username.getText().toString().trim());
                                    userNameView.setText(testUser.getUserName());
                                    contactInfoView.setText(Integer.toString(testUser.getContactInfo()));
                                    Toast.makeText(getContext(), "update successfully!", Toast.LENGTH_SHORT).show();
                                    alertReady=true;

                                }
                                if(alertReady){
                                    dialog.dismiss();}
                            }
                        });
                        Button buttonNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        buttonNegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        }
                    }
                });

                alertDialog.show();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fs.logout();
            }
            });
        // Inflate the layout for this fragment

        return v;
    }


}