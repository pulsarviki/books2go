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
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SaleDetailsFragment extends Fragment {

    View view;

    public void refresh(){

        ImageButton backB = (ImageButton) view.findViewById(R.id.back);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView bookName= (TextView) view.findViewById(R.id.book_name);
        ImageView bookImg = (ImageView) view.findViewById(R.id.book_img);

        TextView status= (TextView) view.findViewById(R.id.status);

        final TextView name= (TextView) view.findViewById(R.id.name);
        final TextView author= (TextView) view.findViewById(R.id.author);
        final TextView version = (TextView) view.findViewById(R.id.version);
        final TextView condition= (TextView) view.findViewById(R.id.condition);
        final TextView price= (TextView) view.findViewById(R.id.price);

        TextView bid2 = (TextView) view.findViewById(R.id.bid2);
        TextView bid1 =(TextView)view.findViewById(R.id.bid1);
        TextView showBid =(TextView)view.findViewById(R.id.show_bid);

        Button cancelB = (Button) view.findViewById(R.id.cancel);
        Button confirmB = (Button) view.findViewById(R.id.confirm);

        bid1.setVisibility(view.VISIBLE);
        bid2.setVisibility(view.VISIBLE);
        showBid.setVisibility(view.VISIBLE);
        cancelB.setVisibility(view.VISIBLE);
        confirmB.setVisibility(view.VISIBLE);

        title.setText("Sale Details");

        final BookDetail item = MainActivity.model.getBook(MainActivity.saleSelected);

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

        name.setText(String.format("Title: %s", item.bookName));

        author.setText(String.format("Author: %s", item.author));

        version.setText(String.format("Version: %s", item.version));

        condition.setText(String.format("Condition: %s", item.condition));

        price.setText(String.format("Original Price: $%.2f", item.price));

        if (item.status.equals("sold")) {
            User buyer = MainActivity.model.getUser(item.buyer);
            status.setText(String.format("Purchased by %s", buyer.username));

            bid1.setText(buyer.username + " have purchased this item for");
            bid2.setVisibility(view.GONE);
            showBid.setText(String.format("$%.2f", item.finalPrice));

            confirmB.setVisibility(view.GONE);
            cancelB.setVisibility(view.GONE);
        }
        else if (item.status.equals("reserved")) {
            User user = MainActivity.model.getUser(item.reserveBy);
            status.setText(String.format("Reserved by %s", user.username));

            bid1.setText(user.username + " have reserved this item for");
            bid2.setVisibility(view.GONE);
            showBid.setText(String.format("$ %.2f", item.currentBid));

            confirmB.setText("Confirm transaction");

            confirmB.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                    int msgDate = (int)(c.getTime().getTime()/1000);

                    BookDetail newBook = new BookDetail(item.id, item.bookName, item.version, item.author,
                            item.bookImg, item.condition, item.price, item.finalPrice,item.seller, item.reserveBy,
                            item.reserveBy, item.currentBid, item.currentBidder, item.postDate, msgDate, "sold");

                    MainActivity.model.insertBook(newBook);

                    /*Message newMessage = new Message(UUID.randomUUID().toString(),newBook.id, newBook.seller,
                            newBook.reserveBy, "purchased", msgDate);


                    MainActivity.model.insertMessage(newMessage);*/

                    Log.d("bookPurchased", newBook.buyer + " paid " + newBook.price);

                    refresh();
                }
            }));

            cancelB.setText("remove book");
            cancelB.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetail newBook = new BookDetail(item.id, item.bookName, item.version, item.author, item.bookImg,
                            item.condition, item.price, -1, item.seller, item.buyer,
                            item.reserveBy, item.currentBid, item.currentBidder, item.postDate, item.purchasedDate,
                            "offMarket");

                    MainActivity.model.insertBook(newBook);
                    Log.d("removeBook", item.seller + " removed " + item.bookName);

                    /*Calendar c = Calendar.getInstance();
                    int msgDate = (int)(c.getTime().getTime()/1000);
                    Message newMessage = new Message(UUID.randomUUID().toString(),newBook.id, newBook.seller,
                            newBook.reserveBy, "removed", msgDate);

                    MainActivity.model.insertMessage(newMessage);

                    Log.d("sellerremovedBookMsg", newMessage.bookId + " seller:" + newMessage.sender +
                            " removed the book and send message to " + newMessage.receiver);*/

                    refresh();
                }
            }));
        }
        else if (item.status.equals("onMarket")){
            status.setText("On Market now");

            bid1.setText("Current Bid");
            bid2.setText("The current highest bid for this item is");
            showBid.setText(String.format("$ %.2f", item.currentBid));

            Button dealB = (Button) view.findViewById(R.id.confirm);

            if(item.currentBid == 1)
                dealB.setVisibility(view.GONE);
            else
            {
                dealB.setText(String.format("Ok, deal!"));

                dealB.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        BookDetail newBook = new BookDetail(item.id, item.bookName, item.version, item.author, item.bookImg,
                                item.condition, item.price, item.currentBid, item.seller, item.buyer, item.currentBidder,
                                item.currentBid, item.currentBidder, item.postDate, item.purchasedDate, "reserved");

                        MainActivity.model.insertBook(newBook);

                        Calendar c = Calendar.getInstance();
                        int msgDate = (int) (c.getTime().getTime() / 1000);
                        Message newMessage = new Message(UUID.randomUUID().toString(), newBook.id, newBook.seller,
                                newBook.currentBidder, "wonBid", msgDate);

                        MainActivity.model.insertMessage(newMessage);

                        Toast.makeText(getActivity(), "Deal successfully", Toast.LENGTH_LONG).show();

                        Log.d("dealmsg", newMessage.bookId + " " + newMessage.sender + " " + newMessage.receiver);
                        Log.d("dealbook", newBook.bookName + " " + "reserve by " + newBook.reserveBy);

                        refresh();
                    }
                });
            }

            cancelB.setText("remove Book");
            cancelB.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetail newBook = new BookDetail(item.id, item.bookName, item.version, item.author, item.bookImg,
                            item.condition, item.price, -1,item.seller, item.reserveBy,
                            item.reserveBy, item.currentBid, item.currentBidder, item.postDate, item.purchasedDate,
                            "offMarket");

                    MainActivity.model.insertBook(newBook);
                    Log.d("removedBook", item.seller + " removed " + item.bookName);

                    refresh();
                }
            }));
        }
        else if(item.status.equals("offMarket")){

            bid1.setVisibility(view.GONE);
            bid2.setVisibility(view.GONE);
            showBid.setVisibility(view.GONE);
            confirmB.setVisibility(view.GONE);

            Button onMarketB = (Button) view.findViewById(R.id.cancel);
            status.setText("Temporary off the Market");
            onMarketB.setText("Make it on the market now");

            onMarketB.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BookDetail rePostBook = new BookDetail(item.id, item.bookName, item.version, item.author, item.bookImg,
                            item.condition, item.price, -1, item.seller, item.reserveBy,
                            item.reserveBy, 1.00, "", item.postDate, item.purchasedDate, "onMarket");

                    MainActivity.model.insertBook(rePostBook);

                    Toast.makeText(getActivity(), "You have made " + item.bookName +
                            " on the market", Toast.LENGTH_LONG).show();

                    refresh();
                }
            }));
        }

        backB.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        }));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.sale_detail, container, false);

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