package com.cs442.dliu33.booktogo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.Message;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.User;

import java.util.ArrayList;


public class SaleHistoryAdapter extends BaseAdapter {

    ArrayList<BookDetail> list;
    Activity activity;

    public interface OnSaleHistoryAdapterListener {
        public void onShowSaleDetails(String id);
    }

    public SaleHistoryAdapter(Activity activity, ArrayList<BookDetail> list){
        super();
        this.activity=activity;
        this.list=list;

        ArrayList<BookDetail> mylist = list;
        for (BookDetail each : mylist) {
            Log.d("booklist", each.id + " " + each.bookName + " " + each.seller);
        }

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
            convertView = inflater.inflate(R.layout.sale_list, null);
        }

        ImageView bookImgLeft = (ImageView) convertView.findViewById(R.id.book_img);
        TextView txtMessage = (TextView) convertView.findViewById(R.id.message);

        BookDetail item = list.get(position);
        Log.d("saleAdapter", position + " "+ item.bookName );

        if (item.bookImg.equals("")) {
            bookImgLeft.setImageResource(R.drawable.no_image_found);
        }
        else if (item.bookImg.startsWith("base64|")) {
            byte[] buf = Base64.decode(item.bookImg.substring(7), Base64.DEFAULT);
            bookImgLeft.setImageBitmap(BitmapFactory.decodeByteArray(buf, 0, buf.length));
        }
        else {
            byte[] buf = Base64.decode(item.bookImg, Base64.DEFAULT);
            bookImgLeft.setImageBitmap(BitmapFactory.decodeByteArray(buf, 0, buf.length));
        }

        txtMessage.setText("You Posted the Book:\n" + item.bookName +
                "\nClick to see the details");

        txtMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnSaleHistoryAdapterListener callback = (OnSaleHistoryAdapterListener) activity;
                callback.onShowSaleDetails(list.get(position).id);
            }
        });

        bookImgLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnSaleHistoryAdapterListener callback = ( OnSaleHistoryAdapterListener) activity;
                callback.onShowSaleDetails(list.get(position).id);
            }
        });

        return convertView;
    }
}

