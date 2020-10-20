package com.example.bookit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.LinearViewHolder> {
    private Context mContext;
    private ArrayList<Book> bookData = new ArrayList<>();


    public BookAdapter(Context context, ArrayList<Book> bookData){
        this.mContext = context;
        this.bookData = bookData;
    }


    @NonNull
    @Override
    public BookAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.LinearViewHolder holder, int position) {
        holder.textView.setText(bookData.get(position).getTitle());
    }


    @Override
    public int getItemCount() {
        return bookData.size();
    }

    //inherit viewholder
    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public LinearViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.book_name);
        }
    }
}
