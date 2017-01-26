package com.cs442.dliu33.booktogo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class BookDetailsFragment extends Fragment {

    public interface OnBookDetailsListener {
        public void linkToMessage();
        public void linkToFeed();
    }
    View view;

    public void refresh() {
        ImageButton backB = (ImageButton) view.findViewById(R.id.back);
        ImageView bookImg = (ImageView) view.findViewById(R.id.add_book);

        TextView title2 = (TextView) view.findViewById(R.id.title2);
        final TextView info = (TextView) view.findViewById(R.id.post_info);
        final TextView bookName = (TextView) view.findViewById(R.id.book_name);

        final TextView author = (TextView) view.findViewById(R.id.author);
        final TextView version = (TextView) view.findViewById(R.id.version);
        final TextView condition = (TextView) view.findViewById(R.id.condition);
        final TextView price = (TextView) view.findViewById(R.id.price);

        TextView bid1 = (TextView) view.findViewById(R.id.bid1);
        TextView bid2 = (TextView) view.findViewById(R.id.bid2);
        final TextView showBid = (TextView) view.findViewById(R.id.show_bid);
        TextView bid3 = (TextView) view.findViewById(R.id.bid3);
        final TextView bid4 = (TextView) view.findViewById(R.id.bid4);

        ImageView line = (ImageView) view.findViewById(R.id.line);
        final EditText enterBid = (EditText) view.findViewById(R.id.enter_bid);
        Button reserveB = (Button) view.findViewById(R.id.reserve);

        title2.setText("book description");

        bid1.setVisibility(view.VISIBLE);
        bid2.setVisibility(view.VISIBLE);
        showBid.setVisibility(view.VISIBLE);
        bid3.setVisibility(view.VISIBLE);
        bid4.setVisibility(view.VISIBLE);
        line.setVisibility(view.VISIBLE);
        enterBid.setVisibility(view.VISIBLE);
        reserveB.setVisibility(view.VISIBLE);

        backB.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        }));

        final BookDetail item = MainActivity.model.getBook(MainActivity.selected);

        bookName.setText(String.format("%s", item.bookName));
        if (item.bookImg.equals("")) {
            bookImg.setImageResource(R.drawable.no_image_found);
        } else if (item.bookImg.startsWith("base64|")) {
            byte[] buf = Base64.decode(item.bookImg.substring(7), Base64.DEFAULT);
            bookImg.setImageBitmap(BitmapFactory.decodeByteArray(buf, 0, buf.length));
        } else {
            byte[] buf = Base64.decode(item.bookImg, Base64.DEFAULT);
            bookImg.setImageBitmap(BitmapFactory.decodeByteArray(buf, 0, buf.length));
        }

        String sellerName = MainActivity.model.getUserNameByEmail(item.seller);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        info.setText(String.format("Posted by " + sellerName + " on " + df.format(
                (long) item.postDate * 1000)));

        author.setText(String.format("Author: %s", item.author));

        version.setText(String.format("Version: %s", item.version));

        condition.setText(String.format("Condition: %s", item.condition));

        price.setText(String.format("Original Price: $%.2f", item.price));

        if (MainActivity.currentUser.email.equals("admin@iit.edu")) {

            bid1.setVisibility(view.GONE);
            bid2.setVisibility(view.GONE);
            showBid.setVisibility(view.GONE);
            bid3.setVisibility(view.GONE);
            bid4.setVisibility(view.GONE);
            line.setVisibility(view.GONE);
            enterBid.setVisibility(view.GONE);

            Button removeBook = (Button) view.findViewById(R.id.reserve);

            if(item.status.equals("offMarketByAdmin")) {
                info.setText("The item has been removed by Admin");
                removeBook.setText("Go Back");
                removeBook.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                    }
                }));
            }
            else {
                removeBook.setText("Remove Book");

                removeBook.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        BookDetail newBook = new BookDetail(item.id, item.bookName, item.version, item.author, item.bookImg,
                                item.condition, item.price, item.finalPrice, item.seller, item.buyer, item.reserveBy,
                                item.currentBid, item.currentBidder, item.postDate, item.purchasedDate, "offMarketByAdmin");
                        MainActivity.model.insertBook(newBook);

                        Toast.makeText(getContext(), "Removed successfully", Toast.LENGTH_LONG).show();

                        refresh();
                    }
                });
            }

        } else if (MainActivity.currentUser.email.equals(item.seller)) {

            bid1.setText("Current Bid");
            bid2.setText("The current highest bid for this item is");
            showBid.setText(String.format("$ %.2f", item.currentBid));
            line.setVisibility(view.GONE);
            enterBid.setVisibility(view.GONE);
            bid3.setVisibility(view.GONE);
            bid4.setVisibility(view.GONE);
            reserveB.setVisibility(view.GONE);

        } else if (MainActivity.currentUser.email.equals(item.reserveBy)){

            bid1.setVisibility(view.GONE);
            bid3.setVisibility(view.GONE);
            bid4.setVisibility(view.GONE);
            line.setVisibility(view.GONE);
            enterBid.setVisibility(view.GONE);

            bid2.setText("You have reserved this item for ");
            showBid.setText(String.format("$ %.2f", item.price));
            Button shopping = (Button) view.findViewById(R.id.reserve);

            shopping.setText("Continue Shopping");

            shopping.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    OnBookDetailsListener callback = (OnBookDetailsListener) getActivity();
                    callback.linkToFeed();
                }
            });
        }

        else{
            bid1.setText("Current Bid");
            bid2.setText("The current highest bid for this item is");
            showBid.setText(String.format("$ %.2f", item.currentBid));
            bid3.setText("Your Bid");
            bid4.setText(String.format("Enter a value greater than $ %.2f", item.currentBid));
            reserveB.setText(String.format("Reserve Now for $%.2f", item.price));

            enterBid.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_ENTER:
                                if (Double.parseDouble(enterBid.getText().toString()) <= item.currentBid) {
                                    Toast.makeText(getActivity(), "Enter a value greater than current bid",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    BookDetail newBook = new BookDetail(item.id, item.bookName, item.version, item.author,
                                            item.bookImg, item.condition, item.price, item.finalPrice, item.seller, item.buyer,
                                            item.reserveBy, Double.parseDouble(enterBid.getText().toString()),
                                            MainActivity.currentUser.email, item.postDate, item.purchasedDate, item.status);

                                    MainActivity.model.insertBook(newBook);
                                    Toast.makeText(getActivity(), "Thanks for your bidding", Toast.LENGTH_LONG).show();

                                    Log.d("enterBid", newBook.bookName + " buyer:" + newBook.buyer + " reservedBy:" +
                                            newBook.reserveBy + " currentBid:" + newBook.currentBid + " currentBidder:"
                                            + newBook.currentBidder + " status:" + newBook.status);

                                    showBid.setText(String.format("$ %.2f", newBook.currentBid));
                                    bid4.setText(String.format("Enter a value greater than $ %.2f", newBook.currentBid));
                                }
                                return true;
                            default:
                                break;
                        }
                    }
                    return false;
                }
            });

            reserveB.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    BookDetail newBook = new BookDetail(item.id, item.bookName, item.version, item.author, item.bookImg,
                            item.condition, item.price, item.price, item.seller, item.buyer, MainActivity.currentUser.email,
                            item.currentBid, item.currentBidder, item.postDate, item.purchasedDate, "reserved");

                    MainActivity.model.insertBook(newBook);

                    Calendar c = Calendar.getInstance();
                    int msgDate = (int) (c.getTime().getTime() / 1000);
                    Message newMessage = new Message(UUID.randomUUID().toString(), newBook.id,
                            newBook.seller, MainActivity.currentUser.email, "reserved", msgDate);

                    MainActivity.model.insertMessage(newMessage);

                    Log.d("reservationMsg", newMessage.bookId + " " + newMessage.sender + " " + newMessage.receiver);
                    Log.d("reservation", newBook.bookName + " " + "reserve by " + newBook.reserveBy);

                    Toast.makeText(getActivity(), "Reserved successfully", Toast.LENGTH_LONG).show();

                    OnBookDetailsListener callback = (OnBookDetailsListener) getActivity();
                    callback.linkToMessage();
                }
            });
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.book_detail, container, false);

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
