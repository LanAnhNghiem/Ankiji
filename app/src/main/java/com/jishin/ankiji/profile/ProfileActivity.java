package com.jishin.ankiji.profile;

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
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.jishin.ankiji.R;
import com.jishin.ankiji.resetpassword.ResetPasswordActivity;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.CountSubstring;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.LocalDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ConstraintLayout layoutProfileImg;
    private ConstraintLayout layoutUsername;
    private ConstraintLayout layoutEmail;
    private ConstraintLayout layoutChangePass;
    private ConstraintLayout layoutNumberSets;
    private ConstraintLayout layoutTotalWord;

    private CircleImageView imgProfileImg;
    private TextView txtusername;
    private TextView txtUsername_err;
    private TextView txtEmail;
    private TextView txtNumberOfSet;
    private TextView txtTotalWord;

    private FirebaseUser user;
    private StorageReference storageRef;
    private DatabaseReference mReference;

    private FirebaseStorage mFirebaseStor;

    private Uri filePath = null;
    String imageUrlFromCloudStorage = "";

    public static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private String currentUsername;
    private String currentEmail;
    private String currentPhotoUrl;
    private String currentUid;

    private int totalOfWords = 0;
    private int totalOfSets = 0;
    private String mUserID = "";
    private LocalDatabase mLocalData = LocalDatabase.getInstance();
    private DatabaseService mData = DatabaseService.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.USER_ID)){
            mUserID = intent.getStringExtra(Constants.USER_ID);
        }
        mLocalData.init(this, mUserID, mData);
        setControls();
        setEvents();
    }

    private void setEvents() {
        layoutProfileImg.setOnClickListener(this);
        layoutUsername.setOnClickListener(this);
        layoutEmail.setOnClickListener(this);
        layoutChangePass.setOnClickListener(this);
    }

    private void setControls() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutProfileImg = findViewById(R.id.layoutProfileImg);
        layoutUsername = findViewById(R.id.layoutUsername);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutChangePass = findViewById(R.id.layoutChangePass);
        layoutNumberSets = findViewById(R.id.layoutNumSet);
        layoutTotalWord = findViewById(R.id.layoutNumSet);

        imgProfileImg = findViewById(R.id.imgAvatar);
        txtusername = findViewById(R.id.txtUsername);
        txtUsername_err = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail_profile);
        txtNumberOfSet = findViewById(R.id.txtNumberOfSet);
        txtTotalWord = findViewById(R.id.txtTotalOfWord);

        mFirebaseStor = FirebaseStorage.getInstance();
        storageRef = mFirebaseStor.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser() ;

        mReference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        getUserDetail();
        getNumberOfSetsAndWords();
    }

    private void getNumberOfSetsAndWords() {
        Map<String, Map> profileMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
        if(profileMap != null){
            totalOfSets = profileMap.size();
            txtNumberOfSet.setText("Number Of Sets: " + String.valueOf(totalOfSets));

            for (Map.Entry<String, Map> entry : profileMap.entrySet())
            {
                String value = String.valueOf(entry.getValue());
                totalOfWords += CountSubstring.countMatches(value, "cachDocHira=");
                totalOfWords += CountSubstring.countMatches(value, "CachDocHira=");
                totalOfWords += CountSubstring.countMatches(value, "amHan");
                totalOfWords += CountSubstring.countMatches(value, "amhan");
            }
            txtTotalWord.setText("Total Of Words: "+ String.valueOf(totalOfWords));
        }
        else{
            txtNumberOfSet.setText("Number Of Sets: 0");
            txtTotalWord.setText("Total Of Words: 0");
        }

//        mReference = FirebaseDatabase.getInstance().getReference("SetByUser");
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()){
//                    if (data.getKey().equalsIgnoreCase(user.getUid())){
//                        totalOfSets += data.getChildrenCount();
//                        for (DataSnapshot word : data.getChildren()){
//                            totalOfWords += word.getChildrenCount();
//                        }
//                    }
//                }
//                txtNumberOfSet.setText("Number Of Sets: " + String.valueOf(totalOfSets));
//                txtTotalWord.setText("Total Of Words: " + String.valueOf(totalOfWords));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public void getUserDetail() {
        Map profileMap = mLocalData.readData(Constants.USER_NODE);
        //load photo
        currentPhotoUrl = String.valueOf(profileMap.get("linkPhoto"));
        Glide.with(imgProfileImg).load(currentPhotoUrl).into(imgProfileImg);
        //load email
        currentEmail = String.valueOf(profileMap.get("email"));
        txtEmail.setText(currentEmail);
        //load username
        currentUsername = String.valueOf(profileMap.get("username"));
        txtusername.setText(currentUsername);
        //load user id
        currentUid = String.valueOf(profileMap.get("id"));
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User detailUser = dataSnapshot.getValue(User.class);
//
//                if (!TextUtils.isEmpty(detailUser.getLinkPhoto())){
//                    currentPhotoUrl = detailUser.getLinkPhoto();
//                    Log.d("imgAvatar", currentPhotoUrl);
//                    Glide.with(imgProfileImg).load(currentPhotoUrl).into(imgProfileImg);
//                }
//
//                if (!TextUtils.isEmpty(detailUser.getUsername())){
//                    currentUsername = detailUser.getUsername();
//                    Log.d("Username", currentUsername);
//                    txtusername.setText(detailUser.getUsername());
//                }
//                if (!TextUtils.isEmpty(detailUser.getEmail())){
//                    currentEmail = detailUser.getEmail();
//                    Log.d("Emai", currentEmail);
//                    txtEmail.setText(detailUser.getEmail());
//                }
//                currentUid = detailUser.getId();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutProfileImg:
                if(isNetworkAvailable()){
                    selectImage();
                }
                else{
                    Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layoutUsername:
                if(isNetworkAvailable()){
                    Intent intentChangeName = new Intent(ProfileActivity.this, ChangeUsernameActivity.class);
                    intentChangeName.putExtra("currentUid", currentUid);
                    intentChangeName.putExtra("currentUsername", currentUsername);
                    startActivity(intentChangeName);
                }
                else{
                    Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layoutEmail:
                break;
            case R.id.layoutChangePass:
                if(isNetworkAvailable()){
                    Intent intentChangePass = new Intent(this, ResetPasswordActivity.class);
                    startActivity(intentChangePass);
                }
                else{
                    Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        final Boolean result = ContextCompat.checkSelfPermission( this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED;

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Take Photo")){
                    if (checkCamPermission())
                        cameraIntent();
                }else if (items[i].equals("Choose from Library")) {
                    if (checkGaleryPermission())
                        galleryIntent();
                }else if (items[i].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
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

        imgProfileImg.setImageBitmap(thumbnail);
        deleteOldProfileImage();
        uploadImageCaptureAndUpdateDB(filePath);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            filePath = data.getData();
            Log.d("FILE_PATH", filePath.toString());
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgProfileImg.setImageBitmap(bm);
        deleteOldProfileImage();
        uploadImageInGaleryAndUpdateUser(filePath);
    }

    public void deleteOldProfileImage() {
//        Toast.makeText(this, "currentImageURL" + currentPhotoUrl, Toast.LENGTH_LONG).show();
        if (!TextUtils.isEmpty(currentPhotoUrl)){
            Log.d("OLD_IMAGE", currentPhotoUrl);
//            StorageReference delRef = mFirebaseStor.getReferenceFromUrl("Photos/" + currentPhotoUrl);
            StorageReference delRef = storageRef.child("Photos/" + currentPhotoUrl);
            delRef.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfileActivity.this, "Delete Old Profile Image successfully",
                                    Toast.LENGTH_LONG).show();
                            currentPhotoUrl = "";
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ProfileActivity.this, "Delete Old Profile Image failed",
//                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            Log.d("NO_OLD_IMAGE", "NO_OLD_IMAGE");
        }

    }

    private void uploadImageInGaleryAndUpdateUser(Uri uri) {
        if (uri != null) {
            StorageReference ref = storageRef.child("Photos/" + UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            imageUrlFromCloudStorage = downloadUri.toString();
                            Log.d("imageUrlCloud_Galery", imageUrlFromCloudStorage);
                            savePhoto(imageUrlFromCloudStorage);
                            Toast.makeText(ProfileActivity.this, "Upload Image in Galery Successfully", Toast.LENGTH_LONG).show();

                            updateUserProfileImage(imageUrlFromCloudStorage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, "Upload Galery Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            Log.d("URI NULL", "URI NULL");
        }

    }
    //save photo to local data
    private void savePhoto(String url){
        Map myMap = mLocalData.readAllData();
        Map userMap = mLocalData.readData(Constants.USER_NODE);
        userMap.put("linkPhoto", url);
        myMap.put(Constants.USER_NODE, userMap);
        String str = new Gson().toJson(myMap);
        mLocalData.writeToFile(Constants.DATA_FILE+mUserID, str, getBaseContext());
        mLocalData.getmListener().loadData();
    }

    private void uploadImageCaptureAndUpdateDB(Uri uri){
        if (uri != null) {
            StorageReference ref = storageRef.child("Photos").child(uri.getLastPathSegment());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            imageUrlFromCloudStorage = downloadUri.toString();
                            Log.d("imageUrlCloud_Capture", imageUrlFromCloudStorage);
                            Toast.makeText(ProfileActivity.this, "Upload Image Capture Successfully", Toast.LENGTH_LONG).show();
                            deleteOldProfileImage();
                            updateUserProfileImage(downloadUri.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, "Upload Capture Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    private void updateUserProfileImage(String newImageUrl) {
        FirebaseDatabase.getInstance().getReference("User").child(currentUid).child("linkPhoto")
                .setValue(newImageUrl);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
