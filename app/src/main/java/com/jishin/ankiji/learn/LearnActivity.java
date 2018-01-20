package com.jishin.ankiji.learn;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardFragmentPagerAdapter;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    private final static String TAG = LearnActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private CardFragmentPagerAdapter mPagerAdapter;
    private static boolean isFront = true;
    private DataTypeEnum dataTypeEnum;
    private ArrayList<Moji> mojiList;
    private ArrayList<Kanji> kanjiList;
    private ArrayList<?> contentList = new ArrayList<>();
    private Set set;
    private DatabaseReference mSetByUserRef;
    private Toolbar mToolbar;
    private DatabaseService mData = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);


        Intent intent = getIntent();
        if (intent != null) {
            dataTypeEnum = (DataTypeEnum) intent.getSerializableExtra(Constants.DATA_TYPE);
            set = (Set) intent.getSerializableExtra(Constants.SET_BY_USER);
            if (dataTypeEnum == DataTypeEnum.Kanji) {
                kanjiList = new ArrayList<Kanji>();
            } else {
                mojiList = new ArrayList<Moji>();
            };
        }
        initParam();
        Log.i(TAG, "onCreate: contextlistsize " + this.contentList.size());
        initControls();
        new fetchData().execute();
        Log.i(TAG, "onCreate: contextlistsize " + this.contentList.size());
        setEvents();

    }

    private void initParam() {
        mSetByUserRef = mData.getDatabase()
                .child(Constants.SET_BY_USER)
                .child(mData.getUserID())
                .child(set.getId());
        Log.d(TAG, "initParam: " + mSetByUserRef.getKey());
    }

    public static boolean isFront() {
        return isFront;
    }

    private void initControls() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Learn");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar = findViewById(R.id.progress_bar);
        if (this.contentList.size() != 0){
            mProgressBar.setProgress(100 / contentList.size());
        }

        mViewPager = findViewById(R.id.viewPager);
        Log.i(TAG, "initControls: contextListSize " + contentList.size());
        mPagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), contentList, dataTypeEnum);
        mViewPager.setAdapter(mPagerAdapter);
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

    private void setEvents() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int progressValue = 0;
                if (contentList.size() != 0){
                    progressValue = ((position + 1) * 100) / contentList.size();
                }
                mProgressBar.setProgress(progressValue);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class fetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void...aVoids) {
            mSetByUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataTypeEnum == DataTypeEnum.Moji) {
                        //this.mojiList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mojiList.add(ds.getValue(Moji.class));
                        }
                        Log.d(TAG, "onDataChange: mojiList");
                        contentList = mojiList;
                    } else {
                        //this.kanjiList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            kanjiList.add(ds.getValue(Kanji.class));
                        }
                        contentList = kanjiList;
                    }
                    Log.d(TAG, "onDataChange: contentSize" + contentList.size());
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
            mPagerAdapter.setContentList(contentList);
            mPagerAdapter.createCardList();
            mPagerAdapter.notifyDataSetChanged();
        }
    }
}
