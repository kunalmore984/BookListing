package com.example.booklisting;

import static android.widget.Toast.LENGTH_LONG;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {
    // the variables are declared here
    private static final String  TAG = MainActivity.class.getName();
    private static String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int BOOK_ID = 1;
    private String query;
    private String new_url;
    private BookAdapter bookAdapter;
    private TextView emptytext;
    private EditText BookTitle;
    private Button submit_btn;
    ListView books_list;
    //private ProgressBar progressBar;
    /** Methods are declared here **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Listview from activity_main.xml
        books_list = (ListView) findViewById(R.id.book_list);
        BookTitle = (EditText) findViewById(R.id.edit_text);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        emptytext = (TextView) findViewById(R.id.empty_view);
        //Submit button to get the input from the user.....
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = BookTitle.getText().toString();
                if (TextUtils.isEmpty(query)){
                    Toast.makeText(MainActivity.this,"Please enter the text and continue",LENGTH_LONG).show();
                }else {
                    new_url = REQUEST_URL + query;
                    Log.v(TAG, "Request URL is : " + new_url);
                    justcontinue();
                }
            }
        });


    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        BookLoader earthquakeLoader = new BookLoader(MainActivity.this,new_url);
        Log.v(TAG,"OnCreateLoader message : 2");
        return earthquakeLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> data) {
        emptytext.setText("No books found");
//        progressBar.setVisibility(View.GONE);
        bookAdapter.clear();
        if (data != null && !data.isEmpty()){
            bookAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        bookAdapter.clear();
    }

    private void justcontinue(){
        bookAdapter =new BookAdapter(MainActivity.this,new ArrayList<Books>());
        //checking the connectivity of the device....
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnectedOrConnecting()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_ID,null,this);
        }else {
            //progressBar =(ProgressBar) findViewById(R.id.loading_indicator);
            //progressBar.setVisibility(View.GONE);
            emptytext.setText("No Internet ");
        }

        books_list.setAdapter(bookAdapter);
        books_list.setEmptyView(emptytext);

        books_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books books = bookAdapter.getItem(position);
                Uri uri = Uri.parse(books.getmPreview());

                Intent website = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(website);
            }
        });
    }
}