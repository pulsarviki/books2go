package com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class MongoModel implements Model {

    public MongoModel(String uri) {
        this.db = new MongoClient(new MongoClientURI(uri)).getDatabase(db_name);

        updateBooks();
        updateUsers();
        updateMessages();

        Log.d("load mongo", String.format(
                "books %d, users %d, msgs %d",
                books.get().size(), users.get().size(), messages.get().size()));
    }

    @Override
    public Collection<BookDetail> getAllBooks() {
        return books.get().values();
    }

    @Override
    public BookDetail getBook(String id) {
        return books.get().get(id);
    }

    @Override
    public void insertBook(BookDetail book) {
        try {
            HashMap<String, Object> map = mapper.readValue(
                    mapper.writeValueAsString(book),
                    new TypeReference<HashMap<String, Object>>() {
                    });
            map.put("_id", book.id);
            if (books.get().containsKey(book.id))
                db.getCollection(booksKey).replaceOne(Filters.eq("_id", book.id), new Document(map));
            else
                db.getCollection(booksKey).insertOne(new Document(map));
            books.get().put(book.id, book);
        }
        catch (Exception e) {
            Log.e("book insert", e.toString(), e);
        }
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
        db.getCollection(booksKey).deleteOne(new Document("_id", id));
        books.get().remove(id);
    }

    public void removeAllBooks(){
        for (BookDetail book: new ArrayList<>(books.get().values()))
            removeBook(book.id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.get().values();
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
        return users.get().get(email);
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
        try {
            HashMap<String, Object> map = mapper.readValue(
                    mapper.writeValueAsString(user),
                    new TypeReference<HashMap<String, Object>>() {
                    });
            map.put("_id", user.email);
            if (users.get().containsKey(user.email))
                db.getCollection(usersKey).replaceOne(Filters.eq("_id", user.email), new Document(map));
            else
                db.getCollection(usersKey).insertOne(new Document(map));
            users.get().put(user.email, user);
        }
        catch (Exception e) {
            Log.e("user insert", e.toString(), e);
        }
    }

    @Override
    public void removeUser(String email) {
        db.getCollection(usersKey).deleteOne(new Document("_id", email));
        users.get().remove(email);
    }

    public void removeAllUsers(){
        for (User user: new ArrayList<>(users.get().values()))
            removeUser(user.email);
    }

    @Override
    public boolean checkUser(String email) {
        Collection<User> allUsers = getAllUsers();
        for (User user: allUsers){
            if (user.email.equals(email))
               return true;
        }
        return false;
    }

    @Override
    public Collection<Message> getAllMessages() {
        return messages.get().values();
    }

    @Override
    public Message getMessage(String id) {
        return messages.get().get(id);
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
        try {
            HashMap<String, Object> map = mapper.readValue(
                    mapper.writeValueAsString(message),
                    new TypeReference<HashMap<String, Object>>() {
                    });
            map.put("_id", message.id);
            if (messages.get().containsKey(message.id))
                db.getCollection(messagesKey).replaceOne(Filters.eq("_id", message.id), new Document(map));
            else
                db.getCollection(messagesKey).insertOne(new Document(map));
            messages.get().put(message.id, message);
        }
        catch (Exception e) {
            Log.e("msg insert", e.toString(), e);
        }
    }

    @Override
    public void removeMessage(String id) {
        db.getCollection(messagesKey).deleteOne(new Document("_id", id));
        messages.get().remove(id);
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
        for (Message message: new ArrayList<>(messages.get().values()))
            removeMessage(message.id);
    }

    @Override
    public String update() {
        int currentBooks = books.get().size();
        updateBooks();
        updateMessages();
        int newBooks = books.get().size();
        if (currentBooks < newBooks)
            return "A new book is on the market";
        return "";
    }

    private void updateUsers() {
        try {
            TreeMap<String, User> t = new TreeMap<>();
            for (Document document: db.getCollection(usersKey).find()) {
                User user = mapper.readValue(document.toJson(), User.class);
                t.put(user.email, user);
            }
            this.users.set(t);
        } catch (Exception e) {
            Log.e("users update", e.toString(), e);
        }
    }

    private void updateMessages() {
        try {
            TreeMap<String, Message> t = new TreeMap<>();
            for (Document document: db.getCollection(messagesKey).find()) {
                Message msg = mapper.readValue(document.toJson(), Message.class);
                t.put(msg.id, msg);
            }
            this.messages.set(t);
        } catch (Exception e) {
            Log.e("msgs update", e.toString(), e);
        }
    }

    private void updateBooks() {
        try {
            TreeMap<String, BookDetail> t = new TreeMap<>();
            for (Document document: db.getCollection(booksKey).find()) {
                BookDetail book = mapper.readValue(document.toJson(), BookDetail.class);
                t.put(book.id, book);
            }
            this.books.set(t);
        } catch (Exception e) {
            Log.e("books update", e.toString(), e);
        }
    }

    private static final String db_name = "NewBookToGo";
    private static final ObjectMapper mapper = new ObjectMapper();

    private final MongoDatabase db;

    private final AtomicReference<TreeMap<String, BookDetail>> books = new AtomicReference<>();
    private static final String booksKey = "books";

    private final AtomicReference<TreeMap<String, User>> users = new AtomicReference<>();
    private static final String usersKey = "users";

    private final AtomicReference<TreeMap<String, Message>> messages = new AtomicReference<>();
    private static final String messagesKey = "messages";
}
