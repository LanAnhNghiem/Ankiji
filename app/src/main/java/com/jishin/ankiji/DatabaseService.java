package com.jishin.ankiji;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by LanAnh on 21/08/2017.
 */

public class DatabaseService {
    private static DatabaseService mInstance;
    private static FirebaseAuth mAuth;
    private static FirebaseDatabase mDatabase;
    private static FirebaseUser mUser;
    private static DatabaseReference mUserReference;
    private static HashMap<String, String> profileImage = new HashMap<>();

    protected DatabaseService()
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public static DatabaseService getInstance(){
        if(mInstance == null){
            mInstance = new DatabaseService();
        }
        return mInstance;
    }
    public static FirebaseAuth getAuth(){
        return mAuth;
    }
    public static DatabaseReference createDatabase(String databaseName){
        return mDatabase.getInstance().getReference(databaseName);
    }
    public static boolean isSignIn(){
        if(mUser == null)
            return false;
        else
            return true;
    }
    public static String getUserID(){
        if(!isSignIn())
            return "";
        else
            return mUser.getUid().toString();
    }
    public static String getPhotoUrl(){
        if(!isSignIn())
            return "";
        else
            return mUser.getPhotoUrl().toString();
    }
    public static String getDisplayName(){
        if(!isSignIn())
            return "";
        else
            return mUser.getDisplayName().toString();
    }
    public static String getEmail(){
        if(!isSignIn())
            return "";
        else
            return mUser.getEmail().toString();
    }
    public static String getProviderId(){
        if(!isSignIn())
            return "";
        else
            return mUser.getProviderId();
    }
    public static String getProfileImage(String userID){
        if(profileImage.containsKey(userID))
            return profileImage.get(userID);
        else
            return "";
    }
}
