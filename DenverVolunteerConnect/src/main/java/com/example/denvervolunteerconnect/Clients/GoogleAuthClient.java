package com.example.denvervolunteerconnect.Clients;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.denvervolunteerconnect.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.Constants;

@MainThread
public class GoogleAuthClient {
    private static final String TAG = GoogleAuthClient.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private static volatile GoogleAuthClient INSTANCE = null;
    private static ExecutorService authExecutorService = Executors.newSingleThreadExecutor();
    private static volatile FirebaseAuth mFirebaseAuth;
    private static volatile GoogleSignInClient mGoogleSignInClient;
    private Context context;


    public GoogleAuthClient(Context context) {
        this.context = context;
        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
    }

    @Nullable
    public static FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    @NonNull
    public static GoogleAuthClient getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GoogleAuthClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GoogleAuthClient(context);
                }
            }
        }
        return INSTANCE;
    }

    public static void cleanUp() {
        INSTANCE = null;
    }

    public void signOut() {
        try {
            authExecutorService.execute(() -> {
                mGoogleSignInClient.signOut();
                mFirebaseAuth.signOut();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    @Nullable
    public FirebaseUser getSignedInFirebaseUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public int handleLogInResult(int requestCode, int resultCode, Intent data) {
        int result = Constants.Integers.RESULT_FAILED;
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        switch (requestCode) {
            // This is a switch because other cases could be added for other login types.
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                    result = Constants.Integers.RESULT_SUCCESS;
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                }
                break;
            default:
                Log.e(TAG, "Invalid requestCode when attempting to login");
        }
        return result;
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(authExecutorService, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            postUserToDatabase(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            postUserToDatabase(null);
                        }
                    }
                });
    }

    private void postUserToDatabase(FirebaseUser firebaseUser) {
        FirebaseDatabaseClient.getInstance().postUserData(firebaseUser);
    }


}
