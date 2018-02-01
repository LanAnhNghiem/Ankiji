package com.jishin.ankiji.features;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.about_us.AboutUsActivity;
import com.jishin.ankiji.adapter.FragmentAdapter;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.KanjiSet;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.MojiSet;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.model.User;
import com.jishin.ankiji.profile.ProfileActivity;
import com.jishin.ankiji.signin.SigninActivity;
import com.jishin.ankiji.utilities.AppDatabase;
import com.jishin.ankiji.utilities.ConnectivityChangeReceiver;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.NetworkListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeatureActivity extends AppCompatActivity implements NetworkListener{
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
    private AppDatabase db;
    private User mUser;
    private ArrayList<Moji> mMojiList = new ArrayList<>();
    private ArrayList<Kanji> mKanjiList = new ArrayList<>();
    //private FirebaseUser user;
    //LocalDatabase mLocalData = LocalDatabase.getInstance();

    private DatabaseReference mUserRef, mUserListRef, mMojiSetRef, mKanjiSetRef;
    private BroadcastReceiver mHandleMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        mHandleMessageReceiver = new ConnectivityChangeReceiver(this);
        registerReceiver(mHandleMessageReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        getUserID();
        initAppData();
        initReferences();
        new LoadAllUserData().execute();
        new LoadUserTask().execute();
        if(isNetworkAvailable()){
            Toast.makeText(this, R.string.connected, Toast.LENGTH_SHORT).show();
            //loadLocalData();
        }else{
            //Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
        }

        getControls();
        setEvents();
    }

    @Override
    protected void onDestroy() {
        try {
            if (mHandleMessageReceiver != null){
                unregisterReceiver(mHandleMessageReceiver);
            }
        }catch (Exception e){
            e.getMessage();
        }
        super.onDestroy();
    }
    private void initAppData(){
        db = Room.databaseBuilder(FeatureActivity.this,
                AppDatabase.class, Constants.DATABASE_NAME).allowMainThreadQueries().build();

    }
//    private void loadLocalData(){
//
//        if(!mLocalData.hasLocalData()){
//            mLocalData.loadAllData();
//        }
//    }

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
//        if(mLocalData.hasLocalData()){
//            mLocalData.syncData();
//            setProfileInfo();
//            Toast.makeText(this, "Sync data successfully", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void notConnected() {
//        if(mLocalData.hasLocalData()){
//            setProfileInfo();
//        }
//        Toast.makeText(this, getResources().getText(R.string.not_connected), Toast.LENGTH_SHORT).show();
    }

    private void setProfileInfo(){

        if (!TextUtils.isEmpty(mUser.getEmail())){
            Log.d("txtEmail", mUser.getEmail());
            txtEmail.setText(mUser.getEmail());
        }

        if (!TextUtils.isEmpty(mUser.getUsername())){
            Log.d("txtUsername", mUser.getUsername());
            txtUsername.setText(mUser.getUsername());
        }

        if (!TextUtils.isEmpty(mUser.getLinkPhoto())){
            Log.d("imgAvatar", mUser.getLinkPhoto());
            Glide.with(imgAvatar).load(mUser.getLinkPhoto()).into(imgAvatar);
        }
    }
    private void initReferences(){
        mUserRef = mData.createDatabase(Constants.USER_NODE).child(mUserID);
        mUserListRef = mData.createDatabase(Constants.USER_LIST_NODE).child(mUserID);
        mMojiSetRef = mData.createDatabase(Constants.MOJI_SETS_NODE).child(mUserID);
        mKanjiSetRef = mData.createDatabase(Constants.KANJI_SETS_NODE).child(mUserID);
    }
    private class LoadUserTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            mUser = db.userDao().loadUser(mUserID);
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            setProfileInfo();
        }
    }
    private class LoadAllUserData extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            final String id = mUserID;
            if(!id.isEmpty()){
                mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //add User
                        db.userDao().insertUser(dataSnapshot.getValue(User.class));
                        mUser = db.userDao().loadUser(id);
                        mMojiSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    //add moji set
                                    final Set mojiset = ds.getValue(Set.class);
                                    mUserListRef.child(Constants.MOJI).child(ds.getKey())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                                                        mMojiList.add(ds.getValue(Moji.class));
                                                    }
                                                    Log.d(TAG, mMojiList.toString());
                                                    MojiSet newSet = new MojiSet(mojiset.getId(), mojiset.getName(), mojiset.getDatetime()
                                                            , mojiset.getType(), mMojiList);
//                                                    String tmp = toMojiStr(mMojiList);
//                                                    toArrayList(tmp);
                                                    db.mojiSetDao().insertMojiSet(newSet);
                                                    db.mojiSetDao().loadMojiSet();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }
                                mKanjiSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                                            //add kanji set
                                            final Set kanjiset = ds.getValue(Set.class);
                                            mUserListRef.child(Constants.KANJI).child(ds.getKey())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot ds: dataSnapshot.getChildren()){
                                                                mKanjiList.add(ds.getValue(Kanji.class));
                                                            }
                                                            KanjiSet newSet = new KanjiSet(kanjiset.getId(), kanjiset.getName()
                                                                    , kanjiset.getDatetime(), kanjiset.getType(), mKanjiList);
                                                            db.kanjiSetDao().insertKanjiSet(newSet);
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        publishProgress();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            setProfileInfo();
        }
    }
//    public String toMojiStr(ArrayList<Moji> list){
//        Gson gsonBuilder = new GsonBuilder().create();
//        // Convert Java ArrayList into JSON
//        String jsonString = gsonBuilder.toJson(list);
//        return jsonString;
//    }
//    public ArrayList<Moji> toArrayList(String string){
//        ArrayList<Moji> list = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(string);
//            for(int i=0; i<jsonArray.length(); i++) {
//                Moji word = new Moji();
//                word.setId(jsonArray.getJSONObject(i).getString("id"));
//                word.setCachDocHira(jsonArray.getJSONObject(i).getString("cachDocHira"));
//                word.setAmHan(jsonArray.getJSONObject(i).getString("amHan"));
//                word.setTuTiengNhat(jsonArray.getJSONObject(i).getString("tuTiengNhat"));
//                word.setNghiaTiengViet(jsonArray.getJSONObject(i).getString("nghiaTiengViet"));
//                list.add(word);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }
}