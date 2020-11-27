package com.example.bookit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.LinearViewHolder> {
    private Context mContext;
    private ArrayList<User> userData = new ArrayList<>();
    public MyClickListener myClickListener;

    /**
     * This constructor takes in two parameters
     * @param context context of environment
     * @param userData ArrayList contains user data
     */
    public UserAdapter(Context context, ArrayList<User> userData, MyClickListener listener){
        this.mContext = context;
        this.userData = userData;
        this.myClickListener = listener;
        //implement interface on the click
    }


    @NonNull
    @Override
    public UserAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LinearViewHolder holder = new UserAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.request_list_rendering,parent,false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.request_list_rendering,parent,false);

        LinearViewHolder holder = new UserAdapter.LinearViewHolder(view, new MyClickListener() {
            @Override
            public void onAccept(int p) {
            }

            @Override
            public void onDeny(int p) {
            }

            @Override
            public void onClickPhoto(int p) {

            }

        });
        return holder;
    }

    //here we set the information and click functionality for each row of list
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.LinearViewHolder holder, final int position) {
        holder.email.setText(userData.get(position).getEmail());
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onAccept(position);
            }
        });
        holder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onDeny(position);
            }
        });

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onClickPhoto(position);
            }
        });
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
    public class LinearViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        MyClickListener listener;
        private TextView email;
        public Button acceptButton;
        public Button denyButton;
        public ImageView photo;
        public LinearViewHolder(View itemView, MyClickListener listener){
            super(itemView);
            email = itemView.findViewById(R.id.user_email);
            acceptButton = itemView.findViewById(R.id.accpet);
            denyButton = itemView.findViewById(R.id.deny);
            photo = itemView.findViewById(R.id.image);
            this.listener = listener;

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onAccept(getAdapterPosition());
                }
            });


            denyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onDeny(getAdapterPosition());
                }
            });

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onClickPhoto(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.accpet:
                    listener.onAccept(this.getLayoutPosition());
                    break;
                case R.id.deny:
                    listener.onDeny(this.getLayoutPosition());
                    break;
                default:
                    break;

            }
        }
    }

    public interface MyClickListener{
        void onAccept(int p);
        void onDeny(int p);
        void onClickPhoto(int p);
    }
}

