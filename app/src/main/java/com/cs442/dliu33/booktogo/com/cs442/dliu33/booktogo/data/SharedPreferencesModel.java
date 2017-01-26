package com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class SharedPreferencesModel implements Model{

    public SharedPreferencesModel(Activity activity) {
        this.activity = activity;
        this.books = loadBooks();
        this.users = loadUsers();
        this.messages = loadMessages();

        Log.d("load shared preference", String.format(
                "books %d, users %d, msgs %d",
                books.size(), users.size(), messages.size()));

        if(books.isEmpty()){
              /*  bookDetail.add(new BookDetail(++count, "Android: Programming Conceptes","Thomas Cormen","12th Edition",
                        109.90, "Good"));
                bookDetail.add(new BookDetail(++count, "Code Complete","Steve McConnel","1st Edition",
                        108.99, "Very good"));
                bookDetail.add(new BookDetail(++count, "Head First Java","Bert Bates", "10th Edition",
                        194.29, "Excellent"));
                bookDetail.add(new BookDetail(++count, "Computer Networks","Andrew Tanenbaum", "2nd Edition",
                        208.49, "Good"));
                bookDetail.add(new BookDetail(++count, "Algorithm Design","Jon Kleinberg", "8th Edition",
                        105.99, "Excellent"));
                bookDetail.add(new BookDetail(++count, "Effective C++","Scott Meyers","5th Edition",
                        120.99, "Good"));
            this.books.put("1",new BookDetail("1", "Android: Programming Conceptes","12th Edition","Thomas Cormen","",
                    "Good", 109.90,"","",""));
            saveBooks();*/
        }
    }

    @Override
    public Collection<BookDetail> getAllBooks() {
        return books.values();
    }

    @Override
    public BookDetail getBook(String id) {
        return books.get(id);
    }

    @Override
    public void insertBook(BookDetail book) {
        books.put(book.id, book);
        saveBooks();
    }

    @Override
    public Collection<BookDetail> getPurchasedBooks(String email) {
        ArrayList<BookDetail> books = new ArrayList<>();
        Collection<BookDetail> allBooks = getAllBooks();
        for (BookDetail book: allBooks){
            if (book.buyer.equals(email))
                books.add(book);
        }
        return books;
    }

    @Override
    public Collection<BookDetail> getSaleBooks(String email) {
        ArrayList<BookDetail> books = new ArrayList<>();
        Collection<BookDetail> allBooks = getAllBooks();
        for (BookDetail book: allBooks){
            if (book.seller.equals(email))
                books.add(book);
        }
        return books;
    }

    @Override
    public Collection<BookDetail> getBooksOnMarket() {
        ArrayList<BookDetail> books = new ArrayList<>();
        Collection<BookDetail> allBooks = getAllBooks();
        for (BookDetail book: allBooks){
            if (book.status.equals("onMarket"))
                books.add(book);
        }
        return books;
    }


    @Override
    public void removeBook(String id) {
        books.remove(id);
        saveBooks();
    }

    public void removeAllBooks(){
        books.clear();
        saveBooks();
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Collection<User> getUsersWithoutAdmin() {
        ArrayList<User> users = new ArrayList<>();
        Collection<User> allUsers = getAllUsers();
        for (User user: allUsers){
            if (!user.email.equals("admin@iit.edu"))
                users.add(user);
        }
        return users;
    }

    @Override
    public User getUser(String email) {
        return users.get(email);
    }

    @Override
    public String getUserNameByEmail(String email) {
        String username = "";
        Collection<User> allUsers = getAllUsers();
        for (User user: allUsers){
            if (user.email.equals(email)){
                username = user.username;
                break;
            }
        }
        return username;
    }

    @Override
    public void insertUser(User user) {
        users.put(user.email, user);
        saveUsers();
    }

    @Override
    public void removeUser(String email) {
        users.remove(email);
        saveUsers();
    }

    public void removeAllUsers(){
        users.clear();
        saveUsers();
    }

    @Override
    public boolean checkUser(String email) {
        return false;
    }

    @Override
    public Collection<Message> getAllMessages() {
        return messages.values();
    }

    @Override
    public Message getMessage(String id) {
        return messages.get(id);
    }

    @Override
    public Collection<Message> getMessages(String email){
        ArrayList<Message> msgs = new ArrayList<>();
        Collection<Message> allMessages = getAllMessages();
        for (Message msg: allMessages){
            if (msg.receiver.equals(email))
                msgs.add(msg);
            Log.d("getMessages", msg.bookId + " " + msg.sender + " " + msg.receiver);
        }
        return msgs;
    }

    @Override
    public void insertMessage(Message message) {
        messages.put(message.id, message);
        saveMessages();
    }

    @Override
    public void removeMessage(String id) {
        messages.remove(id);
        saveMessages();
    }

    @Override
    public void removeMessageByBookID(String id) {
        ArrayList<Message> msgs = new ArrayList<>();
        Collection<Message> allMessages = getAllMessages();
        for (Message msg: allMessages){
            if (msg.bookId.equals(id))
                msgs.remove(msg);
        }
    }

    @Override
    public void removeAllMessage() {
        messages.clear();
        saveMessages();
    }

    @Override
    public String update() {
        return "";
    }

    private void saveBooks() {
        try {
            // convert array to string
            String bookJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(books);

            Log.d("Save Books", bookJson);

            SharedPreferences hp = activity.getSharedPreferences(modelPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = hp.edit();
            editor.putString(booksKey, bookJson);
            editor.commit();
        } catch (Exception e) {

        }
    }

    private TreeMap<String,BookDetail> loadBooks() {
        try {
            SharedPreferences hp = activity.getSharedPreferences(modelPref, Context.MODE_PRIVATE);
            String bookJson = hp.getString(booksKey, "{}");
            return mapper.readValue(bookJson, new TypeReference<TreeMap<String,BookDetail>>() {
            });
        } catch (Exception e) {
            return new TreeMap<>();
        }
    }



    private void saveUsers() {
        try {
            // convert array to string
            String userJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(users);

            Log.d("Save User", userJson);

            SharedPreferences hp = activity.getSharedPreferences(modelPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = hp.edit();
            editor.putString(usersKey, userJson);
            editor.commit();
        } catch (Exception e) {

        }
    }

    private TreeMap<String,User> loadUsers() {
        try {
            SharedPreferences hp = activity.getSharedPreferences(modelPref, Context.MODE_PRIVATE);
            String userJson = hp.getString(usersKey, "{}");
            //Log.d("History Load", historyJson);
            return mapper.readValue(userJson, new TypeReference<TreeMap<String,User>>() {
            });
        } catch (Exception e) {
            return new TreeMap<>();
        }
    }

    private void saveMessages() {
        try {
            // convert array to string
            String messageJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messages);

            Log.d("Save Message", messageJson);

            SharedPreferences hp = activity.getSharedPreferences(modelPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = hp.edit();
            editor.putString(messagesKey, messageJson);
            editor.commit();
        } catch (Exception e) {

        }
    }

    private TreeMap<String,Message> loadMessages() {
        try {
            SharedPreferences hp = activity.getSharedPreferences(modelPref, Context.MODE_PRIVATE);
            String messageJson = hp.getString(messagesKey, "{}");
            Log.d("msg Load", messageJson);
            return mapper.readValue(messageJson, new TypeReference<TreeMap<String,Message>>() {
            });
        } catch (Exception e) {
            Log.e("msg Load", e.toString(), e);
            return new TreeMap<>();
        }
    }

    private final Activity activity;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String modelPref = "booktogo_model";

    private final TreeMap<String, BookDetail> books;
    private static final String booksKey = "books";

    private final TreeMap<String, User> users;
    private static final String usersKey = "users";

    private final TreeMap<String, Message> messages;
    private static final String messagesKey = "messages";
}

