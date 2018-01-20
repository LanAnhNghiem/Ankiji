package com.jishin.ankiji.explores;

import android.content.Intent;
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
import com.jishin.ankiji.adapter.MojiAdater;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;

public class MojiExploresActivity extends AppCompatActivity {

    public static final String TAG = MojiExploresActivity.class.getSimpleName();

    public static final String MOJI_SOUMATOME_KEY = "Soumatome";

    private Toolbar mToolbar;
    private String mTopic;
    private ArrayList<Moji> mojiList = new ArrayList<Moji>();
    private RecyclerView mMojiRecycler;
    private MojiAdater mojiAdater;
    private DatabaseReference mMojiRef;
    private DatabaseService mData = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moji_explores);

        //Declare database reference
        Intent intent = getIntent();
        mTopic = intent.getStringExtra("Moji_Key");
        addControl();
        setReference(mTopic);
        new LoadDataTask().execute();
    }

    private void addControl() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMojiRecycler = findViewById(R.id.mojiRecyclerView);
        mojiAdater = new MojiAdater();
        mojiAdater.setmMojiList(mojiList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMojiRecycler.setLayoutManager(linearLayoutManager);
        mMojiRecycler.setAdapter(mojiAdater);
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
            getMoji();
            return null;
        }
    }

    private void getMoji() {
        mMojiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            Moji moji = ds.getValue(Moji.class);
            mojiList.add(moji);
        }
        mojiAdater.notifyDataSetChanged();
    }

    private void setReference(String topic) {
        mMojiRef = mData.getDatabase().child(Constants.MOJI_NODE).child(MOJI_SOUMATOME_KEY).child(topic);
        Log.d(TAG, "setReference: " + mMojiRef);
    }

}
