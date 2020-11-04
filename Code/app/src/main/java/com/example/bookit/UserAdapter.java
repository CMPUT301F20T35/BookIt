package com.example.bookit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.LinearViewHolder> {
    private Context mContext;
    private ArrayList<User> userData = new ArrayList<>();

    /**
     * This constructor takes in two parameters
     * @param context context of environment
     * @param userData ArrayList contains user data
     */
    public UserAdapter(Context context, ArrayList<User> userData){
        this.mContext = context;
        this.userData = userData;
        //implement interface on the click
    }


    @NonNull
    @Override
    public UserAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.request_list_rendering,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.LinearViewHolder holder, final int position) {
        holder.email.setText(userData.get(position).getEmail());
    }


    @Override
    public int getItemCount() {
        return userData.size();
    }

    public void removeItem(int position) {
        userData.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<User> getUserData() {
        return userData;
    }

    //inherit viewholder
    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView email;

        public LinearViewHolder(View itemView){
            super(itemView);
            email = itemView.findViewById(R.id.user_email);
        }
    }

//    public interface OnItemClickListener{
//        void onClick(int pos);
//    }
}
