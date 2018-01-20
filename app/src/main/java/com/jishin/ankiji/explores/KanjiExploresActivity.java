package com.jishin.ankiji.explores;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.KanjiAdapter;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;

public class KanjiExploresActivity extends AppCompatActivity {

    public static final String TAG = KanjiExploresActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private ArrayList<Kanji> kanjiList = new ArrayList<Kanji>();
    private KanjiAdapter kanjiAdapter;
    private RecyclerView mKanjiRecycler;
    private String Topic;
    private DatabaseReference mKanjiRef;
    private DatabaseService mData = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_explores);
        addControl();

        //Declare database references
        Intent intent = getIntent();
        Topic = intent.getStringExtra("Kanji_Key");
        setReference(Topic);
        //Load data
        new LoadDataTask().execute();
    }

    private void addControl() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mKanjiRecycler = (RecyclerView) findViewById(R.id.kanjiRecyclerView);
        kanjiAdapter = new KanjiAdapter();
        kanjiAdapter.setmKanjiList(kanjiList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mKanjiRecycler.setLayoutManager(linearLayoutManager);
        mKanjiRecycler.setAdapter(kanjiAdapter);
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


    private void setReference(String Topic) {
        mKanjiRef =mData.getDatabase().child(Constants.KANJI_NODE).child(Topic);
    }
    public class LoadDataTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getKanji();
            return null;
        }
    }
    public void getKanji() {
        mKanjiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Kanji kanji = ds.getValue(Kanji.class);
            kanjiList.add(kanji);
        }
        if(kanjiList.size() > 0)
            kanjiAdapter.notifyDataSetChanged();
    }

}
