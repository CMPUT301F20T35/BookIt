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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.LinearViewHolder> {
    private Context mContext;
    private ArrayList<Notification> data = new ArrayList<>();
    FireStoreHelper fs;

    /**
     * This constructor takes in three parameters
     * @param context
     * @param  data contains notification text
     */
    public NotificationAdapter(Context context, ArrayList<Notification> data){
        this.mContext = context;
        this.data = data;
        //implement interface on the click

    }


    @NonNull
    @Override
    public NotificationAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_row_rendering,parent,false));

    }
    public Notification getData(int pos){
        return data.get(pos);
    }


    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {
        String type=data.get(position).getNotificationType();
        if (type.equals("REQUEST_ACCEPTED")){
            holder.notification.setText("@"+data.get(position).getOwnerName()+" accepted your request on the book <<"+data.get(position).getTitle()+">>");
            holder.notification.setTextColor(Color.parseColor("#43A047"));

        }
        else{
            //owner notification for 高玉振
        }

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }




    //inherit viewholder
    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView notification;

        public LinearViewHolder(View itemView){
            super(itemView);
            notification=itemView.findViewById(R.id.noti);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }




}


