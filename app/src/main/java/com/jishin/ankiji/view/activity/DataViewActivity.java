package com.jishin.ankiji.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.DataFragmentAdapter;

public class DataViewActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);
        getControls();
    }

    private void getControls() {
        tabLayout = (TabLayout) findViewById(R.id.Data_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.Data_viewPager);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new DataFragmentAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    }
}
