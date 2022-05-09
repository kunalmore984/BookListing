package com.example.booklisting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Books> {
    private String TAG = BookAdapter.class.getName();
    public BookAdapter(Context context, List<Books> books){
        super(context,0,books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview = convertView;
        if (listview == null){
            listview = LayoutInflater.from(getContext()).inflate(R.layout.book_item,parent,false);
        }
        Books current_book = getItem(position);

        TextView title_view = (TextView)listview.findViewById(R.id.title);
        title_view.setText(current_book.getmTitle());

        TextView pages = (TextView) listview.findViewById(R.id.pages);
        String pg = String.valueOf(current_book.getmPages());
        pages.setText("Pages : "+pg);

        TextView author = (TextView) listview.findViewById(R.id.Author);
        author.setText("Author : "+current_book.getmAuthor());

         //failed attemt to display image from parsed json string.........😡😡😡
        String image = current_book.getmImage();
        Log.v(TAG,"cheking string link in adapter : "+image);
        ImageView imageView = listview.findViewById(R.id.book_img);

        /*Bitmap drawable = null;g
        try {
            InputStream in = new URL(image).openStream();
            drawable = BitmapFactory.decodeStream(in);
        }catch (Exception e){
            e.printStackTrace();
        }
        imageView.setImageBitmap(drawable);*/
        Picasso.get().load(image).into(imageView);
        imageView.setVisibility(View.VISIBLE);

        return listview;
    }

}

