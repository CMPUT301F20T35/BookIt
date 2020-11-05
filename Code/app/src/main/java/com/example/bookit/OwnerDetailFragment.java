package com.example.bookit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.IOException;
import java.util.Map;

public class OwnerDetailFragment extends Fragment {
    Button back;
    private FireStoreHelper db;
    private String username;
    String id;
    private TextView usernameView;
    private TextView emailView;
    private TextView numberView;
    private ImageView image;
    @Override
    /**
     * fragment used for displaying owner detail
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_owner_detail
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_owner_detail, container, false);
        back = view.findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        Bundle b=getArguments();
        username=b.getString("username");

        usernameView=view.findViewById(R.id.owner_name);
        emailView=view.findViewById(R.id.textView11);
        numberView=view.findViewById(R.id.textView12);
        image=view.findViewById(R.id.imageView2);

        usernameView.setText(username);
        db=new FireStoreHelper(getContext());
        db.fetch_user_withUsername(username, new dbCallback() {
            @Override
            public void onCallback(Map map) {
                String e=map.get("email").toString();
                String number=map.get("number").toString();
                id=map.get("id").toString();
                emailView.setText(e);
                numberView.setText(number);
                try {
                    db.load_image_with_id(id,new dbCallback() {
                        @Override
                        public void onCallback(Map map) {
                            String imageEncoded = (String) map.get("userimg");
                            SharedPreferences.Editor prefEditor = getContext().
                                    getSharedPreferences("Profile", Context.MODE_PRIVATE).edit();
                            prefEditor.putString("profileimg", imageEncoded);
                            prefEditor.commit();

                            byte[] decodedByte = Base64.decode(imageEncoded, 0);
                            Bitmap img = BitmapFactory
                                    .decodeByteArray(decodedByte, 0, decodedByte.length);
                            image.setImageBitmap(img);
                        }
                    });
                } catch (IOException f) {
                    f.printStackTrace();
                }

            }
        });


        return view;
    }
}
