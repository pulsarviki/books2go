package com.cs442.dliu33.booktogo;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.BookDetail;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.Model;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.MongoModel;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.SharedPreferencesModel;
import com.cs442.dliu33.booktogo.com.cs442.dliu33.booktogo.data.User;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements HomePageFragment.OnHomePageListener, LoginFragment.OnLoginListener,
        RegisterFragment.OnLoginListener, UserProfileFragment.OnProfileListener, PostBookFragment.OnPostListener,
        BookDetailsFragment.OnBookDetailsListener, MessageListFragment.OnMessageListener, BookListFragment.OnBookListener,
        MessageListAdapter.OnMessageAdapterListener,BookListAdapter.OnBookItemListener,
        PurchaseHistoryFragment.OnPurchaseHistoryListener,SaleHistoryFragment.OnSaleHistoryListener,
        SaleHistoryAdapter.OnSaleHistoryAdapterListener, AdminProfileFragment.OnAdminProfileListener,
        UserManagementFragment.OnUserManagementListener, MessageDetailsFragment.OnMessageDetailsListener{

    static Model model;

    static String selected = "";

    static String msgSelected = "";

    static String saleSelected= "";

    static User currentUser = new User("","","", "");

    static User lastUser = new User("","","","");

    private  static final int RESULT_LOAD_IMAGE = 1;
    private  static final int RESULT_LOAD_PIC = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // model = new SharedPreferencesModel(this);
        model = new MongoModel("mongodb://group42:UpAf1A1zWK7K@45.76.21.164");

        Intent intent = new Intent(this, AppService.class);
        startService(intent);

        setContentView(R.layout.activity_main);

        // Create an instance of ExampleFragment
        LoginFragment firstFragment = new LoginFragment();

        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();
    }

    @Override
    public void userRegister(User user) {
        if (model.getUserNameByEmail(user.email).equals("")) {
            model.insertUser(user);
            // see new user's info
            User newUser = model.getUser(user.email);
            Log.d("newUser", newUser.username + " " + newUser.email + " " + newUser.password);

            // Create fragment and give it an argument for the selected item
            LoginFragment newFragment = new LoginFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
        else
            Toast.makeText(this, "User have already existed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void linkToLogin() {
        // Create fragment and give it an argument for the selected item
        LoginFragment newFragment = new LoginFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void userLogin(String email, String password) {

        ArrayList<User> allUsers = new ArrayList<>(this.model.getAllUsers());
        if (allUsers.isEmpty()) {
            Log.d("users", "no user");
        }
        else {
            for (User each : allUsers) {
                Log.d("users", each.username + " " + each.email + " " + each.password);
            }
        }

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "please fill out your information", Toast.LENGTH_LONG).show();
        }
        else{
            for(User user: allUsers){
                Log.d("user", user.email + "?" + email);
                Log.d("user", user.password + "?" + password);
                if (user.email.equals(email)) {
                    if (user.password.equals(password)) {
                        if(!user.email.equals("admin@iit.edu")) {
                            currentUser = model.getUser(email);
                            lastUser = model.getUser(email);
                            linkToProfile();
                            return;
                        }
                        else{
                            currentUser = model.getUser(email);
                            lastUser = model.getUser(email);
                            linkToAdmin();
                            return;
                        }

                    } else {
                        Toast.makeText(this, "Password is not correct", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Toast.makeText(this, "User is not exist, please register", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void linkToRegister() {

        // Create fragment and give it an argument for the selected item
        RegisterFragment newFragment = new RegisterFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    public void linkToFeed() {
        // Create fragment and give it an argument for the selected item
        BookListFragment newFragment = new BookListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void linkToPost() {
        // Create fragment and give it an argument for the selected item
        PostBookFragment newFragment = new PostBookFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void linkToHistory() {
        ArrayList<BookDetail> purchaseBooks = new ArrayList<>(this.model.getPurchasedBooks(this.currentUser.email));
        if (purchaseBooks.isEmpty()) {
            Log.d("pBooks", "no purchased book");
        }
        else {
            for (BookDetail each : purchaseBooks) {
                Log.d("pBooks", each.bookName + " " + each.buyer + " " + each.price);
            }
        }
        // Create fragment and give it an argument for the selected item
        PurchaseHistoryFragment newFragment = new PurchaseHistoryFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    @Override
    public void linkToAdmin() {
        // Create fragment and give it an argument for the selected item
        AdminProfileFragment newFragment = new  AdminProfileFragment ();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    @Override
    public void linkToUser() {
        // Create fragment and give it an argument for the selected item
        UserManagementFragment newFragment = new UserManagementFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void linkToBook() {
// Create fragment and give it an argument for the selected item
        BookListFragment newFragment = new BookListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void backToLogin() {

        // Create fragment and give it an argument for the selected item
        LoginFragment newFragment = new LoginFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void setUserPic() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,RESULT_LOAD_PIC);
    }

    @Override
    public void linkToSale() {
        // Create fragment and give it an argument for the selected item
        SaleHistoryFragment newFragment = new SaleHistoryFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void submitBook() {

        // Create fragment and give it an argument for the selected item
        BookListFragment newFragment = new BookListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void linkToProfile() {

        // Create fragment and give it an argument for the selected item
        UserProfileFragment newFragment = new UserProfileFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void addBookImg() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImg = data.getData();
            PostBookFragment PostBookFrag = (PostBookFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            PostBookFrag.setBookImg(selectedImg);
            return;
        }

        if (requestCode == RESULT_LOAD_PIC && resultCode == RESULT_OK && data != null){
            Uri selectedPic = data.getData();
            UserProfileFragment UserProfileFrag = (UserProfileFragment)
            getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            UserProfileFrag.setUserPic(selectedPic);
            return;
        }
    }

    @Override
    public void onShowDetails(String id) {
        selected = id;
        // Create fragment and give it an argument for the selected item
        BookDetailsFragment newFragment = new BookDetailsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onShowMsgDetails(String id) {
        msgSelected = id;
        // Create fragment and give it an argument for the selected item
        MessageDetailsFragment newFragment = new MessageDetailsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void linkToMessage() {
        /*ArrayList<Message> allMsgs = new ArrayList<>(this.model.getAllMessages());
        if (allMsgs.isEmpty()) {
            Log.d("msg", "no message");
        }
        else {
            for (Message each : allMsgs) {
                Log.d("msg", each.bookId + " " + each.sender + " " + each.receiver);
            }
        }*/
        // Create fragment and give it an argument for the selected item
        MessageListFragment newFragment = new MessageListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onShowSaleDetails(String id) {
        saleSelected = id;
        // Create fragment and give it an argument for the selected item
        SaleDetailsFragment newFragment = new SaleDetailsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}

