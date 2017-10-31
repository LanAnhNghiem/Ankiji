package com.jishin.ankiji;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ProfilePictureView profilePictureView;
    LoginButton loginButton;
    Button logoutButton, functionFbButton;
    TextView txtName, txtEmail, txtFirstName;
    CallbackManager callbackManager;
    String email, name, firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        createParameters();
        profilePictureView.setVisibility(View.INVISIBLE);
        txtName.setVisibility(View.INVISIBLE);
        txtEmail.setVisibility(View.INVISIBLE);
        txtFirstName.setVisibility(View.INVISIBLE);
        functionFbButton.setVisibility(View.INVISIBLE);
        logoutButton.setVisibility(View.INVISIBLE);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        setLoginButton();
        setLogoutButton();
        setFunctionFbButton();
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance();
        super.onStart();
    }

    private void setLoginButton() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Success", "Login success");
                loginButton.setVisibility(View.INVISIBLE);
                result();
                profilePictureView.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.VISIBLE);
                txtEmail.setVisibility(View.VISIBLE);
                txtFirstName.setVisibility(View.VISIBLE);
                functionFbButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Login", error.toString());
            }
        });
    }

    public void setLogoutButton() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                logoutButton.setVisibility(View.INVISIBLE);
                //profilePictureView.setVisibility(View.INVISIBLE);
                txtName.setVisibility(View.INVISIBLE);
                txtEmail.setVisibility(View.INVISIBLE);
                txtFirstName.setVisibility(View.INVISIBLE);
                functionFbButton.setVisibility(View.INVISIBLE);
                txtEmail.setText("");
                txtName.setText("");
                txtFirstName.setText("");
                profilePictureView.setProfileId(null);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setFunctionFbButton() {
        functionFbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FbActionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("JSON", response.getJSONObject().toString());
                        try {
                            email = object.getString("email");
                            name = object.getString("name");
                            firstName = object.getString("first_name");
                            profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
                            txtName.setText(name);
                            txtEmail.setText(email);
                            txtFirstName.setText(firstName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    public void createParameters() {
        profilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
        loginButton = (LoginButton) findViewById(R.id.btn_LoginFB);
        logoutButton = (Button) findViewById(R.id.btn_LogoutFB);
        functionFbButton = (Button) findViewById(R.id.btn_FunctionFB);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtFirstName = (TextView) findViewById(R.id.txtFirstName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
