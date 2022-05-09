package com.example.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtlis {

    private static final String TAG = QueryUtlis.class.getName();

    private QueryUtlis(){

    }

    private static List<Books> extractJSON( String BooksJSON){
        //chacking whether the argument is empty
        if (TextUtils.isEmpty(BooksJSON)){
            return null;
        }
        ArrayList<Books> booksList =new ArrayList<>();
        try {
            JSONObject root = new JSONObject(BooksJSON);
            JSONArray items = root.getJSONArray("items");
            for (int i=0;i< items.length();i++){
                JSONObject item_obj = items.getJSONObject(i);
                JSONObject volumeInfo = item_obj.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                JSONArray authors = volumeInfo.getJSONArray("authors");
                String author =authors.getString(0);
                int pages = volumeInfo.getInt("pageCount");
                String curl = volumeInfo.getString("previewLink");
                JSONObject imglinks = volumeInfo.getJSONObject("imageLinks");
                String img = imglinks.getString("smallThumbnail");
                Log.v(TAG,"checking image url : "+img);
                //img=img+".jpg";
                Books books = new Books(title,curl,pages,author,img+".jpg");
                booksList.add(books);
            }

        }catch (JSONException e){
            Log.e(TAG,"JSon error : "+e);
        }
        return booksList;
    }

    public static List<Books> FetchBooks(String urls){
        URL url = createurl(urls);
        String jsonresponse = "";
        try {
            jsonresponse = makehttprequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }
        List<Books> BooksList = extractJSON(jsonresponse);

        return BooksList;
    }

    private static URL createurl(String Url){
        URL newurl = null;
        try {
            newurl = new URL(Url);
        }catch (MalformedURLException e){
            Log.e(TAG,"error : "+e);
            return null;
        }
        return newurl;
    }

    private static String makehttprequest(URL murl) throws IOException{
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        if (murl == null){
            return jsonResponse;
        }
        try {
            urlConnection = (HttpURLConnection) murl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG,"Error Response code : "+urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(TAG,"IOException error : ",e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream input) throws IOException{
        StringBuilder output = new StringBuilder();
        if (input != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
