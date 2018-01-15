package com.jishin.ankiji.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jishin.ankiji.R;

public class SigninActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGNIN_REQUEST = 1;
    static final int FACEBOOK_SIGNIN_REQUEST = 1;
    public static final String TAG = SigninActivity.class.getSimpleName();

    private Button btnLogin;
    private ImageView imgFacebook;
    private ImageView imgGoogle;
    private TextView txtCreateAcount;
    private TextView txtForgotPass;
    CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        FirebaseApp.initializeApp(this);
        getControls();
        setEvents();
    }

    private void getControls() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        imgFacebook = (ImageView) findViewById(R.id.img_facebook);
        imgGoogle = (ImageView) findViewById(R.id.img_google);
        txtCreateAcount = (TextView) findViewById(R.id.tv_create_an_account);
        txtForgotPass = (TextView) findViewById(R.id.tv_forgot_password);
        mCallbackManager = CallbackManager.Factory.create();
    }


    private void setEvents() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);

                startActivityForResult(intent, FACEBOOK_SIGNIN_REQUEST);
            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, FacebookSigninActivity.class);
                startActivity(intent);
            }
        });

        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GoogleLogin();
            }
        });

        txtCreateAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SigninActivity.this, "Forgotpass.TRUNG", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (resultCode == Activity.RESULT_OK && requestCode == GOOGLE_SIGNIN_REQUEST) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed" + e.getStatusCode());
//                Toast.makeText(SigninActivity.this, "Google sign in failed", Toast.LENGTH_LONG).show();
//                // ...
//            }
//        }
    }

    @Override
    protected void onStart() {
        //LoginManager.getInstance();
        super.onStart();
//        if (mGoogleApiClient != null)
//            mGoogleApiClient.connect();
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
        if (mAuth != null) {
            mAuth.addAuthStateListener(mAuthListener);

            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Log.d(TAG, "onStart:currentUser");
                //TODO: Change activity
                Toast.makeText(SigninActivity.this, "currentUser", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.stopAutoManage(this);
//            mGoogleApiClient.disconnect();
//        }
//    }

//    private void GoogleLogin() {
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Toast.makeText(SigninActivity.this, "You got an Error", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, GOOGLE_SIGNIN_REQUEST);
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //TODO: updateUI
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(SigninActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //TODO: updateUI
//
//                        }
//
//                        // ...
//                    }
//                });
//    }

}
