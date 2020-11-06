package com.example.bookit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class fragment_requestList extends Fragment {

    private RecyclerView rv;
    private UserAdapter uAdapter;
    private ImageView backButton;
    private TextView ownerDetail;
    FireStoreHelper fs;
    RequestHandler rh;

    private ImageButton editImage;
    private ImageButton delete;
    private ImageButton editInfo;
    private TextView titleView;
    private TextView ownerView;
    private TextView isbnView;
    private TextView descriptionView;
    private TextView authorView;

    protected ImageView imageView;

    private String isbn;
    private String title;
    private String description;
    private String owner;
    private String author;

    @Override
    /**
     * fragment used for owner accepting or denying requester
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_request_list
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        fs=new FireStoreHelper(getActivity());
        rv = view.findViewById(R.id.requestList);
        ownerDetail = (TextView)view.findViewById(R.id.owner_name);
        backButton = view.findViewById(R.id.bt_back2);

        //editImage=view.findViewById(R.id.editimage2);
        //delete=view.findViewById(R.id.deleteButton);
        titleView=view.findViewById(R.id.title);
        ownerView=view.findViewById(R.id.owner_name);
        isbnView=view.findViewById(R.id.IBSN);
        descriptionView=view.findViewById(R.id.Description);
        //authorView=view.findViewById(R.id.);
        //editInfo=view.findViewById(R.id.editbookinfo);

        Bundle b=getArguments();
        isbn=b.getString("isbn");
        title=b.getString("title");
        description=b.getString("description");
        owner=b.getString("owner");
        isbn=b.getString("isbn");
        rh = (RequestHandler) b.getSerializable("rh");
        //author=b.getString("author");
        //set text
        titleView.setText(title);
        ownerView.setText(owner);
        isbnView.setText(isbn);
        descriptionView.setText(description);
        //authorView.setText(author);

        ArrayList<String> requestorTemp = new ArrayList<String>();
        requestorTemp = rh.getRequestors();

        //initilize test array and adapter

        final ArrayList<User> testList = new ArrayList<User>();

        //set up manager and adapter to contain data
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);

        //back to last page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        //see owner detail
        ownerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_requestList_to_owner_detail);
            }
        });


//        setting the separate line
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL);
        rv.addItemDecoration(divider);


        int i;
        for (i=0;i<requestorTemp.size();i++){
            String requestName = requestorTemp.get(i);
            fs.fetchUser(requestName, new dbCallback() {
                @Override
                public void onCallback(Map map) {
                    String id = map.get("id").toString();
                    String email = map.get("email").toString();
                    String number = map.get("number").toString();
                    String name = map.get("name").toString();
                    String username = map.get("username").toString();

                    testList.add(new User(name,username,id,email,number,"123"));
                    uAdapter.notifyDataSetChanged();
                }
            });
            //testList.add(new User("ybs","YBS","ysb","email","number","123"));
        }


        //adapter operation
        ArrayList<String> finalRequestorTemp = requestorTemp;
        uAdapter = new UserAdapter(getActivity(), testList, new UserAdapter.MyClickListener() {
            @Override
            //accept button functionality
            public void onAccept(int p) {
                Toast.makeText(getActivity(),"Testing"+p, Toast.LENGTH_SHORT).show();
                User accpetRequestor = testList.get(p);
                String acceptName = testList.get(p).getUserName();
                fs.updateRequestor(acceptName,isbn);
                ArrayList<String> list = new ArrayList<String>();
                list.add(acceptName);
                fs.updateRequestors(isbn,list);
                testList.clear();
                testList.add(accpetRequestor);
                uAdapter.notifyDataSetChanged();

            }

            @Override
            //deny button functionality
            public void onDeny(int p) {
                String deleteName = testList.get(p).getUserName();
                finalRequestorTemp.remove(deleteName);
                testList.remove(p);
                fs.updateRequestors(isbn, finalRequestorTemp);
                uAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"Succesfully denied the request"+p, Toast.LENGTH_LONG).show();
            }
        });
        rv.setAdapter(uAdapter);

//        testList.add(new User("YBS","YBS","123","123@13","123","123"));
//        testList.add(new User("YBS","YBS","123","123@13","123","123"));
//        testList.add(new User("YBS","YBS","123","123@13","123","123"));
//        testList.add(new User("YBS","YBS","123","123@13","123","123"));
//        uAdapter.notifyDataSetChanged();


        return view;

    }




}