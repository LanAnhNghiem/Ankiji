package com.jishin.ankiji;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private ImageView imgFacebook;
    private ImageView imgGoogle;
    private TextView txtCreateAcount;
    private TextView txtForgotPass;
    
    public static final String TAG = LoginActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getControls();
        setEvents();
    }

    private void getControls() {
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        imgFacebook = (ImageView) findViewById(R.id.img_facebook);
        imgGoogle = (ImageView) findViewById(R.id.img_google);
        txtCreateAcount = (TextView) findViewById(R.id.tv_create_an_account);
        txtForgotPass = (TextView) findViewById(R.id.tv_forgot_password);
    }


    private void setEvents() {
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(LoginActivity.this, "LAN ANH ne!", Toast.LENGTH_SHORT).show();
                
            }
        });
        
        
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Thanh Hoai ne!", Toast.LENGTH_SHORT).show();
            }
        });
        
        
        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Huu Duc ne!", Toast.LENGTH_SHORT).show();   
            }
        });
        
        txtCreateAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Creat an account. TRUNG", Toast.LENGTH_SHORT).show();   
            }
        });
        
        
        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Forgotpass.TRUNG", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
