package com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data;


import java.util.Collection;

public interface Model {

        public Collection<BookDetail> getAllBooks();
        public BookDetail getBook(String id);
        public void insertBook(BookDetail book);
        public Collection<BookDetail>getPurchasedBooks(String email);
        public Collection<BookDetail>getSaleBooks(String email);
        public Collection<BookDetail>getBooksOnMarket();
        public void removeBook(String id);
        public void removeAllBooks();


        public Collection<User> getAllUsers();
        public Collection<User> getUsersWithoutAdmin();
        public User getUser(String email);
        public String getUserNameByEmail(String email);
        public void insertUser(User user);
        public void removeUser(String email);
        public void removeAllUsers();
        public boolean checkUser(String email);

        public Collection<Message> getAllMessages();
        public Collection<Message> getMessages(String email);
        public Message getMessage(String id);
        public void insertMessage(Message message);
        public void removeMessage(String id);
        public void removeMessageByBookID(String bookId);
        public void removeAllMessage();

        public String update();

}
