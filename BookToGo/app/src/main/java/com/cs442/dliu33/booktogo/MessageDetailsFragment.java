package com.cs442.dliu33.booktogo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MessageDetailsFragment extends Fragment {

    public interface OnMessageDetailsListener {
        public void linkToFeed();
    }

    View view;

    public void refresh(){

        ImageButton backB = (ImageButton) view.findViewById(R.id.back);

        TextView title2 = (TextView) view.findViewById(R.id.title2);
        TextView bookName= (TextView) view.findViewById(R.id.book_name);
        ImageView bookImg = (ImageView) view.findViewById(R.id.book_img);
        TextView info= (TextView) view.findViewById(R.id.info);

        TextView bid1 = (TextView) view.findViewById(R.id.bid1);
        TextView showBid = (TextView) view.findViewById(R.id.show_bid);

        Button cancelB = (Button) view.findViewById(R.id.cancel_reservation);

        title2.setText("Message Details");

        final Message msg = MainActivity.model.getMessage(MainActivity.msgSelected);

        final BookDetail item = MainActivity.model.getBook(msg.bookId);

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

        bookName.setText(String.format("%s", item.bookName));

        backB.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        }));

        if(item.status.equals("offMarket") ||item.status.equals("offMarketByAdmin") ){
            info.setText("Sorry, the item is not on the market now.");
            bid1.setVisibility(view.GONE);
            showBid.setVisibility(view.GONE);
            cancelB.setVisibility(view.GONE);
        }
        else if (item.status.equals("reserved")) {

            if (item.reserveBy.equals(MainActivity.currentUser.email)){

                String sellerName = MainActivity.model.getUserNameByEmail(item.seller);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                info.setText(String.format("Posted by "+ sellerName + " on " + df.format(
                        (long)item.postDate*1000)));

                bid1.setText("You have reseved this item for");
                showBid.setText(String.format("$%.2f", item.finalPrice));

                cancelB.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BookDetail newBook = new BookDetail(item.id, item.bookName, item.version, item.author, item.bookImg,
                                item.condition, item.price, -1 ,item.seller, item.buyer, "", 1,
                                "", item.postDate, item.purchasedDate, "onMarket");
                        Log.d("cancelReserved", MainActivity.currentUser.email + " cancel reserved book " + newBook.bookName);

                        MainActivity.model.insertBook(newBook);

                        Toast.makeText(getActivity(), "Cancel Reservation successfully", Toast.LENGTH_LONG).show();
                        /*Calendar c = Calendar.getInstance();
                        int msgDate = (int)(c.getTime().getTime()/1000);
                        Message newMessage = new Message(UUID.randomUUID().toString(), newBook.id, newBook.seller,
                                MainActivity.currentUser.email, "cancelled", msgDate);

                        MainActivity.model.insertMessage(newMessage);*/

                        refresh();
                    }
                }));
        }
            else{
                info.setText(item.reserveBy + " have reserved this book");
                showBid.setVisibility(view.GONE);
                cancelB.setVisibility(view.GONE);
                bid1.setVisibility(view.GONE);
            }

        }
        else if (item.status.equals("sold")) {

            cancelB.setVisibility(view.GONE);

            if (item.buyer.equals(MainActivity.currentUser.email)) {
                info.setText("The item is off the market");
                bid1.setText("You have purchased this item for");
                showBid.setText(String.format("$%.2f", item.finalPrice));
            }
            else {
                info.setText("The item is off the market");
                bid1.setText(item.buyer + " have purchased this item");
                showBid.setText(String.format("$%.2f", item.finalPrice));

            }
        }
        else if (item.status.equals("onMarket")){
            info.setText("The item is on the market");
            bid1.setText("The current price for the item is ");
            showBid.setText(String.format("$%.2f", item.price));
            Button shopping = (Button)view.findViewById(R.id.cancel_reservation);
            shopping.setText("Continue Shopping");
            shopping.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                OnMessageDetailsListener callback = (OnMessageDetailsListener) getActivity();
                callback.linkToFeed();
                }
            });
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.message_detail, container, false);

        refresh();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}