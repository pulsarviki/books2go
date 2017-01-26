package com.cs442.dliu33.booktogo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.User;

import java.util.ArrayList;


public class UserManagementAdapter extends BaseAdapter {

    public interface OnUserManagermentAdapterListener {
        public void refresh();
    }

    ArrayList<User> list;
    Activity activity;
    TextView txtName;
    TextView txtEmail;
    OnUserManagermentAdapterListener callback;

    public UserManagementAdapter(Activity activity, ArrayList<User> list, OnUserManagermentAdapterListener callback){
        super();
        this.activity=activity;
        this.list=list;
        this.callback = callback;
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

            convertView = inflater.inflate(R.layout.user_managerment, null);

            txtName = (TextView) convertView.findViewById(R.id.user_name);
            txtEmail = (TextView) convertView.findViewById(R.id.user_email);

            final Button deleteB = (Button)convertView.findViewById(R.id.delete);

            deleteB.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainActivity.model.removeUser(list.get(position).email);
                    callback.refresh();

                }
            });
        }

            User user = list.get(position);
            txtName.setText(String.format("%s", user.username));
            txtEmail.setText(String.format("%s",user.email));

            return convertView;
        }

}

