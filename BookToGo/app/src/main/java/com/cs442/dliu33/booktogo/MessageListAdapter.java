package com.cs442.dliu33.booktogo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MessageListAdapter extends BaseAdapter {

    public interface OnMessageListAdapterListener {
        public void reload();
    }

    public interface OnMessageAdapterListener {
        public void onShowMsgDetails(String id);
    }

    final ArrayList<Message> list;
    final Activity activity;
    final OnMessageListAdapterListener callback;

    public MessageListAdapter(Activity activity, ArrayList<Message> list, OnMessageListAdapterListener callback){
        super();
        this.activity=activity;
        this.list=list;
        this.callback=callback;
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
            convertView = inflater.inflate(R.layout.message_list, null);
        }
        ImageView bookImgLeft = (ImageView) convertView.findViewById(R.id.book_img);
        TextView txtMessage = (TextView) convertView.findViewById(R.id.message);

        final Message msg = list.get(position);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msgDate = df.format((long) msg.msgDate * 1000);
        BookDetail item = MainActivity.model.getBook(msg.bookId);
        Log.d("msgAdapter", item.bookName);

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

        if (MainActivity.currentUser.email.equals("admin@iit.edu")){
            if (msg.bookStatus.equals("reserved")) {
                txtMessage.setText(MainActivity.model.getUserNameByEmail(msg.receiver) +
                    " reserved the item:\n" + item.bookName + "\nOn " + msgDate +
                    "\nClick to delete the message");
            }
            else if (msg.bookStatus.equals("wonBid")) {
                txtMessage.setText(MainActivity.model.getUserNameByEmail(msg.receiver)
                    +" won the bid for the item:\n" + item.bookName + "\nOn " + msgDate +
                    "\nClick to delete the message");
            }
            /*else if(msg.bookStatus.equals("cancelled")) {
                txtMessage.setText(MainActivity.model.getUserNameByEmail(msg.receiver)
                        +" cancelled the reserved item:\n" + item.bookName +
                        "\nClick to delete the message");
            }
            else if(msg.bookStatus.equals("removed")) {
                txtMessage.setText(MainActivity.model.getUserNameByEmail(msg.sender)
                        + " removed the item:\n" + item.bookName +
                        "\nClick to delete the message");
            }
            else if(msg.bookStatus.equals("purchased")) {
                txtMessage.setText("You purchased the item:\n" + item.bookName +
                        "\nClick to delete the message");
            }*/

           /*txtMessage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainActivity.model.removeMessage(msg.id);
                    Toast.makeText(v.getContext(), "message deleted", Toast.LENGTH_LONG).show();
                    callback.reload();

                }
            });

            bookImgLeft.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainActivity.model.removeMessage(msg.id);
                    Toast.makeText(v.getContext(), "message deleted", Toast.LENGTH_LONG).show();
                    callback.reload();
                }
            });*/
        }
        else {
            if (msg.bookStatus.equals("reserved")) {
                txtMessage.setText("You reserved the item:\n" + item.bookName + "\nOn " + msgDate +
                    "\nClick to see the details");
            } else if (msg.bookStatus.equals("wonBid")) {
                txtMessage.setText("You won the bid for the item:\n" + item.bookName + "\nOn " + msgDate +
                    "\nClick to see the details");
            }
            /*else if(msg.bookStatus.equals("cancelled")) {
                txtMessage.setText("You cancelled the reserved item:\n" + item.bookName +
                        "\nClick to see the details");
            }
            else if(msg.bookStatus.equals("removed")) {
                txtMessage.setText("Seller removed the item:\n" + item.bookName +
                        "\nClick to see the details");
            }
            else if(msg.bookStatus.equals("purchased")) {
                txtMessage.setText("You purchased the item:\n" + item.bookName +
                        "\nClick to see the details");
            }*/

            txtMessage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                OnMessageAdapterListener callback = (OnMessageAdapterListener) activity;
                callback.onShowMsgDetails(list.get(position).id);
                }
            });

            bookImgLeft.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                OnMessageAdapterListener callback = (OnMessageAdapterListener) activity;
                callback.onShowMsgDetails(list.get(position).id);
                }
            });
        }
        return convertView;
    }
}

