package com.example.bookit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class BorrowSearchFragment extends Fragment {
    private ImageView backButton;
    private ImageView searchButton;
    private EditText searchText;
    private RecyclerView rv;
    private BookAdapter bAdapter;
    private FireStoreHelper fs;
    ArrayList<Book> testList;
    private ProgressBar pb;

    @Override
    /**
     * fragment used for borrower searching books they want to borrow
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_borrow_search
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_borrow_search, container, false);
        fs=new FireStoreHelper(getActivity());
        pb = view.findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);
        // get views by their ids
        backButton = view.findViewById(R.id.button_back);
        searchButton = view.findViewById(R.id.button_search);
        searchText = view.findViewById(R.id.text_search);
        rv = view.findViewById(R.id.rv_search);

        //set up recycler view for book data
        testList = new ArrayList<Book>();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);
        bAdapter = new BookAdapter(getActivity(), testList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                // switch to book request fragment
                // get current clicked book info
                Book bookGet = bAdapter.getBookObject(pos);
                String isbn = bookGet.getISBN();
                String des = bookGet.getDescription();
                String title = bookGet.getTitle();
                String owner = bookGet.getOwnerName();
                String author = bookGet.getAuthor();
                RequestHandler rh = new RequestHandler();

                // put info into bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("rh",rh);
                bundle.putString("isbn",isbn);
                bundle.putString("description",des);
                bundle.putString("title",title);
                bundle.putString("author",author);
                bundle.putString("owner",owner);
                Navigation.findNavController(view).navigate(R.id.action_borrow_search_to_book_request, bundle);
            }
        });
        rv.setAdapter(bAdapter);


        // set listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        // set listener for searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // search matched books
                String searInfo = searchText.getText().toString();
                // searching method need to be implemented here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if (searInfo.isEmpty()) {
                    Toast.makeText(getActivity(),"Cannot search empty string", Toast.LENGTH_SHORT).show();
                } else {
                    testList.clear();
                    bAdapter.notifyDataSetChanged();
                    searchAvailable(searInfo);
                }
//                testList.add(new Book(searInfo, "zhengyao", "123", "haha", "bingshen", null));
//                bAdapter.notifyDataSetChanged();
                searchText.setText("");
            }
        });

        return view;
    }

    private void searchAvailable(String keyword) {
        fs.fetch_AvailableBook( new dbCallback(){
            @Override
            public void onCallback(Map map) {
                String title=map.get("title").toString();
                String ISBN=map.get("ISBN").toString();
                String author=map.get("author").toString();
                String description=map.get("description").toString();
                String ownerName=map.get("ownerName").toString();

                if (title.toLowerCase().contains(keyword.toLowerCase())
                        || author.toLowerCase().contains(keyword.toLowerCase())
                        || description.toLowerCase().contains(keyword.toLowerCase())
                        || ISBN.toLowerCase().contains(keyword.toLowerCase())) {
                    Book b= new Book(title,author,ISBN,description,ownerName,null);
                    testList.add(b);
                    bAdapter.notifyDataSetChanged();
                }
            }
        }, pb);

        fs.fetch_RequestBorrowedBook(new dbCallback() {
            @Override
            public void onCallback(Map map) {
                String title=map.get("title").toString();
                String ISBN=map.get("ISBN").toString();
                String author=map.get("author").toString();
                String description=map.get("description").toString();
                String ownerName=map.get("ownerName").toString();

                if (title.toLowerCase().contains(keyword.toLowerCase())
                        || author.toLowerCase().contains(keyword.toLowerCase())
                        || description.toLowerCase().contains(keyword.toLowerCase())
                        || ISBN.toLowerCase().contains(keyword.toLowerCase())) {
                    Book b= new Book(title,author,ISBN,description,ownerName,null);
                    testList.add(b);
                    bAdapter.notifyDataSetChanged();
                }
            }
        }, pb);
    }
}
