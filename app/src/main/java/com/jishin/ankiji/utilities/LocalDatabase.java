package com.jishin.ankiji.utilities;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lana on 24/01/2018.
 */

public class LocalDatabase {
    private static LocalDatabase mInstance;
    private static Context mContext;
    private static String mUserID;
    private static Map<String, Map> mUserData, mSetByUserData, mMojiSetData, mKanjiSetData, mLocalData;
    private static DatabaseService mData;
    private static DatabaseReference mUserRef, mSetByUserRef, mMojiSetRef, mKanjiSetRef;
    protected LocalDatabase(){

    }
    public static LocalDatabase getInstance(){
        if(mInstance == null){
            mInstance = new LocalDatabase();
        }
        return mInstance;
    }
    public static void init(Context context, String userID, DatabaseService data){
        mContext = context;
        mUserID = userID;
        mData = data;
        mUserRef = mData.createDatabase(Constants.USER_NODE).child(mUserID);
        mKanjiSetRef = mData.createDatabase(Constants.KANJI_SET_NODE).child(mUserID);
        mMojiSetRef = mData.createDatabase(Constants.MOJI_SET_NODE).child(mUserID);
        mSetByUserRef = mData.createDatabase(Constants.SET_BY_USER_NODE).child(mUserID);
        mLocalData = new HashMap<>();
    }
    public static void loadAllData(){
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserData = (Map<String, Map>) dataSnapshot.getValue();
                mLocalData.put(Constants.USER_NODE, mUserData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mKanjiSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mKanjiSetData = (Map<String, Map>)dataSnapshot.getValue();
                mLocalData.put(Constants.KANJI_SET_NODE, mKanjiSetData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mMojiSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMojiSetData = (Map<String, Map>)dataSnapshot.getValue();
                mLocalData.put(Constants.MOJI_SET_NODE, mMojiSetData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSetByUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSetByUserData = (Map<String, Map>)dataSnapshot.getValue();
                mLocalData.put(Constants.SET_BY_USER_NODE, mSetByUserData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void writeToFile(String fileName, String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String fileName, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
