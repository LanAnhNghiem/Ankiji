package com.jishin.ankiji.explores;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.TopicAdapter;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;

public class TopicMojiActivity extends AppCompatActivity {

    public static final String TAG = TopicKanjiActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private ArrayList<String> mojiTopicList = new ArrayList<>();
    private RecyclerView mRv_MojiTopic;
    private TopicAdapter topicAdapter;
    private DatabaseReference mMojiTopicRef;
    private DatabaseService mData = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_moji);
        initializeParam();
        new LoadDataTask().execute();
    }

    private void initializeParam() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRv_MojiTopic = findViewById(R.id.rv_TopicMoji);
        topicAdapter = new TopicAdapter("MOJI");
        topicAdapter.setTopic(mojiTopicList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_MojiTopic.setLayoutManager(linearLayoutManager);
        mRv_MojiTopic.setAdapter(topicAdapter);
        mMojiTopicRef = mData.getDatabase().child(Constants.MOJI_NODE).child("Soumatome");
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

    public class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getMojiTopic();
            return null;
        }
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
        }
        topicAdapter.notifyDataSetChanged();
    }
}
