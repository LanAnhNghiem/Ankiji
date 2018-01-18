package com.jishin.ankiji.explores;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.TopicAdapter;

import java.util.ArrayList;

public class TopicMojiActivity extends AppCompatActivity {

    public static final String TAG = TopicKanjiActivity.class.getSimpleName();

    private Toolbar mToolbar;
    ArrayList<String> mojiTopicList = new ArrayList<>();
    RecyclerView mRv_MojiTopic;

    FirebaseDatabase mDatabase;
    DatabaseReference mMojiTopicRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_moji);
        initializeParam();
        getMojiTopic();
    }

    private void initializeParam() {
        mRv_MojiTopic = findViewById(R.id.rv_TopicMoji);
        mDatabase = FirebaseDatabase.getInstance();
        mMojiTopicRef = mDatabase.getReference().child("moji").child("Soumatome");
    }

    private void getMojiTopic() {
        mMojiTopicRef.addValueEventListener(new ValueEventListener() {
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

            mojiTopicList.add(ds.getKey());

            TopicAdapter topicAdapter = new TopicAdapter("MOJI");
            topicAdapter.setTopic(mojiTopicList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRv_MojiTopic.setLayoutManager(linearLayoutManager);
            mRv_MojiTopic.setAdapter(topicAdapter);
        }
    }
}
