package com.jishin.ankiji.features;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.FragmentAdapter;

import java.util.ArrayList;

public class FeatureActivity extends AppCompatActivity{
    private static final String TAG = FeatureActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;
    private RecyclerView rvMenuItem;
    private MenuItemAdapter menuItemAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        getControls();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getControls() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        drawerLayout = findViewById(R.id.drawerLayout);
//        drawerPane = findViewById(R.id.drawerPane);
//        rvMenuItem = findViewById(R.id.rvMenuItem);
//        layoutManager = new LinearLayoutManager(getBaseContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rvMenuItem.setLayoutManager(layoutManager);
//        menuItemAdapter = new MenuItemAdapter(createData(), createIcon(), getBaseContext());
//        rvMenuItem.setAdapter(menuItemAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

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
//        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.ns_menu_open,R.string.ns_menu_close){
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                invalidateOptionsMenu();
//            }
//        };
//        drawerLayout.addDrawerListener(drawerToggle);
//    }
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = drawerLayout.isDrawerOpen(rvMenuItem);
//        //menu.findItem(R.id.action_search).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }
    //@Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if(drawerToggle.onOptionsItemSelected(item)){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        drawerToggle.syncState();
    }

    private ArrayList<String> createData(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Profile");
        list.add("Setting");
        list.add("How to use");
        list.add("About us");
        return list;
    }
    private ArrayList<Integer> createIcon(){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0;i<4;i++){
            list.add(R.drawable.ic_item_menu);
        }
        return list;
    }
}
