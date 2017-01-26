package com.cs442.dliu33.booktogo;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UserProfileFragment extends Fragment {

    public interface OnProfileListener {
        public void linkToFeed();
        public void linkToPost();
        public void linkToHistory();
        public void backToLogin();
        public void linkToMessage();
        public void setUserPic();
        public void linkToSale();
    }

    View view;

    public void setUserPic(Uri uri){
        final ImageButton picture = (ImageButton) view.findViewById(R.id.show_pic);
        Log.d("setUserPic", uri.toString());

        try {
            InputStream in = getContext().getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(in);
            picture.setImageBitmap(bmp);
            in.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String picData = "base64|"+ Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            baos.close();

            MainActivity.currentUser = new User(MainActivity.currentUser.username,
                    MainActivity.currentUser.email, MainActivity.currentUser.password, picData);
            MainActivity.model.insertUser(MainActivity.currentUser);
        }
        catch (Exception e)
        {
            Log.e("setUserPic", e.toString());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.userprofile, container, false);

        ImageView picture = (ImageView) view.findViewById(R.id.show_pic);

        TextView showName = (TextView) view.findViewById(R.id.show_name);
        Button feed = (Button) view.findViewById(R.id.feed);
        Button purchaseHistory = (Button) view.findViewById(R.id.puchaseHis);
        Button message = (Button) view.findViewById(R.id.message);
        Button post = (Button) view.findViewById(R.id.post);
        Button sale = (Button) view.findViewById(R.id.sale);
        Button logout = (Button) view.findViewById(R.id.logout);

        showName.setText(MainActivity.currentUser.username);

        if ((MainActivity.currentUser.picture == null)
            || MainActivity.currentUser.picture.equals(""))
            picture.setImageResource(R.drawable.no_pic);
        else if (MainActivity.currentUser.picture.startsWith("base64|")) {
            byte[] buf = Base64.decode(MainActivity.currentUser.picture.substring(7), Base64.DEFAULT);
            picture.setImageBitmap(BitmapFactory.decodeByteArray(buf, 0, buf.length));
        }
        else {
            byte[] buf = Base64.decode(MainActivity.currentUser.picture, Base64.DEFAULT);
            picture.setImageBitmap(BitmapFactory.decodeByteArray(buf, 0, buf.length));
        }

        feed.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnProfileListener callback = (OnProfileListener) getActivity();
                callback.linkToFeed();
            }
        }));

        message.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnProfileListener callback = (OnProfileListener) getActivity();
                callback.linkToMessage();
            }
        }));

        purchaseHistory.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnProfileListener callback = (OnProfileListener) getActivity();
                callback.linkToHistory();
            }
        }));

        post.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnProfileListener callback = (OnProfileListener) getActivity();
                callback.linkToPost();
            }
        }));

        picture.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnProfileListener callback = (OnProfileListener) getActivity();
                callback.setUserPic();
            }
        }));

        sale.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnProfileListener callback = (OnProfileListener) getActivity();
                callback.linkToSale();
            }
        }));

        logout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentUser = new User("","","","");
                OnProfileListener callback = (OnProfileListener) getActivity();
                callback.backToLogin();
            }
        }));

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
