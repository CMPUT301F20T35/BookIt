package com.example.bookit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.LinearViewHolder> {
    private Context mContext;
    private ArrayList<Book> bookData = new ArrayList<>();
    private OnItemClickListener mlistener;

    public BookAdapter(Context context, ArrayList<Book> bookData, OnItemClickListener listener){
        this.mContext = context;
        this.bookData = bookData;
        //implement interface on the click
        this.mlistener = listener;
    }


    @NonNull
    @Override
    public BookAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.book_row_rendering,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.LinearViewHolder holder, final int position) {
        holder.bookName.setText(bookData.get(position).getTitle());
        holder.authorName.setText(bookData.get(position).getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onClick(position);
                //Toast.makeText(mContext,"Testing"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookData.size();
    }

    public void removeItem(int position) {
        bookData.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<Book> getBookData() {
        return bookData;
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

    public interface OnItemClickListener{
        void onClick(int pos);
    }

//    public interface OnItemLongClickListener{
//        void onLongClick(int pos);
//    }


}


