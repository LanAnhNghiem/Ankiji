package com.jishin.ankiji.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.jishin.ankiji.R;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.LocalDatabase;

import java.util.Map;

public class ChangeUsernameActivity extends AppCompatActivity {

    private EditText txtUsername;
    private TextView txtError;
    private Button btnSave;
    private Button btnCancel;
    private String userUid;
    private String currentName;
    private LocalDatabase mLocalData = LocalDatabase.getInstance();
    private DatabaseService mData = DatabaseService.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("currentUid")){
                userUid = intent.getExtras().getString("currentUid");
                currentName = intent.getExtras().getString("currentUsername");
                Log.d("currentUid", userUid);
                Log.d("currentName", currentName);
            }
        }
        mLocalData.init(this, userUid, mData);
        setControls();
        setEvents();
    }

    private void setEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                String newUsername = txtUsername.getText().toString().trim();
                if (TextUtils.isEmpty(txtError.getText())){
                    updateUserDislayName(newUsername);
                }
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void updateUserDislayName(String dislayName) {
        Log.d("DATABASE", FirebaseDatabase.getInstance().getReference("User").child(userUid).child("username").toString());
        FirebaseDatabase.getInstance().getReference("User").child(userUid).child("username")
                .setValue(dislayName);
        saveUsername(dislayName);
    }

    private void setControls() {
        txtUsername = findViewById(R.id.edt_username);
        txtError = findViewById(R.id.txtUsername_err);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        txtUsername.setText(currentName);
        textChangeListener();
//        txtUsername.setSelection(currentName.length());
        txtUsername.requestFocus();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtUsername, InputMethodManager.SHOW_IMPLICIT);

    }

    private void textChangeListener() {
        txtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidUsername(editable.toString())) {
                    txtError.setText(R.string.invalid_username);
                } else{
                    txtError.setText("");
                }

            }
        });
    }

    private boolean isValidUsername(String username) {
        if (username.length() >= 6 && username.length() <= 20) {
            return true;
        } else {
            return false;
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //save username to local data
    private void saveUsername(String name){
        Map myMap = mLocalData.readAllData();
        Map userMap = mLocalData.readData(Constants.USER_NODE);
        userMap.put("username", name);
        myMap.put(Constants.USER_NODE, userMap);
        String str = new Gson().toJson(myMap);
        mLocalData.writeToFile(Constants.DATA_FILE+userUid, str, getBaseContext());
        mLocalData.getmListener().loadData();
    }
}
