package com.cs442.dliu33.booktogo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class AdminProfileFragment extends Fragment {

    public interface OnAdminProfileListener {
        public void linkToUser();
        public void linkToMessage();
        public void linkToBook();
        public void backToLogin();
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.admin_profile, container, false);

        ImageButton back = (ImageButton) view.findViewById(R.id.back);
        ImageButton showDetail = (ImageButton) view.findViewById(R.id.show_details);

        ImageView picture = (ImageView) view.findViewById(R.id.show_pic);

        TextView showName = (TextView) view.findViewById(R.id.show_name);
        Button user = (Button) view.findViewById(R.id.user);
        Button book= (Button) view.findViewById(R.id.book);
        Button message = (Button) view.findViewById(R.id.message);
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

        user.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAdminProfileListener callback = (OnAdminProfileListener) getActivity();
                callback.linkToUser();
            }
        }));

        book.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAdminProfileListener callback = (OnAdminProfileListener) getActivity();
                callback.linkToBook();
            }
        }));

        message.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAdminProfileListener callback = (OnAdminProfileListener) getActivity();
                callback.linkToMessage();
            }
        }));

        logout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentUser = new User("","","","");
                OnAdminProfileListener callback = (OnAdminProfileListener) getActivity();
                callback.backToLogin();
            }
        }));

        return view;
    }

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
        catch (Exception e) {
            Log.e("setUserPic", e.toString());
        }
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
