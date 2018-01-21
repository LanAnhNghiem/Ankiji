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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.FragmentAdapter;
import com.jishin.ankiji.signin.SigninActivity;
import com.jishin.ankiji.utilities.DatabaseService;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeatureActivity extends AppCompatActivity{
    private static final String TAG = FeatureActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private DatabaseService mData = DatabaseService.getInstance();
    private NavigationView nav;


    private CircleImageView imgAvatar;
    private TextView txtUsername;
    private TextView txtEmail;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        getControls();
        setEvents();
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
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.ns_menu_open,R.string.ns_menu_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav = (NavigationView) findViewById(R.id.navigationBar);

        View hView = nav.getHeaderView(0);

        imgAvatar = hView.findViewById(R.id.imgAvatar);
        txtUsername = hView.findViewById(R.id.txtUsername_PF);
        txtEmail = hView.findViewById(R.id.txtEmail_PF);

        user = FirebaseAuth.getInstance().getCurrentUser();

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
                        Toast.makeText(FeatureActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                        mData.getFirebaseAuth().signOut();
                        Intent intent = new Intent(FeatureActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                drawerLayout.closeDrawers();  // CLOSE DRAWER
                return true;
            }
        });
        if (user != null) {
            if (!TextUtils.isEmpty(user.getEmail())){
                Log.d("txtEmail", user.getEmail());
                txtEmail.setText(user.getEmail());
            }
            if (!TextUtils.isEmpty(user.getDisplayName())){
                Log.d("txtUsername", user.getDisplayName());
                txtUsername.setText(user.getDisplayName());
            }

            if (user.getPhotoUrl() != null){
                Log.d("imgAvatar", user.getPhotoUrl().toString());
                Glide.with(imgAvatar).load(user.getPhotoUrl()).into(imgAvatar);
            }else{
                Log.d("NONE_URL", "NONE_URL");
            }
        }
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