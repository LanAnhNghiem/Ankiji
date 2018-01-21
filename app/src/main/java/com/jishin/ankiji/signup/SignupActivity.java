package com.jishin.ankiji.signup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jishin.ankiji.R;
import com.jishin.ankiji.model.User;
import com.jishin.ankiji.signin.SigninActivity;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {
    public static String TAG = SignupActivity.class.getSimpleName();
    private Toolbar toolbar;
    TextInputEditText mUsername, mEmail, mPassword, mConfirmPass;
    Button mBtnSignUp;
    TextView txtUsername, txtEmail, txtPass, txtConfirmPass;
    CircleImageView imgAvatar_SignUp;

    DatabaseService mDatabase = DatabaseService.getInstance();
    DatabaseReference mUserRef = mDatabase.createDatabase(Utilities.USER_REFERENCE);

    StorageReference storageRef;

    private Uri filePath = null;

    private String userChoosenTask;
    String imageUrlFromCloudStorage = "";

    public static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Log.d("VO_LAm_GI", "VO_LAm_GI");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUsername = findViewById(R.id.edt_username);
        mEmail = findViewById(R.id.edt_email);
        mPassword = findViewById(R.id.edt_password);
        mConfirmPass = findViewById(R.id.edt_confirm_password);

        mBtnSignUp = findViewById(R.id.btnSignup);

        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPassword);
        txtConfirmPass = findViewById(R.id.txtConfirmPass);

        imgAvatar_SignUp = findViewById(R.id.imgAvatar_SignUp);

        storageRef = FirebaseStorage.getInstance().getReference();

        imgAvatar_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        textChangeListener();
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim(),
                        email = mEmail.getText().toString().trim(),
                        password = mPassword.getText().toString().trim(),
                        confirmPass = mConfirmPass.getText().toString().trim();
                Log.d("imageUrl_click",imageUrlFromCloudStorage );
                Log.d("username", username);
                Log.d("email", email);
                Log.d("password", password);
                Log.d("repassword", confirmPass);
                Log.d("ISNOImage", String.valueOf(!isNoImage()));
                //registerAccount(email, password, username, "");
                if (isValidUsername(username) && isValidEmail(email)
                        && isValidPass(password)
                        && isPassMatching(password, confirmPass) && !isNoImage()) {
                    Log.d(TAG, "register");
                    hideKeyboard(view);
                    mBtnSignUp.setEnabled(false);
                    mBtnSignUp.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorDisable));
                    registerAccount(email, password, username, imageUrlFromCloudStorage);
                }
                if (!isConnected()) {
                    Toast.makeText(SignupActivity.this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "fail1");
                }
            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void textChangeListener() {
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidUsername(editable.toString())) {
                    txtUsername.setText(R.string.invalid_username);
                    Log.d(TAG, "fail2");
                } else
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
                if (!isValidEmail(editable.toString())) {
                    txtEmail.setText(R.string.invalid_email);
                    Log.d(TAG, "fail3");
                } else {
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
                if (!isValidPass(editable.toString())) {
                    txtPass.setText(R.string.invalid_password);
                    Log.d(TAG, "fail4");
                } else {
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
                if (!isPassMatching(mPassword.getEditableText().toString(), editable.toString())) {
                    txtConfirmPass.setText(R.string.pass_not_matching);
                    Log.d(TAG, "fail5");
                } else {
                    txtConfirmPass.setText("");
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

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private boolean isValidPass(String pass) {
        if (pass.length() < 6) {
            return false;
        }
        return true;
    }

    private boolean isNoImage() {
        if (imgAvatar_SignUp.getDrawable() == null)
            return true;
        return false;
    }

    private boolean isPassMatching(String pass, String confirm) {
        if (pass.equals(confirm))
            return true;
        return false;
    }

    private void registerAccount(final String email, final String password, final String username, final String linkPhoto) {
        FirebaseAuth mAuth = mDatabase.getFirebaseAuth();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(task.getResult().getUser().getUid(), username, email, linkPhoto);
                    //setProgressBar();
                    addNewUserOnFirebase(user);
                } else {
                    Toast.makeText(SignupActivity.this, R.string.sign_up_failed, Toast.LENGTH_SHORT).show();
                    mBtnSignUp.setEnabled(true);
                    mBtnSignUp.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                }
            }
        });
    }

    private void addNewUserOnFirebase(final User user) {
        final DatabaseReference mRef = mUserRef.child(user.getId());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mRef.setValue(user);
                    Toast.makeText(SignupActivity.this, R.string.sign_up_successful, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignupActivity.this, R.string.sign_up_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Add Photo!");
        final Boolean result = ContextCompat.checkSelfPermission( this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED;
        Log.d("RESULT", result.toString());

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Take Photo")){
                    userChoosenTask = "Take Photo";
                    if (checkCamPermission())
                        cameraIntent();
                }else if (items[i].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if (checkGaleryPermission())
                        galleryIntent();
                }else if (items[i].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void uploadImageInGaleryAndGetUrl(Uri uri) {
        if (uri != null) {
            StorageReference ref = storageRef.child("Photos/" + UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            imageUrlFromCloudStorage = downloadUri.toString();
                            Log.d("imageUrlCloud_Galery", imageUrlFromCloudStorage);
                            Toast.makeText(SignupActivity.this, "Upload Image in Galery Successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, "Upload Galery Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void uploadImageCaptureAndGetUrl(Uri uri){
        if (uri != null) {
            StorageReference ref = storageRef.child("Photos").child(uri.getLastPathSegment());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            imageUrlFromCloudStorage = downloadUri.toString();
                            Log.d("imageUrlCloud_Capture", imageUrlFromCloudStorage);
                            Toast.makeText(SignupActivity.this, "Upload Image Capture Successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, "Upload Capture Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    private Boolean checkCamPermission () {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA )
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},REQUEST_CAMERA);
            return false;
        }else{
            return true;
        }
    }

    private Boolean checkGaleryPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CAMERA:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED){

                        cameraIntent();
                    }
                }
                else {
                    Toast.makeText(this, "Can't get camera because of permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            case SELECT_FILE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                }
                else {
                    Toast.makeText(this, "Can't get location because of permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        filePath = Uri.fromFile(destination);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Log.d("FilePath_Capture", filePath.toString());
        uploadImageCaptureAndGetUrl(filePath);

        imgAvatar_SignUp.setImageBitmap(thumbnail);
    }


    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            filePath = data.getData();
//            Log.d("FILE_PATH", filePath.toString());
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        uploadImageInGaleryAndGetUrl(filePath);
        imgAvatar_SignUp.setImageBitmap(bm);
    }
}
