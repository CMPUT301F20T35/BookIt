package com.example.bookit;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    FireStoreHelper fs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);
        User testUser = new User("testName", "testUser", "testID",
                "testEmail", 123456, "123456abc");
        fs=new FireStoreHelper(getActivity());
        TextView userNameView = v.findViewById(R.id.userName);
        TextView contactInfoView = v.findViewById(R.id.contactInfo);
        Button signOut=v.findViewById(R.id.logoutButton);

        userNameView.setText(testUser.getUserName());
        contactInfoView.setText(Integer.toString(testUser.getContactInfo()));
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