package com.example.denvervolunteerconnect.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.denvervolunteerconnect.R;
import com.example.denvervolunteerconnect.ViewModels.MainActivityViewModel;
import com.example.denvervolunteerconnect.Clients.FirebaseDatabaseClient;
import com.example.denvervolunteerconnect.Clients.GoogleAuthClient;

import androidx.annotation.MainThread;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.denvervolunteerconnect.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import utils.Constants;

@MainThread
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainActivityViewModel mMainActivityViewModel = null;
    private AppBarConfiguration appBarConfiguration = null;
    private ActivityMainBinding mMainActivityBinding = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    private GoogleAuthClient mGoogleAuthClient = null;
    private FirebaseDatabaseClient mFirebaseDatabaseClient = null;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        // Not all of the member object are used in MainActivity,
        // but having them in this class will prevent them from being garbage collected
        // while the application is running, and if future proof MainActivity.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mGoogleAuthClient = GoogleAuthClient.getInstance(getApplicationContext());
        mFirebaseDatabaseClient = FirebaseDatabaseClient.getInstance();
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mMainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainActivityBinding.getRoot());
        setSupportActionBar(mMainActivityBinding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        authorizeUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
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
        FirebaseDatabaseClient.cleanUp();
        mFirebaseDatabaseClient = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        int loginResult = mGoogleAuthClient.handleLogInResult(requestCode, resultCode, data);
        switch (loginResult) {
            case Constants.Integers.RESULT_SUCCESS:
                Log.v(TAG, "Logged in Success, posting user data to Firebase.");
                break;
            case Constants.Integers.RESULT_FAILED:
                Log.w(TAG, "Closing application due to failure to login");
                Toast.makeText(this, "Login is required to use Denver Volunteer Connect", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case Constants.Integers.RESULT_NETWORK_ERROR:
                Log.w(TAG, "Closing application due to lack of internet connection.");
                Toast.makeText(this, "Sorry, Denver Volunteer Connect required a Internet Connection.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Log.e(TAG, "Closing application, due to invalid state.");
                Toast.makeText(this, "An unexpected error happened, sorry. Try again later.", Toast.LENGTH_SHORT).show();
                mFirebaseAnalytics.logEvent("invalid_login_state", null);
                finish();
                break;
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
        int id = item.getItemId();

        if (id == R.id.logout_action) {
            mGoogleAuthClient.signOut();
            Toast.makeText(this, "Successfully Logged out.", Toast.LENGTH_LONG).show();
            authorizeUser();
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

    private void authorizeUser() {
        startActivityForResult(mGoogleAuthClient.getSignInIntent(), RC_SIGN_IN);
        mFirebaseDatabaseClient.startVolunteerRequestListUpdate();
        mMainActivityViewModel.startUserDataObserver(this);
        mMainActivityViewModel.startRequestListObserver(this);
    }
}