package com.cs442.dliu33.booktogo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomePageFragment extends Fragment {

    public interface OnHomePageListener {
        public void linkToLogin();
        public void linkToRegister();
    }
    View view;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_page, container, false);
        Button login = (Button)view.findViewById(R.id.login);
        Button register = (Button)view.findViewById(R.id.register);

        login.setStateListAnimator(null);
        register.setStateListAnimator(null);

        login.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnHomePageListener callback = (OnHomePageListener) getActivity();
                callback.linkToLogin();
            }
        }));

        register.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnHomePageListener callback = (OnHomePageListener) getActivity();
                callback.linkToRegister();
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
