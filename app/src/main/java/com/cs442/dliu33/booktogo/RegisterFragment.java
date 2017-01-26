package com.cs442.dliu33.booktogo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.User;

public class RegisterFragment extends Fragment {

    public interface OnLoginListener {
        public void userRegister(User user);
        public void linkToLogin();
    }

    View view;

    String picUri = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.register, container, false);

        final EditText username = (EditText) view.findViewById(R.id.usernameInput);
        final EditText email = (EditText) view.findViewById(R.id.emailInput);
        final EditText password = (EditText) view.findViewById(R.id.passwordInput);

        Button register = (Button) view.findViewById(R.id.register);
        Button linkToLogin = (Button) view.findViewById(R.id.linkToLogin);

        register.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                        || password.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please fill out your information", Toast.LENGTH_LONG).show();
                }
                else if (password.getText().toString().length() < 4)
                {
                    Toast.makeText(getContext(), "Please enter a password with at least 4 characters",
                            Toast.LENGTH_LONG).show();
                }
                else if (MainActivity.model.checkUser(email.getText().toString()) == true){
                    Toast.makeText(getContext(), "User existed",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    OnLoginListener callback = (OnLoginListener) getActivity();
                    User newUser = new User(username.getText().toString(), email.getText().toString(),
                            password.getText().toString(), "");
                    Toast.makeText(getContext(), "register successfully",
                            Toast.LENGTH_LONG).show();
                    callback.userRegister(newUser);
                }
            }
        }));

        linkToLogin.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnLoginListener callback = (OnLoginListener) getActivity();
                callback.linkToLogin();
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
