package com.jishin.ankiji.edit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.KanjiItemAdapter;
import com.jishin.ankiji.adapter.MojiItemAdapter;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.userlist.SwipeController;
import com.jishin.ankiji.userlist.SwipeControllerActions;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.LocalDatabase;
import com.jishin.ankiji.utilities.MapHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class EditVocabActivity extends AppCompatActivity{
    private static final String TAG = EditVocabActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView rvVocab;
    private ArrayList<Kanji> mKanjiList = new ArrayList<>();
    private ArrayList<Moji> mMojiList = new ArrayList<>();
    private KanjiItemAdapter kanjiAdapter;
    private MojiItemAdapter mojiAdapter;
    private LinearLayoutManager layoutManager;
    private TextView btnDone;
    private ImageView btnAdd;
    private TextView txtWord;
    private boolean isKanji = true;
    private String mSetName = "", mUserID = "", mSetID = "";
    private Set mSet = new Set();
    private Date currentTime;
    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mMojiSet = mData.createDatabase(Constants.MOJI_SET_NODE);
    private DatabaseReference mKanjiSet = mData.createDatabase(Constants.KANJI_SET_NODE);
    private DatabaseReference mSetByUser = mData.createDatabase(Constants.SET_BY_USER_NODE);
    private LocalDatabase mLocalData = LocalDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vocab);
        getIntentData();
        initControl();
        loadData();
        setupRecyclerView();
        setEvents();
        currentTime = Calendar.getInstance().getTime();
        Toast.makeText(this, String.valueOf(currentTime), Toast.LENGTH_SHORT).show();
    }
    private void getIntentData(){
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.USER_ID)){
            //DataTypeEnum dataTypeEnum = (DataTypeEnum) intent.getSerializableExtra(Constants.DATA_TYPE);
            String type = intent.getStringExtra(Constants.DATA_TYPE);
            if(type.equals("MOJI")){
                isKanji = false;
            }
            mSet = (Set) intent.getSerializableExtra(Constants.SET_BY_USER);
            mSetName = mSet.getName();
            mSetID = mSet.getId();
            mUserID = intent.getStringExtra(Constants.USER_ID);
            if(mUserID.isEmpty())
                mUserID = mData.getUserID();
        }
    }
    private void initControl(){
        //load data set
        //new LoadSetTask().execute();
        //controller
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.create_vocab_menu);
        toolbar.setTitle(mSetName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtWord = findViewById(R.id.txt_vocab);
        btnDone = findViewById(R.id.btn_done);
        btnAdd = findViewById(R.id.btn_add);
    }
    private void loadData(){
        //init local data
        mLocalData.init(getBaseContext(),mUserID, mData);
        Map editMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
        if(editMap != null){
            if(isKanji){
                if(editMap.containsKey(mSetID)){
                    mKanjiList = MapHelper.convertToKanji(editMap, mSetID);
                }
            }
            else{
                if(editMap.containsKey(mSetID)){
                    mMojiList = MapHelper.convertToMoji(editMap, mSetID);
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (isKanji)
        {
            if(mKanjiList.size() > 0){
                showAlertDialog();
            }
            else{
                finish();
            }
        }
        else{
            if(mMojiList.size() > 0){
                showAlertDialog();
            }else{
                finish();
            }
        }

    }
    //Warning if user hasn't saved vocab set.
    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warning_unsave_data);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
                finish();
            }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private void setEvents(){
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditVocabActivity.this, "Done", Toast.LENGTH_SHORT).show();
                saveData();
                finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isKanji){
                    mKanjiList.add(new Kanji("","","",""));
                    kanjiAdapter.notifyItemInserted(mKanjiList.size());
                    rvVocab.scrollToPosition(mKanjiList.size()-1);
                    txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
                }
                else{
                    mMojiList.add(new Moji("","","",""));
                    mojiAdapter.notifyItemChanged(mMojiList.size());
                    rvVocab.scrollToPosition(mMojiList.size()-1);
                    txtWord.setText("Từ vựng ("+mMojiList.size()+")");
                }
            }
        });

        //set swipe action
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                if(isKanji){
                    mKanjiList.add(position+1,new Kanji("","","",""));
                    kanjiAdapter.notifyItemInserted(position+1);
                    rvVocab.scrollToPosition(position+1);
                    txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
                }
                else{
                    mMojiList.add(position+1,new Moji("","","",""));
                    mojiAdapter.notifyItemInserted(position+1);
                    rvVocab.scrollToPosition(position+1);
                    txtWord.setText("Từ vựng ("+mMojiList.size()+")");
                }

            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
                if(isKanji){
                    mKanjiList.remove(position);
                    kanjiAdapter.notifyItemRemoved(position);
                    rvVocab.scrollToPosition(position);
                    txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
                }
                else{
                    mMojiList.remove(position);
                    mojiAdapter.notifyItemRemoved(position);
                    rvVocab.scrollToPosition(position);
                    txtWord.setText("Từ vựng ("+mMojiList.size()+")");
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(rvVocab);
        rvVocab.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                swipeController.onDraw(c);
            }
        });
    }
    private void setupRecyclerView(){
        rvVocab = findViewById(R.id.rvVocab);
        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVocab.setLayoutManager(layoutManager);
        if(isKanji){
            kanjiAdapter = new KanjiItemAdapter(mKanjiList, getBaseContext());
            rvVocab.setAdapter(kanjiAdapter);
            txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
        }else{
            mojiAdapter = new MojiItemAdapter(mMojiList, getBaseContext());
            rvVocab.setAdapter(mojiAdapter);
            txtWord.setText("Từ vựng ("+mMojiList.size()+")");
        }
    }
    private void saveData(){
        if(isNetworkAvailable()){
            saveDatabase(isKanji);
            saveLocalDatabase(isKanji);
        }else{
            saveLocalDatabase(isKanji);
        }
    }
    //save to Firebase
    private void saveDatabase(boolean isKanji){
        if(!isKanji){
            Set set = new Set(mSetID, mSetName, String.valueOf(currentTime));
            mMojiSet.child(mUserID).child(mSetID).setValue(set);
            mSetByUser.child(mUserID).child(mSetID).setValue(mMojiList);
        }else{
            Set set = new Set(mSetID, mSetName, String.valueOf(currentTime));
            mKanjiSet.child(mUserID).child(mSetID).setValue(set);
            mSetByUser.child(mUserID).child(mSetID).setValue(mKanjiList);
        }
    }
    //save in local
    private void saveLocalDatabase(boolean isKanji){
        Set set = new Set(mSetID, mSetName, String.valueOf(currentTime));
        Map myMap = mLocalData.readAllData();
        if(!isKanji){
            Map mojiMap = mLocalData.readData(Constants.MOJI_SET_NODE);
            Map setByUserMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
            if(mojiMap != null){
                mojiMap.put(mSetID, set);
                setByUserMap.put(mSetID, mMojiList);
                myMap.put(Constants.MOJI_SET_NODE, mojiMap);
                myMap.put(Constants.SET_BY_USER_NODE, setByUserMap);
            }
        }else{
            Map kanjiMap = mLocalData.readData(Constants.KANJI_SET_NODE);
            Map setByUserMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
            if(kanjiMap!= null){
                kanjiMap.put(mSetID, set);
                setByUserMap.put(mSetID, mKanjiList);
                myMap.put(Constants.KANJI_SET_NODE, kanjiMap);
                myMap.put(Constants.SET_BY_USER_NODE, setByUserMap);
            }
        }

        String str = new Gson().toJson(myMap);
        mLocalData.writeToFile(Constants.DATA_FILE, str, getBaseContext());
        mLocalData.getmListener().loadData();
        Log.d("", str);
    }
    private class LoadSetTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            mSetByUser.child(mUserID).child(mSetID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!isKanji) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mMojiList.add(ds.getValue(Moji.class));
                        }
                    } else {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mKanjiList.add(ds.getValue(Kanji.class));
                        }
                    }
                    onProgressUpdate();
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
            if(isKanji){
                kanjiAdapter.notifyDataSetChanged();
                txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
            }else{
                mojiAdapter.notifyDataSetChanged();
                txtWord.setText("Từ vựng ("+mMojiList.size()+")");
            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
