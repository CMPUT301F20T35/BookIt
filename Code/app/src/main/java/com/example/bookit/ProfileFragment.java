package com.example.bookit;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        User testUser = new User("testName", "testUser", "testID",
                "testEmail", 123456, "123456abc");

        TextView userNameView = v.findViewById(R.id.userName);
        TextView contactInfoView = v.findViewById(R.id.contactInfo);

        userNameView.setText(testUser.getUserName());
        contactInfoView.setText(Integer.toString(testUser.getContactInfo()));
        // Inflate the layout for this fragment
        return v;
    }
}