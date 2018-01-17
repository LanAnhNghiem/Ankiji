package com.jishin.ankiji.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.jishin.ankiji.CardFragmentPagerAdapter;
import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Item;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Button btnChange;
    private ProgressBar mProgressBar;

    private ArrayList<Item> contentList;
    private CardFragmentPagerAdapter mPagerAdapter;
    private static boolean isFont = true;

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
        return isFont;
    }

    private void dummyData() {
        for (int i = 0; i < 10; i++){
            contentList.add(new Item("FontItem " + i, "BackItem " + i));
        }

    }

    private void initControls() {
        btnChange = findViewById(R.id.btn_change);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setProgress(30);
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

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFont = !isFont;
                if(isFont){
                    btnChange.setText("Back");
                }
                else{
                    btnChange.setText("Font");
                }
                mPagerAdapter.swapData();
            }
        });
    }
}
