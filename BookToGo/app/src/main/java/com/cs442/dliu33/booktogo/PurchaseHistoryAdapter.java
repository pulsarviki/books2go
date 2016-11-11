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

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class PurchaseHistoryAdapter extends BaseAdapter {

    ArrayList<BookDetail> list;
    Activity activity;

    public PurchaseHistoryAdapter(Activity activity, ArrayList<BookDetail> list){
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
            convertView = inflater.inflate(R.layout.purchase_list, null);
        }

        ImageView bookImgLeft = (ImageView) convertView.findViewById(R.id.book_img);
        TextView txtPurchaseDetail = (TextView) convertView.findViewById(R.id.purchase_detail);
        BookDetail item = list.get(position);
        Log.d("PuchaseHisotryAdapter", item.bookName);

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

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        txtPurchaseDetail.setText(String.format("%s\nPurchased at $ %.2f\nOn %s",item.bookName,
                item.finalPrice, df.format((long)item.purchasedDate*1000)));

        return convertView;
    }
}

