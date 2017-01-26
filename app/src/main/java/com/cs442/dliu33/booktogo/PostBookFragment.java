package com.cs442.dliu33.booktogo;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class PostBookFragment extends Fragment {

    public interface OnPostListener {
        public void submitBook();
        public void linkToProfile();
        public void addBookImg();
    }

    View view;

    String imgData = "";

    public void setBookImg(Uri uri){
        final ImageButton bookImg = (ImageButton) view.findViewById(R.id.add_book);
        Log.d("setBookImg", uri.toString());

        /*imgUri = uri.toString();
        bookImg.setImageURI(uri);*/

        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            bookImg.setImageBitmap(bmp);
            inputStream.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imgData = "base64|"+Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            baos.close();
        }
        catch (Exception e)
        {
            Log.e("setBookImg", e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.post_book, container, false);

        // Inflate the layout for this fragment

        ImageButton backB = (ImageButton) view.findViewById(R.id.back);
        ImageButton linkToProfile = (ImageButton) view.findViewById(R.id.profile);

        final TextView title = (TextView) view.findViewById(R.id.title);
        final EditText bookName = (EditText) view.findViewById(R.id.book_name);
        final EditText author = (EditText) view.findViewById(R.id.author);
        final EditText version = (EditText) view.findViewById(R.id.version);
        final EditText condition = (EditText) view.findViewById(R.id.condition);
        final EditText price = (EditText) view.findViewById(R.id.price);
        final ImageButton bookImg = (ImageButton) view.findViewById(R.id.add_book);

        Button postB = (Button) view.findViewById(R.id.post);
        Button cancelB = (Button) view.findViewById(R.id.cancel);


        title.setText("Sell a book");
        linkToProfile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnPostListener callback = (OnPostListener) getActivity();
                callback.linkToProfile();
            }
        }));

        postB.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int postDate = (int)(c.getTime().getTime()/1000);
                OnPostListener callback = (OnPostListener) getActivity();

                if (title.getText().toString().isEmpty() || bookName.getText().toString().isEmpty()
                        || author.getText().toString().isEmpty()|| version.getText().toString().isEmpty()||
                        condition.getText().toString().isEmpty()|| price.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "please fill out your information", Toast.LENGTH_LONG).show();
                }
                else {
                    BookDetail newBook = new BookDetail(UUID.randomUUID().toString(),
                            bookName.getText().toString(), version.getText().toString(),
                            author.getText().toString(), imgData, condition.getText().toString(),
                            Double.parseDouble(price.getText().toString()), -1,
                            MainActivity.currentUser.email, "", "", 1.00, "", postDate, postDate, "onMarket");

                    MainActivity.model.insertBook(newBook);

                    callback.submitBook();
                }
            }
        }));

        cancelB.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        }));

        bookImg.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnPostListener callback = (OnPostListener) getActivity();
                callback.addBookImg();
            }
        }));

        backB.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
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
