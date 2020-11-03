/**
 * citation: https://www.youtube.com/watch?v=9HLSNHbZSUA
 */
package com.example.bookit;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class BookImageAdapter extends PagerAdapter {
    private Context context;
    private Uri uri;

    public BookImageAdapter(Context context, Uri uri) {

    /**
     * This constructor takes in two parameters
     * @param context
     * @param imgArrayList arrayList contains Uri object
     */

        this.context = context;
        this.uri=uri;
    }


    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, container, false);

        ImageView bookImgView = view.findViewById(R.id.bookImgView);



        bookImgView.setImageURI(uri);

        container.addView(view, position);

        //click to delete the image
        bookImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeViewAt(position);
                notifyDataSetChanged();

            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
