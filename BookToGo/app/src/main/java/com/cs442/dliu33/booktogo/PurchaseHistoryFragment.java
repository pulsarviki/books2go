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

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PurchaseHistoryFragment extends Fragment {

    public interface OnPurchaseHistoryListener {
        public void linkToProfile();
    }

    View view;

    public void refresh() {
        ImageButton backB = (ImageButton) view.findViewById(R.id.back);
        ImageButton profile = (ImageButton) view.findViewById(R.id.profile);
        TextView title1 = (TextView) view.findViewById(R.id.title);
        ListView list = (ListView) view.findViewById(R.id.purchase_list);
        TextView showMessage = (TextView) view.findViewById(R.id.show_message);

        title1.setText("Purchase History");

        final ArrayList<BookDetail> books =
                new ArrayList<>(MainActivity.model.getPurchasedBooks(MainActivity.currentUser.email));

        Comparator cmp = Collections.reverseOrder(new Comparator<BookDetail>() {
            public int compare(BookDetail b1, BookDetail b2){
                if (b1.purchasedDate < b2.purchasedDate)
                    return -1;
                else if (b1.purchasedDate > b2.purchasedDate)
                    return 1;
                else
                    return 0;
            }
        });
        Collections.sort(books,cmp);

        list.setAdapter(new PurchaseHistoryAdapter(getActivity(),books));

        if(books.isEmpty())
            showMessage.setText("No purchase now");
        else
            showMessage.setVisibility(view.GONE);

        profile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnPurchaseHistoryListener callback = (OnPurchaseHistoryListener) getActivity();
                callback.linkToProfile();
            }
        }));

        backB.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.purchaselist, container, false);

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