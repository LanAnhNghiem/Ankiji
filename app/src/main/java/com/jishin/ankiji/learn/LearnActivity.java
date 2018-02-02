package com.jishin.ankiji.learn;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardFragmentPagerAdapter;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.KanjiSet;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.MojiSet;
import com.jishin.ankiji.utilities.AppDatabase;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LearnActivity extends AppCompatActivity {

    private final static String TAG = LearnActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private CardFragmentPagerAdapter mPagerAdapter;
    private static boolean isFront = true;
    private DataTypeEnum dataTypeEnum;
    private ArrayList<Moji> mojiList;
    private ArrayList<Kanji> kanjiList;
    private List<MojiSet> mMojiSet;
    private List<KanjiSet> mKanjiSet;
    private ArrayList<?> contentList = new ArrayList<>();
    private AppDatabase db;
    private Toolbar mToolbar;
    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mDateSet;
    private String mUserID = "";
    private int setIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        initAppData();
        Intent intent = getIntent();
        if (intent != null) {
            dataTypeEnum = (DataTypeEnum) intent.getSerializableExtra(Constants.DATA_TYPE);
            setIndex = intent.getIntExtra(Constants.INDEX, 0);
            mUserID = intent.getStringExtra(Constants.USER_ID);
            if (dataTypeEnum == DataTypeEnum.Kanji) {
                kanjiList = new ArrayList<Kanji>();
            } else {
                mojiList = new ArrayList<Moji>();
            }
        }
        initRef();
        new LoadData().execute();
        initControls();
        setEvents();

    }
    private void initAppData(){
        db = Room.databaseBuilder(LearnActivity.this,
                AppDatabase.class, Constants.DATABASE_NAME).allowMainThreadQueries().build();
    }

    private void initRef() {
        mDateSet = mData.createDatabase("DateSet");
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

    private void updateDate(int index) {
        Date currentDate = Calendar.getInstance().getTime();
        String id = mMojiSet.get(index).getId();
        mDateSet.child(mUserID).child(id).child("datetime").setValue(currentDate.toString());
    }

    public class LoadData extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if(dataTypeEnum == DataTypeEnum.Moji){
                mMojiSet = db.mojiSetDao().loadMojiSet();
                mojiList = mMojiSet.get(setIndex).getList();
                contentList = mojiList;
            }else{
                mKanjiSet = db.kanjiSetDao().loadKanjiSet();
                kanjiList = mKanjiSet.get(setIndex).getList();
                contentList = kanjiList;
            }
            updateDate(setIndex);
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (contentList.size() != 0) {
                mProgressBar.setProgress(100 / contentList.size());
            }
            mPagerAdapter.setContentList(contentList);
            mPagerAdapter.createCardList();
            mPagerAdapter.notifyDataSetChanged();
        }
    }
}
