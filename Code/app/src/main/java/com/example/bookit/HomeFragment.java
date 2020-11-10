package com.example.bookit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookit.R;

import java.util.ArrayList;
import java.util.Map;


public class HomeFragment extends Fragment {
    private RecyclerView rv;
    private NotificationAdapter bAdapter;
    private ArrayList<Notification> dataList;
    private FireStoreHelper fs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rv = root.findViewById(R.id.rv_1);
        //initilize test array and adapter
        String noti="@xiu accepted your request on the book <<haha>>";
        dataList = new ArrayList<Notification>();
        fs=new FireStoreHelper(getContext());

        fs.fetchBorrowerNotification(new dbCallback() {
            @Override
            public void onCallback(Map map) {
                String isbn=map.get("isbn").toString();
                String title=map.get("title").toString();
                String owner=map.get("owner").toString();
                Notification n=new Notification(title,isbn,owner,new ArrayList<String>(),"","REQUEST_ACCEPTED");
                dataList.add(n);
                //set up manager and adapter to contain data
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                //setting the separate line
                DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
                rv.addItemDecoration(divider);
                bAdapter=new NotificationAdapter(getActivity(),dataList);
                rv.setAdapter(bAdapter);
                enableSwipeToDeleteAndUndo(fs);
            }
        });


        return root;
    }
    private void enableSwipeToDeleteAndUndo(FireStoreHelper fs){
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final String item = bAdapter.getData(position).getISBN();
                bAdapter.removeItem(position);
                fs.deleteNotification(item);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rv);
    }
}