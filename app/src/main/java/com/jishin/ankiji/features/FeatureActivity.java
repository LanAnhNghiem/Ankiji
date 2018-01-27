package com.jishin.ankiji.features;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.jishin.ankiji.PROFILE.ProfileActivity;
import com.jishin.ankiji.R;
import com.jishin.ankiji.about_us.AboutUsActivity;
import com.jishin.ankiji.adapter.FragmentAdapter;
import com.jishin.ankiji.interfaces.LoadDataListener;
import com.jishin.ankiji.signin.SigninActivity;
import com.jishin.ankiji.utilities.ConnectivityChangeReceiver;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.LocalDatabase;
import com.jishin.ankiji.utilities.NetworkListener;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeatureActivity extends AppCompatActivity implements NetworkListener, LoadDataListener{
    private static final String TAG = FeatureActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private DatabaseService mData = DatabaseService.getInstance();
    private NavigationView nav;
    private String mUserID = "";
    private CircleImageView imgAvatar;
    private TextView txtUsername;
    private TextView txtEmail;
    //private FirebaseUser user;
    LocalDatabase mLocalData = LocalDatabase.getInstance();

    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        registerReceiver(new ConnectivityChangeReceiver(this),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        getUserID();
        mLocalData.init(this,mUserID, mData, this);
        if(isNetworkAvailable()){
            Toast.makeText(this, R.string.connected, Toast.LENGTH_SHORT).show();
            loadLocalData();
        }else{
            //Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
        }
        getControls();
        setEvents();
        Log.d(TAG,String.valueOf(mData.isSignIn()));
    }
    private void loadLocalData(){

        if(!mLocalData.hasLocalData()){
            mLocalData.loadAllData();
        }
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

        View hView = nav.getHeaderView(0);

        imgAvatar = hView.findViewById(R.id.imgAvatar);
        txtUsername = hView.findViewById(R.id.txtUsername_PF);
        txtEmail = hView.findViewById(R.id.txtEmail_PF);

        //user = FirebaseAuth.getInstance().getCurrentUser();


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
                        Intent profileIntent = new Intent(FeatureActivity.this, ProfileActivity.class);
                        profileIntent.putExtra(Constants.USER_ID, mUserID);
                        startActivity(profileIntent);
                        break;
//                    case R.id.item_setting:
//                        Toast.makeText(FeatureActivity.this, "Setting", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.item_use:
//                        Toast.makeText(FeatureActivity.this, "How to use", Toast.LENGTH_SHORT).show();
//                        break;
                    case R.id.about:
                        Intent aboutIntent = new Intent(FeatureActivity.this, AboutUsActivity.class);
                        startActivity(aboutIntent);
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
        //setProfileInfo();
//        if (user != null) {
//
//            mReference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
//
//            mReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    User detailUser = dataSnapshot.getValue(User.class);
//
//                    if (!TextUtils.isEmpty(detailUser.getEmail())){
//                        Log.d("txtEmail", detailUser.getEmail());
//                        txtEmail.setText(detailUser.getEmail());
//                    }
//
//                    if (!TextUtils.isEmpty(detailUser.getUsername())){
//                        Log.d("txtUsername", detailUser.getUsername());
//                        txtUsername.setText(detailUser.getUsername());
//                    }
//
//                    if (!TextUtils.isEmpty(detailUser.getLinkPhoto())){
//                        Log.d("imgAvatar", detailUser.getLinkPhoto());
//                        Glide.with(imgAvatar).load(detailUser.getLinkPhoto()).into(imgAvatar);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }
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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void connected() {
        if(mLocalData.hasLocalData()){
            mLocalData.syncData();
            setProfileInfo();
            Toast.makeText(this, "Sync data successfully", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void notConnected() {
        if(mLocalData.hasLocalData()){
            setProfileInfo();
        }
        Toast.makeText(this, getResources().getText(R.string.not_connected), Toast.LENGTH_SHORT).show();
    }
    private void setProfileInfo(){
        Map userMap = mLocalData.readData(Constants.USER_NODE);
        if(userMap != null){
            String email = String.valueOf(userMap.get("email"));
            String userName = String.valueOf(userMap.get("username"));
            String linkPhoto = String.valueOf(userMap.get("linkPhoto"));
            txtEmail.setText(email);
            txtUsername.setText(userName);
            Glide.with(imgAvatar).load(linkPhoto).into(imgAvatar);
        }
    }

    @Override
    public void loadData() {
        setProfileInfo();
    }
}