package com.jishin.ankiji.features;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.FragmentAdapter;
import com.jishin.ankiji.signin.SigninActivity;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;


public class FeatureActivity extends AppCompatActivity{
    private static final String TAG = FeatureActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private DatabaseService mData = DatabaseService.getInstance();
    private NavigationView nav;
    private String mUserID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        getUserID();
        getControls();
        setEvents();
        Log.d(TAG,String.valueOf(mData.isSignIn()));
    }
    private void getUserID(){
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.USER_ID)){
            mUserID = intent.getStringExtra(Constants.USER_ID);
        }
    }

    private void getControls() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(),R.color.startBackground));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(getBaseContext(),R.color.startBackground));
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mUserID));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.ns_menu_open,R.string.ns_menu_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav = (NavigationView) findViewById(R.id.navigationBar);

    }
    private void setEvents(){
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
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.item_profile:
                        Toast.makeText(FeatureActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_setting:
                        Toast.makeText(FeatureActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_use:
                        Toast.makeText(FeatureActivity.this, "How to use", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.log_out:
                        Log.d(TAG,String.valueOf(mData.isSignIn()));
                        Toast.makeText(FeatureActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                        mData.signOut();
                        Log.d(TAG,String.valueOf(mData.isSignIn()));
                        Intent intent = new Intent(FeatureActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                drawerLayout.closeDrawers();  // CLOSE DRAWER
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}