package com.jishin.ankiji.explores;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.TopicAdapter;

import java.util.ArrayList;

public class TopicKanjiActivity extends AppCompatActivity {

    public static final String TAG = TopicKanjiActivity.class.getSimpleName();

    private Toolbar mToolbar;
    ArrayList<String> kanjiTopicList = new ArrayList<>();
    RecyclerView mRv_KanjiTopic;

    FirebaseDatabase mDatabase;
    DatabaseReference mKanjiTopicRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_kanji);
        initializeParam();
        new LoadDataTask().execute();
    }

    private void initializeParam() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRv_KanjiTopic = findViewById(R.id.rv_TopicKanji);
        mDatabase = FirebaseDatabase.getInstance();
        mKanjiTopicRef = mDatabase.getReference().child("data_kanji");
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

    private class LoadDataTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getKanjiTopic();
            return null;
        }
    }
    private void getKanjiTopic(){
        mKanjiTopicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot){
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            kanjiTopicList.add(ds.getKey());
            Log.d(TAG,"showData: kanjiTopicList: "+kanjiTopicList);
            TopicAdapter topicAdapter = new TopicAdapter("KANJI");
            topicAdapter.setTopic(kanjiTopicList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRv_KanjiTopic.setLayoutManager(linearLayoutManager);
            mRv_KanjiTopic.setAdapter(topicAdapter);
        }
    }
}
