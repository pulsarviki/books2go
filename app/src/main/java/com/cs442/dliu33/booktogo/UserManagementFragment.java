package com.cs442.dliu33.booktogo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.User;

import java.util.ArrayList;

public class UserManagementFragment extends Fragment implements UserManagementAdapter.OnUserManagermentAdapterListener{

    public interface OnUserManagementListener {
        public void linkToAdmin();
    }

    View view;

    public void refresh(){
        final ListView list = (ListView) view.findViewById(R.id.user_list);
        UserManagementAdapter adapter = new UserManagementAdapter(getActivity(),
                new ArrayList<User>(MainActivity.model.getUsersWithoutAdmin()),this);
        list.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.usermanagerment, container, false);

        TextView title = (TextView) view.findViewById(R.id.title);

        ImageButton profile = (ImageButton) view.findViewById(R.id.profile);

        ImageButton backB = (ImageButton) view.findViewById(R.id.back);

        title.setText("User Managerment");

        profile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnUserManagementListener callback = (OnUserManagementListener) getActivity();
                callback.linkToAdmin();
            }
        }));

        backB.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        }));

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
