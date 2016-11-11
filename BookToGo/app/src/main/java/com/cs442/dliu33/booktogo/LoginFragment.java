package com.cs442.dliu33.booktogo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;

public class LoginFragment extends Fragment {

    public interface OnLoginListener {
        public void linkToRegister();
        public void userLogin(String email, String password);
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login, container, false);

        ImageView logoLogin = (ImageView) view.findViewById(R.id.logo_login);

        final EditText email = (EditText) view.findViewById(R.id.email);
        final EditText password = (EditText) view.findViewById(R.id.password);
        Button login = (Button) view.findViewById(R.id.login);
        Button linkToRegister = (Button) view.findViewById(R.id.linkToRegister);

        email.setText(MainActivity.lastUser.email);

        linkToRegister.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnLoginListener callback = (OnLoginListener) getActivity();
                callback.linkToRegister();
            }
        }));

        password.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            OnLoginListener callback = (OnLoginListener) getActivity();
                            callback.userLogin(email.getText().toString(), password.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        login.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnLoginListener callback = (OnLoginListener) getActivity();
                callback.userLogin(email.getText().toString(), password.getText().toString());
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
