package com.jishin.ankiji.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jishin.ankiji.interfaces.LoadDataListener;
import com.jishin.ankiji.interfaces.LoadKanjiListener;
import com.jishin.ankiji.interfaces.LoadMojiListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lana on 24/01/2018.
 */

public class LocalDatabase {
    private static final String TAG = LocalDatabase.class.getSimpleName();
    private static LocalDatabase mInstance;
    private static Context mContext;
    private static String mUserID;
    private static Map<String, Map> mUserData;
    private static Map<String, Map> mSetByUserData;
    private static Map<String, Map> mKanjiSetData;
    private static Map<String, Map> mMojiSetData;
    private static Map<String, Map> mLocalData;
    private static DatabaseService mData;
    private static DatabaseReference mUserRef, mSetByUserRef, mMojiSetRef, mKanjiSetRef;
    private static boolean isCompleted = false;//is load data completed
    private static LoadMojiListener mMojiListener;
    private static LoadDataListener mListener;
    private static LoadKanjiListener mKanjiListener;
    public static LoadDataListener getmListener() {
        return mListener;
    }
    public static LoadMojiListener getmMojiListener() {
        return mMojiListener;
    }
    public static LoadKanjiListener getmKanjiListener() {
        return mKanjiListener;
    }
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
    public static void init(Context context, String userID, DatabaseService data, LoadMojiListener listener){
        mContext = context;
        mUserID = userID;
        mData = data;
        mUserRef = mData.createDatabase(Constants.USER_NODE).child(mUserID);
        mKanjiSetRef = mData.createDatabase(Constants.KANJI_SET_NODE).child(mUserID);
        mMojiSetRef = mData.createDatabase(Constants.MOJI_SET_NODE).child(mUserID);
        mSetByUserRef = mData.createDatabase(Constants.SET_BY_USER_NODE).child(mUserID);
        mLocalData = new HashMap<>();
        mMojiListener = listener;
    }
    public static void init(Context context, String userID, DatabaseService data, LoadKanjiListener listener){
        mContext = context;
        mUserID = userID;
        mData = data;
        mUserRef = mData.createDatabase(Constants.USER_NODE).child(mUserID);
        mKanjiSetRef = mData.createDatabase(Constants.KANJI_SET_NODE).child(mUserID);
        mMojiSetRef = mData.createDatabase(Constants.MOJI_SET_NODE).child(mUserID);
        mSetByUserRef = mData.createDatabase(Constants.SET_BY_USER_NODE).child(mUserID);
        mLocalData = new HashMap<>();
        mKanjiListener = listener;
    }
    public static void init(Context context, String userID, DatabaseService data, LoadDataListener listener){
        mContext = context;
        mUserID = userID;
        mData = data;
        mUserRef = mData.createDatabase(Constants.USER_NODE).child(mUserID);
        mKanjiSetRef = mData.createDatabase(Constants.KANJI_SET_NODE).child(mUserID);
        mMojiSetRef = mData.createDatabase(Constants.MOJI_SET_NODE).child(mUserID);
        mSetByUserRef = mData.createDatabase(Constants.SET_BY_USER_NODE).child(mUserID);
        mLocalData = new HashMap<>();
        mListener = listener;
    }

    public static boolean isLoadCompleted(){
        return isCompleted;
    }
    public static void loadAllData(){
        new LoadAllDataTask().execute();
    }
    public static void syncData(){
        new SyncDataTask().execute();
    }
    public static void writeToFile(String fileName, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(String fileName, Context context) {

        String ret = "";

        try {
            Log.d(TAG, fileName);
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
    public static boolean hasLocalData(){
        String str = readFromFile(Constants.DATA_FILE, mContext);
        if(!str.trim().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    //access data through path
    public static Map<String, Map> readData(String path){
        String str = readFromFile(Constants.DATA_FILE, mContext);
        if(hasLocalData())
         {
            // ------- test parse feature -------
            Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
            // readData is just like mainData, stored it in a different variable
            Map<String, Object> readData = new Gson().fromJson(str, mapType);
            String aPath = path;
            Map result = MapHelper.getPath(readData, aPath);
            return result;
        }
        return null;
    }
    public static Map<String, Map> readAllData(){
        String str = readFromFile(Constants.DATA_FILE, mContext);
        if(hasLocalData())
        {
            // ------- test parse feature -------
            Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
            // readData is just like mainData, stored it in a different variable
            Map<String, Map> readData = new Gson().fromJson(str, mapType);

            return readData;
        }
        return null;
    }
    //Load data from Firebase and save to local
    private static class LoadAllDataTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUserData = (Map<String, Map>) dataSnapshot.getValue();
                    mLocalData.put(Constants.USER_NODE, mUserData);
                    mKanjiSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mKanjiSetData = (Map<String, Map>)dataSnapshot.getValue();
                            mLocalData.put(Constants.KANJI_SET_NODE, mKanjiSetData);
                            mMojiSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mMojiSetData = (Map<String, Map>)dataSnapshot.getValue();
                                    mLocalData.put(Constants.MOJI_SET_NODE, mMojiSetData);
                                    mSetByUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mSetByUserData = (Map<String, Map>)dataSnapshot.getValue();
                                            mLocalData.put(Constants.SET_BY_USER_NODE, mSetByUserData);
                                            String str = new Gson().toJson(mLocalData);
                                            writeToFile(Constants.DATA_FILE, str, mContext);
                                            mListener.loadData();
                                            mMojiListener.loadData();
//                                            mKanjiListener.loadData();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
    //Sync data from local to Firebase
    private static class SyncDataTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            //update SetByUser
            Map setByUserMap = readData(Constants.SET_BY_USER_NODE);
            Log.d(TAG, String.valueOf(setByUserMap));
            mSetByUserRef.setValue(setByUserMap);

            //update MojiSet
            Map mojiMap = readData(Constants.MOJI_SET_NODE);
            Log.d(TAG, String.valueOf(mojiMap));
            mMojiSetRef.setValue(mojiMap);

            //update KanjiSet
            Map kanjiMap = readData(Constants.KANJI_SET_NODE);
            Log.d(TAG, String.valueOf(kanjiMap));
            mKanjiSetRef.setValue(kanjiMap);

            //update Profile user
            Map userMap = readData(Constants.USER_NODE);
            mUserRef.setValue(userMap);
            Log.d(TAG, String.valueOf(userMap));
            return null;
        }
    }

}
