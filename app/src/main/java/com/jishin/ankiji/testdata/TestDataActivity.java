package com.jishin.ankiji.testdata;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jishin.ankiji.R;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.LocalDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Map;

public class TestDataActivity extends AppCompatActivity {
    public static final String TAG = TestDataActivity.class.getSimpleName();
    FirebaseDatabase mDatabase;
    DatabaseReference ref;

    Button btnSave;
    Button btnLoad;

    TextView txtLog;
    TextView txtResult;


    Map<String, Map> mainData;


    Context mContext;
    LocalDatabase mLocalData = LocalDatabase.getInstance();
    DatabaseService mData = DatabaseService.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data);
        mLocalData.init(getApplicationContext(),"yRBa49I8oOhPDReyi5rwxog02G13", mData);
        if(!mLocalData.hasLocalData()){
            mLocalData.loadAllData();
            mLocalData.readData("KanjiSet");
        }
        else{
            mLocalData.readData("KanjiSet");
        }
        mContext = this;

        // ------------ init firebase data ----------------
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();// ALL - OF - YOUR - DATA

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mainData = (Map<String, Map>) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mainData = null;
            }
        });
        // ------------------------------------------------

        // bind
        btnSave = (Button)findViewById(R.id.test_btn_save);
        btnLoad = (Button)findViewById(R.id.test_btn_load);
        txtLog = (TextView)findViewById(R.id.test_log);
        txtResult = (TextView)findViewById(R.id.test_result);


        // ----------------- button save ------------------
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mainData != null) {
                    //String str = mainData.toString();

                    // you need to compile Gson in build.gradle (app level)
                    String str = new Gson().toJson(mainData);

                    writeToFile("data.json",str, mContext);
                    txtLog.setText("Save OK!");
                    showLogcat(str);
                }
                else{
                    txtLog.setText("mainData is NULL!");
                }
            }
        });

        // ------------------- button load ------------------
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = readFromFile("data.json", mContext);
                if(str.isEmpty()) {
                    txtLog.setText("file load failed!");
                }
                else {
                    txtResult.setText(str);

                    // ------- test parse feature -------
                    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                    // readData is just like mainData, I just stored it in a different variable
                    Map<String, Object> readData = new Gson().fromJson(str, mapType);


                    String aPath = "data_kanji/N2";
                    Map aMap = MapHelper.getPath(readData, aPath);

                    // ----------------------------------

                    txtLog.setText("load success!");
                }
            }
        });
    }

    private void showLogcat(String veryLongString){
        int maxLogSize = 1000;
        for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.v(TAG, veryLongString.substring(start, end));

        }
    }
    // ------------------- file operation ---------------------

    // save 'fileName' to local, you dont need to know specific where the file stored
    private void writeToFile(String fileName, String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d(TAG, String.valueOf(context.getFileStreamPath("data.js")));
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
    // ------------------------------------------------------------------

}


// ------------------ map helper ----------------------
class MapHelper{

    // access a map node via path // path style: "string/to/path"
    public static Map getPath(Map map, String path){
        String[] subPaths = path.split("/");

        Map result = map;
        for (String subPath:
             subPaths) {
            result = (Map) result.get(subPath);
        }
        return result;
    }
}