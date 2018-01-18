package com.jishin.ankiji.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jishin.ankiji.R;
import com.jishin.ankiji.signin.LoginFacebook;
import com.jishin.ankiji.signin.LoginGoogle;
import com.jishin.ankiji.utilities.DatabaseService;

public class SigninActivity extends AppCompatActivity {

    
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private ImageView imgFacebook;
    private ImageView imgGoogle;
    private TextView txtCreateAcount;
    private TextView txtForgotPass;
    private LoginGoogle loginGoogle;
    private LoginFacebook loginFacebook;
    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private DatabaseService mData = DatabaseService.getInstance();
    
    public static final String TAG = SigninActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        FacebookSdk.sdkInitialize(getApplicationContext());

        getControls();
        setEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mData.isSignIn()){
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getControls() {
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        imgFacebook = (ImageView) findViewById(R.id.img_facebook);
        imgGoogle = (ImageView) findViewById(R.id.img_google);
        txtCreateAcount = (TextView) findViewById(R.id.tv_create_an_account);
        txtForgotPass = (TextView) findViewById(R.id.tv_forgot_password);
        //login google
        loginGoogle = new LoginGoogle(getString(R.string.default_web_client_id), this);
        //login facebook
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginFacebook = new LoginFacebook(loginManager, callbackManager, this);

        mAuth = mData.getFirebaseAuth();
    }


    private void setEvents() {
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                hideKeyboard(view);
                requestSignIn(username, password);
                btnLogin.setEnabled(false);
                btnLogin.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorDisable));
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
        
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFacebook();
            }
        });
        
        
        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = loginGoogle.getmGoogleSignInClient().getSignInIntent();
                startActivityForResult(signInIntent, LoginGoogle.RC_SIGN_IN);
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

    private void loginFacebook(){
        loginFacebook.loginFacebook();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == LoginGoogle.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                LoginGoogle.firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void requestSignIn(String username, String pass){
        mAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SigninActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SigninActivity.this, MainActivity.class));
                }
//                else {
//                    Log.d("EmailPassword", "signInWithEmail:failure", task.getException());
//                    Toast.makeText(SigninActivity.this, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show();
//                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SigninActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
                btnLogin.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
