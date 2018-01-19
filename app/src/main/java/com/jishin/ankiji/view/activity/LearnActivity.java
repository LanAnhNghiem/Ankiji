package com.jishin.ankiji.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.jishin.ankiji.CardFragmentPagerAdapter;
import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Kanji;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private ArrayList<Kanji> contentList;
    private CardFragmentPagerAdapter mPagerAdapter;
    private static boolean isFront = true;

    private final static String TAG = LearnActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        contentList = new ArrayList<>();
        dummyData();
        initControls();
        setEvents();
    }


    public static boolean isFont() {
        return isFront;
    }

    private void dummyData() {
        for (int i = 0; i < 15; i++){
            contentList.add(new Kanji("" + i, "Am han " + i, "Kanji " + i, "Tu vung " + i));
        }

    }

    private void initControls() {
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setProgress(100/contentList.size());
        mViewPager = findViewById(R.id.viewPager);
        mPagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), contentList);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void setEvents() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int progressValue = ((position+1) * 100)/contentList.size();
                mProgressBar.setProgress(progressValue);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
