package com.example.denvervolunteerconnect;

import android.content.Intent;
import android.os.Bundle;

import com.example.denvervolunteerconnect.ViewModels.UserDataViewModel;
import com.example.denvervolunteerconnect.googleauth.GoogleAuthClient;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.denvervolunteerconnect.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.Menu;
import android.view.MenuItem;

import utils.Constants;

@MainThread
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private UserDataViewModel mUserDataViewModel = null;
    private AppBarConfiguration appBarConfiguration = null;
    private ActivityMainBinding mMainActivityBinding = null;
    private GoogleAuthClient mGoogleAuthClient = null;
    private FirebaseDatabase mFirebaseDatabase = null;
    private static final int RC_SIGN_IN = 9001;

    //FireBase member variables
    private FirebaseAnalytics mAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        mAnalytics = FirebaseAnalytics.getInstance(this);
        mGoogleAuthClient = GoogleAuthClient.getInstance(getApplicationContext());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mUserDataViewModel =  mUserDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        setContentView(mMainActivityBinding.getRoot());
        setSupportActionBar(mMainActivityBinding.toolbar);

        // TESTING LOGIC
        DatabaseReference myRef = mFirebaseDatabase.getReference("message");

        myRef.setValue("Hello, World!");
        // END TESTING LOGIC

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        GoogleAuthClient.signOut(); // TODO: Remove testing logic.

        // Check if user is signed in else, requires a Google Login to processed.
        FirebaseUser currentUser = mGoogleAuthClient.getSignedInFirebaseUser();
        if (currentUser == null) {
            startActivityForResult(mGoogleAuthClient.getSignInIntent(), RC_SIGN_IN);
        } else {
            Log.v(TAG, String.valueOf("Current User: " + currentUser));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        GoogleAuthClient.cleanUp();
        mGoogleAuthClient = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.Integers.RESULT_SUCCESS
                == mGoogleAuthClient.handleLogInResult(requestCode, resultCode, data))
        {
            Log.v(TAG, "Logged in Success");
            // TODO: Update the database with the new UserData.
        } else {
            Log.w(TAG, "Closing application due to failure to login");
            //TODO: add logic to notify the user of the failure, and allow them to retry.
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}