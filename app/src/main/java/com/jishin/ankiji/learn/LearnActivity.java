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
import com.jishin.ankiji.model.DateAccess;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.LocalDatabase;
import com.jishin.ankiji.utilities.MapHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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
    private Set set, dateSet;
    private DatabaseReference mSetByUserRef;
    private DatabaseReference mDateSetRef;
    private Toolbar mToolbar;
    private DatabaseService mData = DatabaseService.getInstance();
    private LocalDatabase mLocalData = LocalDatabase.getInstance();
    private DatabaseReference mDateSet = mData.createDatabase("DateSet");
    private String mUserID = "";
    public String type;
    public String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Intent intent = getIntent();
        if (intent != null) {
            dataTypeEnum = (DataTypeEnum) intent.getSerializableExtra(Constants.DATA_TYPE);
            set = (Set) intent.getSerializableExtra(Constants.SET_BY_USER);
            dateSet = set;
            Date currentDate = Calendar.getInstance().getTime();
            dateSet.setDatetime(String.valueOf(currentDate));
            mUserID = intent.getStringExtra(Constants.USER_ID);
            Log.d(TAG, "onCreate: dataTypeEnum: " + dataTypeEnum);
            Log.d(TAG, "onCreate: Set: " + set);
            if (dataTypeEnum == DataTypeEnum.Kanji) {
                kanjiList = new ArrayList<Kanji>();
                type = "Kanji";
            } else {
                mojiList = new ArrayList<Moji>();
                type = "Moji";
            }
            ;
        }
        //initParam();
        Log.i(TAG, "onCreate: contextlistsize " + this.contentList.size());
        initControls();
        loadData();
        //322new fetchData().execute();
        Log.i(TAG, "onCreate: contextlistsize " + this.contentList.size());
        setEvents();

    }

    private void loadData() {
        mLocalData.init(this, mUserID, mData);
        String id = set.getId();
        topicName = set.getName();
        Log.d(TAG, "loadData: id: " + id);
        Log.d(TAG, "loadData: topicName: " + topicName);
        updateDate(id);
        Map learnMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
        if (learnMap != null) {
            if (dataTypeEnum == DataTypeEnum.Moji) {
                if (learnMap.containsKey(id)) {
                    mojiList = MapHelper.convertToMoji(learnMap, set.getId());
                    contentList = mojiList;
                }

            } else {
                if (learnMap.containsKey(id)) {
                    kanjiList = MapHelper.convertToKanji(learnMap, set.getId());
                    contentList = kanjiList;
                }
            }
            if (contentList.size() != 0) {
                mProgressBar.setProgress(100 / contentList.size());
            }
            mPagerAdapter.setContentList(contentList);
            mPagerAdapter.createCardList();
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    private void initParam() {
        mSetByUserRef = mData.getDatabase()
                .child(Constants.SET_BY_USER)
                .child(mUserID)
                .child(set.getId());
        Log.d(TAG, "initParam: " + mSetByUserRef.getKey());
        mDateSetRef = mData.getDatabase().child("DateSet");
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
        if (this.contentList.size() != 0) {
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
                if (contentList.size() != 0) {
                    progressValue = ((position + 1) * 100) / contentList.size();
                }
                mProgressBar.setProgress(progressValue);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void setDateAccess(String topicId, String type, String date) {
        String id = mDateSet.push().getKey();
        DateAccess set = new DateAccess(type, topicId, date);
        mDateSet.child(mUserID).child(id).setValue(set);
    }

    private void updateDate(String id) {
        mDateSet.child(mUserID).child(id).setValue(dateSet);
    }

    public class fetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... aVoids) {
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
