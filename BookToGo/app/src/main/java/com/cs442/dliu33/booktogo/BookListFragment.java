package com.cs442.dliu33.booktogo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookListFragment extends Fragment {

    public interface OnBookListener {
        public void linkToProfile();
        public void linkToAdmin();
        public void onShowDetails(String id);
    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.booklist, container, false);
        ImageButton backB = (ImageButton) view.findViewById(R.id.back);
        TextView title = (TextView) view.findViewById(R.id.title);

        TextView showMessage = (TextView) view.findViewById(R.id.show_message);
        final ListView list = (ListView) view.findViewById(R.id.list);

        final EditText keyword = (EditText) view.findViewById(R.id.keyword);
        ImageButton search = (ImageButton) view.findViewById(R.id.search);

        ImageButton profile = (ImageButton) view.findViewById(R.id.profile);

        ArrayList<BookDetail> books = new ArrayList<>();
        if(MainActivity.currentUser.email.equals("admin@iit.edu")){
            title.setText("Book Management");
            books = new ArrayList<>(MainActivity.model.getAllBooks());
            profile.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                OnBookListener callback = (OnBookListener) getActivity();
                callback.linkToAdmin();
                }
            }));
        }
        else{
            title.setText("Book List");
            books = new ArrayList<>(MainActivity.model.getBooksOnMarket());
            profile.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                OnBookListener callback = (OnBookListener) getActivity();
                callback.linkToProfile();
                }
            }));
        }

        sortAndListen(list,books);

        if (books.isEmpty())
            showMessage.setText("No book now");
        else
            showMessage.setVisibility(view.GONE);

        final ArrayList<BookDetail> booksForSearch = books;

        keyword.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        startSearch(list,booksForSearch,keyword);
                        return true;
                    default:
                        break;
                }
            }
            return false;
            }
        });

        search.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startSearch(list,booksForSearch,keyword);
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

    public void sortAndListen(final ListView list, final ArrayList<BookDetail> books ){
        Comparator cmp = Collections.reverseOrder(new Comparator<BookDetail>() {
            public int compare(BookDetail b1, BookDetail b2){
            if (b1.postDate < b2.postDate)
                return -1;
            else if (b1.postDate > b2.postDate)
                return 1;
            else
                return 0;
            }
        });
        Collections.sort(books,cmp);

        list.setAdapter(new BookListAdapter(getActivity(),books));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id)
            {
            BookDetail book = books.get(pos);
            OnBookListener callback = (OnBookListener) getActivity();
            callback.onShowDetails(book.id);
            }
        });
    }

    public void sortList(final ListView list, final ArrayList<BookDetail> books) {
        Comparator cmp = Collections.reverseOrder(new Comparator<BookDetail>() {
            public int compare(BookDetail b1, BookDetail b2) {
            if (b1.postDate < b2.postDate)
                return -1;
            else if (b1.postDate > b2.postDate)
                return 1;
            else
                return 0;
            }
        });
        Collections.sort(books, cmp);

        list.setAdapter(new BookListAdapter(getActivity(), books));
    }

    public void startSearch (ListView list, ArrayList<BookDetail> booksForSearch, EditText keyword){
        String searchWord = keyword.getText().toString();
        ArrayList<BookDetail> searchResult = new ArrayList<>();
        for (BookDetail book : booksForSearch){
            Log.d("search",book.bookName + "?" + searchWord);
            if (book.bookName.toUpperCase().contains(searchWord.toUpperCase())){
                searchResult.add(book);
            }
        }
        if(searchResult.isEmpty())
            Toast.makeText(getActivity(), "No book found", Toast.LENGTH_LONG).show();
        sortList(list,searchResult);
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