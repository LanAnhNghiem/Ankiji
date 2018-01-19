package com.jishin.ankiji;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ImageView profilePictureView;
    LoginButton loginButton;
    Button logoutButton;
    TextView txtName, txtEmail;
    CallbackManager mCallbackManager;
    String email, name;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FirebaseApp.initializeApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        createParameters();
        logoutButton.setVisibility(View.INVISIBLE);
        loginButton.setReadPermissions("email", "public_profile");
        setLoginButton();
        Log.d(TAG, "onCreate");
        setLogoutButton();
    }

    @Override
    protected void onStart() {
        //LoginManager.getInstance();
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "onStart:currentUser");
            loginButton.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            updateUI(currentUser);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    private void setLoginButton() {
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Success", "Login success");
                handleFacebookAccessToken(loginResult.getAccessToken());
                loginButton.setVisibility(View.INVISIBLE);
                profilePictureView.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.VISIBLE);
                txtEmail.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {

                Log.d(TAG, "facebook:onError", error);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in " + user.getUid());
                    //updateUI(user);
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
    }

    public void setLogoutButton() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                logoutButton.setVisibility(View.INVISIBLE);
                profilePictureView.setVisibility(View.INVISIBLE);
                txtName.setText(null);
                txtEmail.setText(null);
                txtName.setVisibility(View.INVISIBLE);
                txtEmail.setVisibility(View.INVISIBLE);
                profilePictureView.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public void createParameters() {
        profilePictureView = findViewById(R.id.userProfilePicture);
        loginButton = findViewById(R.id.btn_LoginFB);
        logoutButton = findViewById(R.id.btn_LogoutFB);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            Uri uri = user.getPhotoUrl();
            Picasso.with(MainActivity.this).load(uri).into(profilePictureView);
            txtName.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
