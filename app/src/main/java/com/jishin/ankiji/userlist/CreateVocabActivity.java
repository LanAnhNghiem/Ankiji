package com.jishin.ankiji.userlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.KanjiItemAdapter;
import com.jishin.ankiji.adapter.MojiItemAdapter;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.LocalDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CreateVocabActivity extends AppCompatActivity{
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
    private String mSetName = "", mUserID = "";
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
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.CREATE)){
            if(intent.getStringExtra(Constants.CREATE).equals(Constants.CREATE_MOJI)){
                isKanji = false;
            }
            mSetName = intent.getStringExtra(Constants.NAME);
            mUserID = intent.getStringExtra(Constants.USER_ID);
            if(mUserID.isEmpty())
                mUserID = mData.getUserID();
        }
        initControl();
        setupRecyclerView();
        setEvents();
        currentTime = Calendar.getInstance().getTime();
        //currentTime = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        //Toast.makeText(this, String.valueOf(currentTime), Toast.LENGTH_SHORT).show();
    }
    private void initControl(){
        //create data sample
        if(isKanji){
            mKanjiList.add(new Kanji("","","",""));
        }else{
            mMojiList.add(new Moji("","","",""));
        }

        //controller
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.create_vocab_menu);
        toolbar.setTitle(mSetName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtWord = findViewById(R.id.txt_vocab);
        btnDone = findViewById(R.id.btn_done);
        btnAdd = findViewById(R.id.btn_add);
        mLocalData.init(getBaseContext(),mUserID, mData);
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
                //saveDatabase();
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
                Toast.makeText(CreateVocabActivity.this, "Done", Toast.LENGTH_SHORT).show();
                //saveDatabase();
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
        }else{
            mojiAdapter = new MojiItemAdapter(mMojiList, getBaseContext());
            rvVocab.setAdapter(mojiAdapter);
        }
    }
    private void saveData(){
        String id = "";
        if(isNetworkAvailable()){

            if(!isKanji){
                id = mMojiSet.push().getKey();

            }else{
                id = mKanjiSet.push().getKey();
            }
            saveDatabase(id, isKanji);
            saveLocalDatabase(id, isKanji);
        }else{
            if(!isKanji){
                id = mMojiSet.push().getKey();
            }else{
                id = mKanjiSet.push().getKey();
            }
            saveLocalDatabase(id, isKanji);
        }
    }
    //save to Firebase
    private void saveDatabase(String id, boolean isKanji){
        //Date currentTime = Calendar.getInstance().getTime();
        Set set = new Set(id, mSetName, String.valueOf(currentTime));
        if(!isKanji){
            //String id = mMojiSet.push().getKey();
            mMojiSet.child(mUserID).child(id).setValue(set);
            mSetByUser.child(mUserID).child(id).setValue(mMojiList);
        }else{
            //String id = mKanjiSet.push().getKey();
            mKanjiSet.child(mUserID).child(id).setValue(set);
            mSetByUser.child(mUserID).child(id).setValue(mKanjiList);
        }
    }
    //save in local
    private void saveLocalDatabase(String id, boolean isKanji){

        Set set = new Set(id, mSetName, String.valueOf(currentTime));
        Map myMap = mLocalData.readAllData();
        if(!isKanji){
            Map mojiMap = mLocalData.readData(Constants.MOJI_SET_NODE);
            Map setByUserMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
            if(setByUserMap == null){
                setByUserMap = new LinkedTreeMap();
                myMap.put(Constants.SET_BY_USER_NODE, null);
            }
            if(mojiMap == null){
                mojiMap = new LinkedTreeMap();
                myMap.put(Constants.MOJI_SET_NODE, null);
            }
            mojiMap.put(id, set);
            setByUserMap.put(id, mMojiList);
            myMap.put(Constants.MOJI_SET_NODE, mojiMap);
            myMap.put(Constants.SET_BY_USER_NODE, setByUserMap);
            String str = new Gson().toJson(myMap);
            mLocalData.writeToFile(Constants.DATA_FILE+mUserID, str, getBaseContext());
            mLocalData.getmMojiListener().loadData();
        }else{
            Map kanjiMap = mLocalData.readData(Constants.KANJI_SET_NODE);
            Map setByUserMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
            if(setByUserMap == null){
                setByUserMap = new LinkedTreeMap();
                myMap.put(Constants.SET_BY_USER_NODE, null);
            }
            if(kanjiMap == null){
                kanjiMap = new LinkedTreeMap();
                myMap.put(Constants.MOJI_SET_NODE, null);
            }
            kanjiMap.put(id, set);
            setByUserMap.put(id, mKanjiList);
            myMap.put(Constants.KANJI_SET_NODE, kanjiMap);
            myMap.put(Constants.SET_BY_USER_NODE, setByUserMap);
            String str = new Gson().toJson(myMap);
            mLocalData.writeToFile(Constants.DATA_FILE+mUserID, str, getBaseContext());
            mLocalData.getmListener().loadData();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
//    @Override
//    public void setData(Kanji kanji, int position) {
//        mKanjiList.get(position).setTuvung(kanji.getTuvung());
//        mKanjiList.get(position).setKanji(kanji.getKanji());
//        mKanjiList.get(position).setAmhan(kanji.getAmhan());
//    }
}
