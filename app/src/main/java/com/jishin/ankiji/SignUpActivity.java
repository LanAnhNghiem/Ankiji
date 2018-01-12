package com.jishin.ankiji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.model.User;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    public static String TAG = SignUpActivity.class.getSimpleName();

    CircleImageView mImgAvatar;
    TextInputEditText mUsername, mEmail, mPassword, mConfirmPass;
    Button mBtnSignUp;
    TextView txtUsername, txtEmail, txtPass, txtConfirmPass;
    ProgressBar progressBar;
    DatabaseService mDatabase = DatabaseService.getInstance();
    DatabaseReference mUserRef = mDatabase.createDatabase(Utilities.USER_REFERENCE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);
        mImgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        mUsername = (TextInputEditText) findViewById(R.id.tietUsername);
        mEmail = (TextInputEditText) findViewById(R.id.tietEmail);
        mPassword = (TextInputEditText) findViewById(R.id.tietPassword);
        mConfirmPass = (TextInputEditText) findViewById(R.id.tietConfirmPass);

        mBtnSignUp = (Button) findViewById(R.id.btnSignUp);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPass = (TextView) findViewById(R.id.txtPassword);
        txtConfirmPass = (TextView) findViewById(R.id.txtConfirmPass);
        textChangeListener();
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim(),
                        email = mEmail.getText().toString().trim(),
                        password = mPassword.getText().toString().trim(),
                        confirmPass = mConfirmPass.getText().toString().trim();
                //registerAccount(email, password, username, "");
                if(isValidUsername(username) && isValidEmail(email)
                        && isValidPass(password)
                        && isPassMatching(password,confirmPass))
                {
                    Log.d(TAG, "register");
                    hideKeyboard(view);
                    mBtnSignUp.setEnabled(false);
                    mBtnSignUp.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.colorDisable));
                    registerAccount(email, password, username, "");
                }
                if(!isConnected()){
                    Toast.makeText(SignUpActivity.this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "fail1");
                }
            }
        });
    }
    private void textChangeListener(){
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!isValidUsername(editable.toString())){
                    txtUsername.setText(R.string.invalid_username);
                    Log.d(TAG, "fail2");
                }
                else
                    txtUsername.setText("");
            }
        });
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!isValidEmail(editable.toString())){
                    txtEmail.setText(R.string.invalid_email);
                    Log.d(TAG, "fail3");
                }else{
                    txtEmail.setText("");
                }
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!isValidPass(editable.toString())){
                    txtPass.setText(R.string.invalid_password);
                    Log.d(TAG, "fail4");
                }else{
                    txtPass.setText("");
                }
            }
        });
        mConfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!isPassMatching(mPassword.getEditableText().toString(),editable.toString())){
                    txtConfirmPass.setText(R.string.pass_not_matching);
                    Log.d(TAG, "fail5");
                }else{
                    txtConfirmPass.setText("");
                }
            }
        });
    }
    private boolean isValidUsername(String username){
        if(username.length() >= 6 && username.length() <= 20){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private boolean isValidPass(String pass){
        if(pass.length() <6){
            return false;
        }
        return true;
    }
    private boolean isPassMatching(String pass, String confirm){
        if(pass.equals(confirm))
            return true;
        return false;
    }

    private void registerAccount(final String email, final String password, final String username, final String linkPhoto){
        FirebaseAuth mAuth = mDatabase.getAuth();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(task.getResult().getUser().getUid(), username, email, password, linkPhoto);
                    //setProgressBar();
                    addNewUserOnFirebase(user);
                }
                else{
                    Toast.makeText(SignUpActivity.this, R.string.sign_up_failed, Toast.LENGTH_SHORT).show();
                    mBtnSignUp.setEnabled(true);
                    mBtnSignUp.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimaryDark));
                }
            }
        });
    }

    private void addNewUserOnFirebase(final User user){
        final DatabaseReference mRef = mUserRef.child(user.getId());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    mRef.setValue(user);
                    Toast.makeText(SignUpActivity.this, R.string.sign_up_successful, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SignUpActivity.this, R.string.sign_up_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
//    private void setProgressBar(){
//        progressBar.setVisibility(View.VISIBLE);
//        ObjectAnimator anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
//        anim.setDuration(15000);
//        anim.setInterpolator(new DecelerateInterpolator());
//        anim.start();
//    }
}
