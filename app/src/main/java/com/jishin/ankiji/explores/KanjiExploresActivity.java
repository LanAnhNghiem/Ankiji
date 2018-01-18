package com.jishin.ankiji.explores;

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
import com.jishin.ankiji.adapter.KanjiAdapter;
import com.jishin.ankiji.model.Kanji;

import java.util.ArrayList;

public class KanjiExploresActivity extends AppCompatActivity {

    public static final String TAG = KanjiExploresActivity.class.getSimpleName();

    public static final String KEY = "N5";

    Toolbar mToolbar;
    ArrayList<Kanji> kanjiList = new ArrayList<Kanji>();

    RecyclerView mKanjiRecycler, mTopicRecycler;
    String Topic;


    private FirebaseDatabase mDatabase;
    private DatabaseReference mKanjiRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_explores);

        mKanjiRecycler = (RecyclerView) findViewById(R.id.kanjiRecyclerView);
        mTopicRecycler = findViewById(R.id.kanji_topic_recycler);

        //Declare database references
        mDatabase = FirebaseDatabase.getInstance();
        mKanjiRef = mDatabase.getReference().child("data_kanji");
        setTopic(KEY);
        setReference(Topic);
        addControl();
        getKanji();
    }

    private void addControl() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void setReference(String Topic) {
        mKanjiRef = mKanjiRef.child(Topic);
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

//    private void getTopic() {
//       mKanjiRef.addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(DataSnapshot dataSnapshot) {
//               topicList.add(dataSnapshot.getKey());
//                Log.d(TAG,"getTopic"+topicList);
//               TopicAdapter topicAdapter = new TopicAdapter("Kanji");
//               topicAdapter.setTopic(topicList);
//
//           }
//
//           @Override
//           public void onCancelled(DatabaseError databaseError) {
//
//           }
//       });
//
//    }

    private void showData(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Kanji kanji = new Kanji();
            //               Log.d(TAG, "showData: ds = " + ds);
            kanji.setAmhan(ds.getValue(Kanji.class).getAmhan());
            kanji.setTuvung(ds.getValue(Kanji.class).getTuvung());
            kanji.setKanji(ds.getValue(Kanji.class).getKanji());

            kanjiList.add(kanji);

            KanjiAdapter kanjiAdapter = new KanjiAdapter();
            kanjiAdapter.setmKanjiList(kanjiList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mKanjiRecycler.setLayoutManager(linearLayoutManager);
            mKanjiRecycler.setAdapter(kanjiAdapter);
            Log.d(TAG, "showData: amHan: " + kanji.getAmhan());
            Log.d(TAG, "showData: tuVung: " + kanji.getTuvung());
            Log.d(TAG, "showData: Kanji: " + kanji.getKanji());
        }
    }

    public void setTopic(String topic){
        Topic = topic;
    }

}
