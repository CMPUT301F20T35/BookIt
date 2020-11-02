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
    private ArrayList<Uri> imgArrayList;

    /**
     * This constructor takes in two parameters
     * @param context
     * @param imgArrayList arrayList contains Uri object
     */
    public BookImageAdapter(Context context, ArrayList<Uri> imgArrayList) {
        this.context = context;
        this.imgArrayList = imgArrayList;
    }


    @Override
    public int getCount() {
        return imgArrayList.size();
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

        final Uri img = imgArrayList.get(position);
        bookImgView.setImageURI(img);

        container.addView(view, position);

        //click to delete the image
        bookImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgArrayList.remove(position);
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
