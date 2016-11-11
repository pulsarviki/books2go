package com.cs442.dliu33.booktogo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.Message;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MessageListFragment extends Fragment implements MessageListAdapter.OnMessageListAdapterListener{

    public interface OnMessageListener {
        public void linkToProfile();
        public void linkToAdmin();
    }

    public void reload(){
        final ListView list = (ListView) view.findViewById(R.id.message_list);
        final ArrayList<Message> messages = new ArrayList<>(MainActivity.model.getAllMessages());
        sortList(list,messages);

       /*MessageListAdapter adapter = new MessageListAdapter(getActivity(),
                new ArrayList<>(MainActivity.model.getAllMessages()),this);
        list.setAdapter(adapter);*/

    }
    View view;

    public void refresh() {
        ImageButton backB = (ImageButton) view.findViewById(R.id.back);
        TextView title = (TextView) view.findViewById(R.id.title);
        ImageButton profile = (ImageButton) view.findViewById(R.id.profile);
        ListView list = (ListView) view.findViewById(R.id.message_list);
        TextView showMessage = (TextView) view.findViewById(R.id.show_message);

        ArrayList<Message> messages = new ArrayList<>();
        if (MainActivity.currentUser.email.equals("admin@iit.edu")){
            title.setText("Message Management");
            messages = new ArrayList<>(MainActivity.model.getAllMessages());
            profile.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnMessageListener callback = (OnMessageListener) getActivity();
                    callback.linkToAdmin();
                }
            }));
            sortAndRemoveList(list,messages);
        }
        else{
            title.setText("Message");
            messages = new ArrayList<>(MainActivity.model.getMessages(MainActivity.currentUser.email));
            profile.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnMessageListener callback = (OnMessageListener) getActivity();
                    callback.linkToProfile();
                }
            }));
            sortList(list, messages);
        }

        if (messages.isEmpty())
            showMessage.setText("No message now");
        else
            showMessage.setVisibility(view.GONE);

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
        view = inflater.inflate(R.layout.messagelist, container, false);

        refresh();

        return view;
    }

    public void sortList(ListView list, final ArrayList<Message> messages ){
        Comparator cmp = Collections.reverseOrder(new Comparator<Message>() {
            public int compare(Message b1, Message b2){
                if (b1.msgDate < b2.msgDate)
                    return -1;
                else if (b1.msgDate > b2.msgDate)
                    return 1;
                else
                    return 0;
            }
        });
        Collections.sort(messages,cmp);
        list.setAdapter(new MessageListAdapter(getActivity(), messages,this));
    }

    public void sortAndRemoveList(ListView list, final ArrayList<Message> messages ){
        Comparator cmp = Collections.reverseOrder(new Comparator<Message>() {
            public int compare(Message b1, Message b2){
                if (b1.msgDate < b2.msgDate)
                    return -1;
                else if (b1.msgDate > b2.msgDate)
                    return 1;
                else
                    return 0;
            }
        });
        Collections.sort(messages,cmp);
        list.setAdapter(new MessageListAdapter(getActivity(), messages,this));

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> p, View v, int pos, long id)
            {
                Message msg = messages.get(pos);
                MainActivity.model.removeMessage(msg.id);
                Toast.makeText(v.getContext(), "message deleted", Toast.LENGTH_LONG).show();
                refresh();
                return true;
            }
        });
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