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
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.Set;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private static Map<String, Map> mUserData, mSetByUserData, mMojiSetData, mKanjiSetData, mLocalData;
    private static DatabaseService mData;
    private static DatabaseReference mUserRef, mSetByUserRef, mMojiSetRef, mKanjiSetRef;
    private static boolean isCompleted = false;//is load data completed
    private static LoadDataListener mListener;

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
    public static void updateAllData(){
        new UpdateAllDataTask().execute();
    }
    private static void writeToFile(String fileName, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static String readFromFile(String fileName, Context context) {

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
                                            onProgressUpdate();
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

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // you need to compile Gson in build.gradle (app level)
            String str = new Gson().toJson(mLocalData);
            writeToFile(Constants.DATA_FILE, str, mContext);
            mListener.loadData();
        }
    }
    private static class UpdateAllDataTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            //Map map = readData(Constants.SET_BY_USER_NODE);
            Map setByUserMap = readData(Constants.SET_BY_USER_NODE);
            Map setByUserMoji = new HashMap(), setByUserKanji= new HashMap();
            ArrayList<String> mojiID = new ArrayList<>();
            //update MojiSet
            Map mojiMap = readData(Constants.MOJI_SET_NODE);
            ArrayList<Set> mojiSet = MapHelper.convertToSet(mojiMap);

            for(int i =0 ;i<mojiSet.size();i++){
                if(setByUserMap.containsKey(mojiSet.get(i).getId())){
                    //mSetByUserRef.child(mojiSet.get(i).getId()).setValue()
                    setByUserMoji.put(mojiSet.get(i).getId(),setByUserMap.get(mojiSet.get(i).getId()));
                    mojiID.add(mojiSet.get(i).getId());
                }
                mMojiSetRef.child(mojiSet.get(i).getId()).setValue(mojiSet.get(i));
            }
            //update KanjiSet
            Map kanjiMap = readData(Constants.KANJI_SET_NODE);
            ArrayList<Set> kanjiSet = MapHelper.convertToSet(kanjiMap);
            for(int i=0; i<kanjiSet.size();i++){
                if(setByUserMap.containsKey(kanjiSet.get(i).getId())){
                    //mSetByUserRef.child(mojiSet.get(i).getId()).setValue()
                    setByUserKanji.put(kanjiSet.get(i).getId(),setByUserMap.get(kanjiSet.get(i).getId()));
                }
                mKanjiSetRef.child(kanjiSet.get(i).getId()).setValue(kanjiSet.get(i));
            }
            //update SetByUser
            ArrayList<Moji> mojiList = MapHelper.convertToMoji(setByUserMoji);
            ArrayList<Kanji> kanjiList = MapHelper.convertToKanji(setByUserKanji);
            for(int i=0;i<mojiList.size();i++){
                mSetByUserRef.child(mojiID.get(i)).setValue(mojiList.get(i));
            }
            for(int i=0; i<kanjiList.size();i++){
                mSetByUserRef.child(kanjiList.get(i).getId()).setValue(kanjiList.get(i));
            }
            //mKanjiSetRef.setValue(mLocalData.get(Constants.MOJI_SET_NODE));
            return null;
        }
    }

}
