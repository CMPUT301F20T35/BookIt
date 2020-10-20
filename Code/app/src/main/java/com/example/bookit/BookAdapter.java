package com.example.bookit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


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
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.owner_book,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.LinearViewHolder holder, int position) {
        holder.bookName.setText(bookData.get(position).getTitle());
        holder.authorName.setText(bookData.get(position).getAuthor());
    }


    @Override
    public int getItemCount() {
        return bookData.size();
    }

    //inherit viewholder
    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView bookName;
        private TextView authorName;

        public LinearViewHolder(View itemView){
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            authorName = itemView.findViewById(R.id.author);

        }
    }
}
