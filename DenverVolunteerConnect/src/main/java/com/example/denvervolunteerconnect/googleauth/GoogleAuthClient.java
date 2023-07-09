package com.example.denvervolunteerconnect.googleauth;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;

import androidx.annotation.AnyThread;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;

import com.example.denvervolunteerconnect.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@AnyThread
public class GoogleAuthClient {
    private ExecutorService authExecutorService = Executors.newSingleThreadExecutor();
    private Future<?> authFuture = null;
    private Context context;
    private SignInClient signInClient;
    private FirebaseAuth auth;

    public MutableLiveData<UserData> userdata;


    public GoogleAuthClient(Context context, SignInClient signInClient) {
        this.context = context;
        this.signInClient = signInClient;
    }

    public void signIn() {
        try {
            authFuture = authExecutorService.submit(() -> {
                Task<BeginSignInResult> intentSender = signInClient.beginSignIn(buildSignInRequest());
                // TODO: Finish Sign in and launch MainActivity with intent.
            });
        } catch (Exception e) { //TODO: make catch more specific
            e.printStackTrace();
        }
    }

    public void signOut() {
        try {
            authFuture = authExecutorService.submit(() -> {
                signInClient.signOut();
                auth.signOut();
            });
        } catch (Exception e) {//TODO: make catch more specific

        }
    }

    @Nullable
    public UserData getSignedInUser() {
        try {
            // TODO: Create call back or live data to return this data to caller.
            authFuture = authExecutorService.submit(() -> {
                UserData result = null;
                FirebaseUser user = auth.getCurrentUser();

                if (user != null && user.getDisplayName() != null && user.getPhotoUrl() != null) {
                    result = new UserData(
                            user.getUid(),
                            user.getDisplayName(),
                            user.getPhotoUrl().toString()
                    );
                }
            });
        } catch (Exception e) { //TODO: make catch more specific
            e.printStackTrace();
        }

        return result;
    }

    @WorkerThread
    private SignInResult signInWithIntent(Intent intent) {
        SignInResult result = new SignInResult(null, "Default Failure");
        try {
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(intent);
            String googleIdToken = credential.getGoogleIdToken();
            AuthCredential googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null);

            FirebaseUser user = auth.signInWithCredential(googleCredential).getResult().getUser();

            if (user != null && user.getUid() != null && user.getDisplayName() != null && user.getPhotoUrl() != null) {
                result = new SignInResult(new UserData(
                        user.getUid(),
                        user.getDisplayName(),
                        user.getPhotoUrl().toString()
                ), null);
            }
        } catch (Exception e) {//TODO: make catch more specific
            e.printStackTrace();
            result = new SignInResult(null, e.getMessage());
        }
        return result;
    }


    @WorkerThread
    private BeginSignInRequest buildSignInRequest() {
        return BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(context.getString(R.string.default_web_client_id))
                        .build())
                .setAutoSelectEnabled(true)
                .build();
    }
}
