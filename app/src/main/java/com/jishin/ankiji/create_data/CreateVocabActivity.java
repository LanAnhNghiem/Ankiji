package com.jishin.ankiji.create_data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.KanjiSet;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.MojiSet;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.userlist.SwipeController;
import com.jishin.ankiji.userlist.SwipeControllerActions;
import com.jishin.ankiji.utilities.AppDatabase;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class CreateVocabActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private RecyclerView rvVocab;
    private ArrayList<Kanji> mKanjiList = new ArrayList<>();
    private ArrayList<Moji> mMojiList = new ArrayList<>();
    private KanjiCardAdapter kanjiAdapter;
    private MojiCardAdapter mojiAdapter;
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
    private DatabaseReference mUserList = mData.createDatabase(Constants.USER_LIST_NODE);
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vocab);
        initAppData();
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
    }
    private void initAppData(){
        db = Room.databaseBuilder(CreateVocabActivity.this,
                AppDatabase.class, Constants.DATABASE_NAME).allowMainThreadQueries().build();
    }
    private void initControl(){
        //create data sample
//        if(isKanji){
//            mKanjiList.add(new Kanji("","","",""));
//        }else{
//            mMojiList.add(new Moji("","","",""));
//        }

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
//                    mKanjiList.add(new Kanji("","","",""));
//                    kanjiAdapter.notifyItemInserted(mKanjiList.size());
//                    rvVocab.scrollToPosition(mKanjiList.size()-1);
                    int pos = mKanjiList.size() + 1;
                    showInputDialog(pos);

                }
                else{
                    int pos = mMojiList.size() + 1;
                    showInputDialog(pos);
                }
            }
        });

        //set swipe action
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                showInputDialog(position);

            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
                if(isKanji){
                    mKanjiList.remove(position);
                    kanjiAdapter.notifyItemRemoved(position);
//                    rvVocab.scrollToPosition(position);
//                    txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
                }
                else{
                    mMojiList.remove(position);
                    mojiAdapter.notifyItemRemoved(position);
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
            ArrayList<Kanji> list = mKanjiList;
            Collections.reverse(list);
            kanjiAdapter = new KanjiCardAdapter(list, getBaseContext());
            rvVocab.setAdapter(kanjiAdapter);
        }else{
            ArrayList<Moji> list = mMojiList;
            Collections.reverse(list);
            mojiAdapter = new MojiCardAdapter(list, getBaseContext());
            rvVocab.setAdapter(mojiAdapter);
        }
    }
    private void showInputDialog(final int position){
        LayoutInflater layoutInflater = LayoutInflater.from(CreateVocabActivity.this);
        if(!isKanji){
            final View dialogView = layoutInflater.inflate(R.layout.dialog_moji_content, null);
            final TextView tvTitle = dialogView.findViewById(R.id.txtTitle);
            final TextInputEditText edtTu = dialogView.findViewById(R.id.edtTu);
            final TextInputEditText edtAmHan = dialogView.findViewById(R.id.edtAmHan);
            final TextInputEditText edtHira = dialogView.findViewById(R.id.edtHira);
            final TextInputEditText edtNghia = dialogView.findViewById(R.id.edtNghia);
            int total = mMojiList.size() + 1;
            final int pos = position + 1;
            tvTitle.setText("Từ số "+pos+"/"+ total);
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateVocabActivity.this);
            builder.setView(dialogView);
            builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String id = mMojiSet.push().getKey();
                    Moji moji = new Moji(id, edtAmHan.getText().toString(), edtHira.getText().toString(),
                            edtNghia.getText().toString(), edtTu.getText().toString());
                    mMojiList.add(position, moji);
                    txtWord.setText("Từ vựng ("+mMojiList.size()+")");
                    mojiAdapter.notifyItemInserted(position);
                }
            }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }else{
            final View dialogView = layoutInflater.inflate(R.layout.dialog_kanji_content, null);
            final TextView tvTitle = dialogView.findViewById(R.id.txtTitle);
            final TextInputEditText edtKanji = dialogView.findViewById(R.id.edtTu);
            final TextInputEditText edtAmHan = dialogView.findViewById(R.id.edtAmHan);
            final TextInputEditText edtTu = dialogView.findViewById(R.id.edtTu);
            int total = mKanjiList.size() + 1;
            int pos = position + 1;
            tvTitle.setText("Từ số "+pos+"/"+ total);
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateVocabActivity.this);
            builder.setView(dialogView);
            builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String id = mKanjiSet.push().getKey();
                    Kanji kanji = new Kanji(id, edtAmHan.getText().toString()
                            ,edtKanji.getText().toString(), edtTu.getText().toString());
                    mKanjiList.add(position, kanji);
                    txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
                    kanjiAdapter.notifyItemInserted(position);
                }
            }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
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
            new SaveLocalData(id).execute();
        }else{
            if(!isKanji){
                id = mMojiSet.push().getKey();
            }else{
                id = mKanjiSet.push().getKey();
            }
            new SaveLocalData(id).execute();
        }
    }
    //save to Firebase
    private void saveDatabase(String id, boolean isKanji){
        if(!isKanji){
            Set set = new Set(id, mSetName, String.valueOf(currentTime), 0);
            mMojiSet.child(mUserID).child(id).setValue(set);
            mUserList.child(mUserID).child(id).setValue(mMojiList);
        }else{
            Set set = new Set(id, mSetName, String.valueOf(currentTime), 1);
            mKanjiSet.child(mUserID).child(id).setValue(set);
            mUserList.child(mUserID).child(id).setValue(mKanjiList);
        }
    }

    private class SaveLocalData extends AsyncTask<Void, Void, Void>{
        private String mID = "";

        public SaveLocalData(String id) {
            this.mID = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(isKanji){
                KanjiSet kanjiSet = new KanjiSet(mID, mSetName, String.valueOf(currentTime), 0, mKanjiList);
                db.kanjiSetDao().insertKanjiSet(kanjiSet);
            }else{
                MojiSet mojiSet = new MojiSet(mID, mSetName, String.valueOf(currentTime), 1, mMojiList);
                db.mojiSetDao().insertMojiSet(mojiSet);
            }
            return null;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
