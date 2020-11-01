package com.example.bookit;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Map;

import java.util.HashMap;

import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static java.lang.Integer.parseInt;

public class ProfileFragment extends Fragment {
    boolean alertReady;
    FireStoreHelper fs;
    public static final int PICK_IMAGE = 1;
    protected ImageView image;
    private Uri MediaUri;
    private AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);
        final User testUser = new User("xiu", "xiu", "testID",
                "testEmail", "911", "123456abc");

        AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
        builder.setCancelable(true);
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));

        dialog = builder.create();

        fs=new FireStoreHelper(getActivity());

        final TextView userNameView = v.findViewById(R.id.userName);
        final TextView contactInfoView = v.findViewById(R.id.contactInfo);

        final SharedPreferences pref = container.getContext().
                getSharedPreferences("Profile", Context.MODE_PRIVATE);

        image=v.findViewById(R.id.imageView5);

        if (!pref.contains("username") ||
                !pref.contains("username")) {
            fs=new FireStoreHelper(getActivity());
            fs.Fetch(new dbCallback() {
                @Override
                public void onCallback(Map map) {
                    String s= (String) map.get("username");
                    userNameView.setText(s);
                    String n= (String) map.get("contactInfo");
                    contactInfoView.setText(n);

                    SharedPreferences.Editor prefEditor = getContext().
                            getSharedPreferences("Profile", Context.MODE_PRIVATE).edit();
                    prefEditor.putString("username", s);
                    prefEditor.putString("contactInfo", n);
                    prefEditor.commit();
                    dialog.dismiss();

                }
            }, dialog);///
        } else {
            userNameView.setText(pref.getString("username", ""));
            contactInfoView.setText(pref.getString("contactInfo", ""));
        }
        try {
            fs.load_image(image);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //final TextView userNameView = v.findViewById(R.id.userName);
        //final TextView contactInfoView = v.findViewById(R.id.contactInfo);
        //userNameView.setText(testUser.getUserName());
       // contactInfoView.setText(testUser.getContactInfo());

        Button signOut=v.findViewById(R.id.logoutButton);
        ImageButton edit=v.findViewById(R.id.editButton);///
        ImageButton editimage=v.findViewById(R.id.editimage);//

        //change profile image


        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });


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
                username.setText(userNameView.getText());
                contactInfo.setText(contactInfoView.getText());
                username.setText(pref.getString("username", ""));
                contactInfo.setText(pref.getString("contactInfo", ""));
                builder.setPositiveButton("update", null);
                builder.setNegativeButton("cancel", null);

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

                                    //testUser.setContactInfo(contactInfo.getText().toString().trim());
                                   // testUser.setUserName(username.getText().toString().trim());
                                    //////////////////update need here
                                    fs.update(username.getText().toString(),contactInfo.getText()
                                            .toString());
                                    SharedPreferences.Editor prefEditor = getContext().
                                            getSharedPreferences("Profile", Context.MODE_PRIVATE)
                                            .edit();
                                    prefEditor.clear().commit();
                                    Toast.makeText(getContext(), "update successfully!", Toast
                                            .LENGTH_SHORT).show();

                                    Toast.makeText(getContext(), "update successfully!", Toast.LENGTH_SHORT).show();
                                    alertReady=true;
                                    userNameView.setText(username.getText());
                                    contactInfoView.setText(contactInfo.getText());


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
        return v;
    }
    //this method is for choosing a new image for profile
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data==null){
                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_LONG).show();
            }
            else {
                MediaUri = data.getData();
                fs.image_update(MediaUri);
                image.setImageURI(MediaUri);
                Toast.makeText(getActivity(), "update profile image successfully", Toast.LENGTH_LONG).show();
            }
        }

    }


}