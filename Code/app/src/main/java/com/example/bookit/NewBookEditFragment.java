package com.example.bookit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.okhttp.Protocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewBookEditFragment extends Fragment {

    private ViewPager bookPager;
    private ArrayList<Uri> imgArrayList;
    private BookImageAdapter bookImgAdapter;
    public static final int PICK_IMAGE = 1;
    private Uri MediaUri;

    private EditText newBookTitleET;
    private EditText newBookAuthorET;
    private EditText newBookISBNET;
    private EditText newBookDescriptionET;
    private FloatingActionButton addNewBook;
    private Button addImage;
    AlertDialog dialog;
    FireStoreHelper db;

    private ImageButton scanBtn;

    @Override
    /**
     * fragment used for
     * @return view of the fragment
     * @see fragment corresponding to layout file fragment_new_book_edit
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_book_edit, container, false);
        bookPager = view.findViewById(R.id.newBookPager);
        //loadCards();
        imgArrayList = new ArrayList<>();
        addImage=view.findViewById(R.id.add_book_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        newBookTitleET = view.findViewById(R.id.newBookTitle);
        newBookAuthorET = view.findViewById(R.id.newBookAuthor);
        newBookISBNET = view.findViewById(R.id.newBookISBN);
        newBookDescriptionET = view.findViewById(R.id.newBookDescription);
        addNewBook = view.findViewById(R.id.addNewBookBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
        builder.setCancelable(true);
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));

        dialog = builder.create();
        db=new FireStoreHelper(getContext());

        scanBtn = view.findViewById(R.id.ownerScanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });

        addNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // try to add new book, if success, go back mybook page
                // if fail, Toast message
                String title=newBookTitleET.getText().toString().trim();
                String author=newBookAuthorET.getText().toString().trim();
                String ISBN=newBookISBNET.getText().toString().trim();
                String desc=newBookDescriptionET.getText().toString().trim();
                String owner="";
                RequestHandler requestHandler=new RequestHandler();
                Book book= new Book(title,author,ISBN,desc,owner,requestHandler);
                if (bookInfoValidator(book)) {
                    db.addBook(book);
                    db.book_image_add(MediaUri,book);//add the image array to the firebase storage
                    getActivity().onBackPressed();
                }
                else{
                    Toast.makeText(getContext(), "wrong input please check your input!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private boolean bookInfoValidator(Book book) {
        String regex = "^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(book.getISBN());
        if (book.getAuthor().isEmpty() || book.getISBN().isEmpty() || book.getTitle().isEmpty()) {
            return false;
        }

        return true;
    }

    //this method is for choosing a new image for profile
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE);
    }

    private void scan() {

        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(NewBookEditFragment.this);
        integrator.setCaptureActivity(CodeCapture.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scanning");
        integrator.initiateScan();

    }

    private void fetchBook(String ISBN) {
        String queryString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN;
        new FetchBookTask().execute(ISBN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data==null){
                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_LONG).show();
            }
            else {
                MediaUri = data.getData();
                bookImgAdapter = new BookImageAdapter(getContext(),MediaUri);
                bookPager.setAdapter(bookImgAdapter);
                bookPager.setPadding(100, 0, 100, 0);
                Toast.makeText(getActivity(), "upload image successfully", Toast.LENGTH_LONG).show();
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {

                    fetchBook(result.getContents());

                } else {
                    Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    // citation: https://stackoverflow.com/a/16472082
    class FetchBookTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            final String isbn = strings[0];
            if(isCancelled()) {
                return null;
            }

            String query = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            try{
                HttpURLConnection connection = null;
                // Build Connection.
                try{
                    URL url = new URL(query);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000); // 5 seconds
                    connection.setConnectTimeout(5000); // 5 seconds
                } catch (MalformedURLException e) {
                    // Impossible: The only two URLs used in the app are taken from string resources.
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    // Impossible: "GET" is a perfectly valid request method.
                    e.printStackTrace();
                }
                int responseCode = connection.getResponseCode();
                if(responseCode != 200){
                    Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
                    connection.disconnect();
                    return null;
                }

                // Read data from response.
                StringBuilder builder = new StringBuilder();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = responseReader.readLine();
                while (line != null){
                    builder.append(line);
                    line = responseReader.readLine();
                }
                String responseString = builder.toString();
                Log.d(getClass().getName(), "Response String: " + responseString);
                JSONObject responseJson = new JSONObject(responseString);
                connection.disconnect();
                return responseJson;
            } catch (SocketTimeoutException e) {
                Log.w(getClass().getName(), "Connection timed out. Returning null");
                return null;
            } catch(IOException e){
                Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(isCancelled()) {
                Toast.makeText(getActivity(), "canceled", Toast.LENGTH_LONG).show();
            } else if(jsonObject == null) {
                Toast.makeText(getActivity(), "no result", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject basicJSON = jsonObject.getJSONArray("items")
                            .getJSONObject(0)
                            .getJSONObject("volumeInfo");
                    String title = basicJSON.getString("title");
                    String author = basicJSON.getJSONArray("authors").getString(0);
                    String ISBN = basicJSON.getJSONArray("industryIdentifiers")
                            .getJSONObject(1).getString("identifier");
                    showResult(title, author, ISBN);
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "No result, try to scan other books",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }
    private void showResult(String title, String author, String ISBN) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Title: " + title + "\nAuthor: " + author
                + "\nISBN-13: " + ISBN);

        builder.setPositiveButton("scan again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scan();
            }
        });
        builder.setNegativeButton("finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                        getActivity().onBackPressed();
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        newBookTitleET = getView().findViewById(R.id.newBookTitle);
        newBookAuthorET = getView().findViewById(R.id.newBookAuthor);
        newBookISBNET = getView().findViewById(R.id.newBookISBN);

        newBookTitleET.setText(title);
        newBookAuthorET.setText(author);
        newBookISBNET.setText(ISBN);
    }
}
