package com.jishin.ankiji.learn;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardFragmentPagerAdapter;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.utilities.Constants;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private CardFragmentPagerAdapter mPagerAdapter;
    private static boolean isFront = true;
    private DataTypeEnum dataTypeEnum;
    private ArrayList<Moji> mojiList;
    private ArrayList<Kanji> kanjiList;
    private ArrayList<?>contentList;
    private final static String TAG = LearnActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);


        Intent intent = getIntent();
        if (intent != null) {
            dataTypeEnum = (DataTypeEnum) intent.getSerializableExtra(Constants.DATA_TYPE);
            if (dataTypeEnum == DataTypeEnum.Kanji) {
                kanjiList = new ArrayList<Kanji>();
            } else {
                mojiList = new ArrayList<Moji>();
            };
        }
        dummyData();
        initControls();
        setEvents();
    }


    public static boolean isFont() {
        return isFront;
    }

    private void dummyData() {

        if (dataTypeEnum == DataTypeEnum.Moji) {
            for (int i = 0; i < 15; i++) {
                Moji moji = new Moji(
                        "AmHan " + i,
                        "CachDoc " + i,
                        "NghiaTV " + i,
                        "TuNhat " + i);
                this.mojiList.add(moji);
            }
            this.contentList = this.mojiList;
        } else {
            for (int i = 0; i < 15; i++) {
                Kanji kanji = new Kanji("" + i,
                        "AnHan " + i,
                        "Kanji " + i,
                        "TuVung " + i);
                this.kanjiList.add(kanji);
            }
            this.contentList = this.kanjiList;
        }
    }

    private void initControls() {
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setProgress(100 / contentList.size());
        mViewPager = findViewById(R.id.viewPager);
        mPagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), contentList, dataTypeEnum);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void setEvents() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int progressValue = ((position + 1) * 100) / contentList.size();
                mProgressBar.setProgress(progressValue);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
