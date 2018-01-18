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
import com.jishin.ankiji.adapter.MojiAdater;
import com.jishin.ankiji.model.Moji;

import java.util.ArrayList;

public class MojiExploresActivity extends AppCompatActivity {

    public static final String TAG = MojiExploresActivity.class.getSimpleName();

    public static final String MOJI_SOUMATOME_KEY = "Soumatome";

    Toolbar mToolbar;
    String mTopic;
    private ArrayList<Moji> mojiList = new ArrayList<Moji>();
    private ArrayList<String> topicList = new ArrayList<String>();
    RecyclerView mMojiRecycler;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMojiRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moji_explores);

        mMojiRecycler = findViewById(R.id.mojiRecyclerView);

        //Declare database reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMojiRef = mFirebaseDatabase.getReference().child("moji").child(MOJI_SOUMATOME_KEY);
        Log.d(TAG, "onCreate: " + mFirebaseDatabase);
        Log.d(TAG, "onCreate: " + mMojiRef);

        mTopic = "Bai_1";

        addControl();
        setReference(mTopic);
        getMoji();
//        getMojiTopic();
    }

    private void addControl() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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

//    private void showData(DataSnapshot dataSnapshot) {
//        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//            String bai = dataSnapshot1.getKey();
//            Log.d(TAG, "showData: bai = " + bai);
//
//            for (DataSnapshot ds : dataSnapshot1.getChildren()) {
//                Log.d(TAG, "showData: ds = " + ds);
//                Moji moji = new Moji();
//                moji.setAmHan(ds.getValue(Moji.class).getAmHan());
//                moji.setCachDocHira(ds.getValue(Moji.class).getCachDocHira());
//                moji.setNghiaTiengViet(ds.getValue(Moji.class).getNghiaTiengViet());
//                moji.setTuTiengNhat(ds.getValue(Moji.class).getTuTiengNhat());
//
//
////                //Test
////                Log.d(TAG, "showData: amHan: " + moji.getAmHan());
////                Log.d(TAG, "showData: cachDocHira: " + moji.getCachDocHira());
////                Log.d(TAG, "showData: nghiaTV: " + moji.getNghiaTiengViet());
////                Log.d(TAG, "showData: tuTN: " + moji.getTuTiengNhat());
//                mojiList.add(moji);
//
//                MojiAdater mojiAdater = new MojiAdater();
//                mojiAdater.setmMojiList(mojiList);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                mMojiRecycler.setLayoutManager(linearLayoutManager);
//                mMojiRecycler.setAdapter(mojiAdater);
//            }
//
//        }
//
//    }

    private void getMojiTopic() {
        mMojiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void showTopic(DataSnapshot dataSnapshot) {
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            String topic = ds.getKey();
//            topicList.add(topic);
//            TopicAdapter topicAdapter = new TopicAdapter("Moji");
//            topicAdapter.setTopic(topicList);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            mMojiRecycler.setLayoutManager(linearLayoutManager);
//            mMojiRecycler.setAdapter(topicAdapter);
//        }
//    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            Moji moji = new Moji();
            moji.setAmHan(ds.getValue(Moji.class).getAmHan());
            moji.setCachDocHira(ds.getValue(Moji.class).getCachDocHira());
            moji.setNghiaTiengViet(ds.getValue(Moji.class).getNghiaTiengViet());
            moji.setTuTiengNhat(ds.getValue(Moji.class).getTuTiengNhat());

//                //Test
            Log.d(TAG, "showData: amHan: " + moji.getAmHan());
            Log.d(TAG, "showData: cachDocHira: " + moji.getCachDocHira());
            Log.d(TAG, "showData: nghiaTV: " + moji.getNghiaTiengViet());
            Log.d(TAG, "showData: tuTN: " + moji.getTuTiengNhat());

            mojiList.add(moji);

            MojiAdater mojiAdater = new MojiAdater();
            mojiAdater.setmMojiList(mojiList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mMojiRecycler.setLayoutManager(linearLayoutManager);
            mMojiRecycler.setAdapter(mojiAdater);

        }

    }

    private void setReference(String topic) {
        mMojiRef = mFirebaseDatabase.getReference().child("moji").child(MOJI_SOUMATOME_KEY).child(topic);
        Log.d(TAG,"setReference: "+mMojiRef);
    }

}
