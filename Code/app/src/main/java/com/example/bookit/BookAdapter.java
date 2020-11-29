package com.example.bookit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.LinearViewHolder> {
    private Context mContext;
    private ArrayList<Book> bookData = new ArrayList<>();
    private OnItemClickListener mlistener;
    private FireStoreHelper fs;

    /**
     * This constructor takes in three parameters
     * @param context
     * @param bookData arrayList contains Book object
     * @param listener
     */
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
        holder.ownerName.setText("owner: " + bookData.get(position).getOwnerName());
        //holder.state.setText(bookData.get(position).getISBN());
        fs=new FireStoreHelper(mContext);
        fs.fetch_MyBookState(bookData.get(position).getISBN(), new dbCallback() {
            @Override
            public void onCallback(Map map) {
                String state = map.get("state").toString();
                holder.state.setText(state);
                switch (state) {
                    case "REQUESTED": holder.state.setTextColor(Color.parseColor("#F44336"));
                        break;
                    case "BORROWED": holder.state.setTextColor(Color.parseColor("#FF9800"));
                        break;
                    case "ACCEPTED": holder.state.setTextColor(Color.parseColor("#4CAF50"));
                        break;
                    case "AVAILABLE": holder.state.setTextColor(Color.parseColor("#3F51B5"));
                        break;
                    default: holder.state.setTextColor(Color.parseColor("#fff"));
                }

            }
        });

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

    public String getTitle(int position){
        return bookData.get(position).getTitle();
    }

    public ArrayList<Book> getBookData() {
        return bookData;
    }

    public Book getBookObject(int pos){
        return bookData.get(pos);
    }

    //inherit viewholder
    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView bookName;
        private TextView authorName;
        private TextView state;
        private TextView ownerName;

        public LinearViewHolder(View itemView){
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            authorName = itemView.findViewById(R.id.author);
            state = itemView.findViewById(R.id.status);
            ownerName = itemView.findViewById(R.id.row_render_owner);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }

//    public interface OnItemLongClickListener{
//        void onLongClick(int pos);
//    }


}


