package com.cs442.dliu33.booktogo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.mongodb.client.model.geojson.Position;

import java.util.ArrayList;


public class BookListAdapter extends BaseAdapter {

    ArrayList<BookDetail> list;
    Activity activity;

    public interface OnBookItemListener {
        public void onShowDetails(String id);
    }

    public BookListAdapter(Activity activity, ArrayList<BookDetail> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position; //list.get(position).id;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.book_list, null);
        }

        TextView txtItem = (TextView) convertView.findViewById(R.id.book_txt);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.book_price);
        ImageView bookImg = (ImageView) convertView.findViewById(R.id.book_image);

        final ImageButton imgB = (ImageButton)convertView.findViewById(R.id.show_details);

        imgB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnBookItemListener callback = (OnBookItemListener) activity;
                callback.onShowDetails(list.get(position).id);
            }
        });

        BookDetail item = list.get(position);
        txtItem.setText(String.format("%s", item.bookName));
        txtPrice.setText(String.format("$%.2f",item.price));

        if (item.bookImg.equals("")) {
            bookImg.setImageResource(R.drawable.no_image_found);
        }
        else if (item.bookImg.startsWith("base64|")) {
            byte[] buf = Base64.decode(item.bookImg.substring(7), Base64.DEFAULT);
            bookImg.setImageBitmap(BitmapFactory.decodeByteArray(buf, 0, buf.length));
        }
        else {
            byte[] buf = Base64.decode(item.bookImg, Base64.DEFAULT);
            bookImg.setImageBitmap(BitmapFactory.decodeByteArray(buf, 0, buf.length));
        }

        return convertView;
    }

}

