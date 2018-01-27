package com.jishin.ankiji.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jishin.ankiji.Feature_Test.TestActivity;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardItemsAdapter;
import com.jishin.ankiji.edit.EditVocabActivity;
import com.jishin.ankiji.explores.TopicKanjiActivity;
import com.jishin.ankiji.interfaces.LoadDataListener;
import com.jishin.ankiji.interfaces.RemoveDataCommunicator;
import com.jishin.ankiji.learn.LearnActivity;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.userlist.CreateVocabActivity;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;
import com.jishin.ankiji.utilities.LocalDatabase;
import com.jishin.ankiji.utilities.MapHelper;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class KanjiFragment extends Fragment implements RemoveDataCommunicator, LoadDataListener {
    private static final String TAG = KanjiFragment.class.getSimpleName();
    private ArrayList<Set> mKanjiSetList = new ArrayList<>();
    private ArrayList<Kanji> mKanjiList = new ArrayList<>();
    private RecyclerView rvRecentlyList;
    private CardItemsAdapter mItemsAdapter;
    public String FRAGMENT_TAG = "KANJI";
    private FloatingActionButton mFABtn, mFABCreate, mFABAdd;
    private boolean isStable = true;
    private DatabaseReference mKanjiSetRef;
    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mSetByUser;
    private String mUserID = "";
    private boolean isScrollDown = false;
    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    private LocalDatabase mLocalData = LocalDatabase.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        if(isNetworkAvailable()){
            //Toast.makeText(getContext(), R.string.connected, Toast.LENGTH_SHORT).show();

        }else{
            //Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT).show();
        }

        //new LoadMojiDataTask().execute();
    }
    //Load local data
    private void loadLocalData(){
        Map myMap = mLocalData.readData(Constants.KANJI_SET_NODE);
        if(myMap != null){
            mKanjiSetList = MapHelper.convertToSet(myMap);
            if(mKanjiSetList!=null)
                mItemsAdapter.setSetList(mKanjiSetList);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kanji, container, false);
        addControl(view);
        initRecycler(view);
        //load Local data
        loadLocalData();
        return view;
    }

    private void initParam() {
        mLocalData.init(getContext(),mUserID, mData, this);
        if(!mData.getUserID().isEmpty()){
            mKanjiSetRef = mData.getDatabase()
                    .child(Constants.KANJI_SET_NODE)
                    .child(mData.getUserID());
            mSetByUser = mData.createDatabase(Constants.SET_BY_USER_NODE).child(mData.getUserID());
            mUserID = mData.getUserID();
        }
        else{
            mKanjiSetRef = mData.getDatabase()
                    .child(Constants.KANJI_SET_NODE)
                    .child(getmUserID());
            mSetByUser = mData.createDatabase(Constants.SET_BY_USER_NODE).child(getmUserID());
            mUserID = getmUserID();
        }
    }


    private void addControl(View view){
        mFABtn = view.findViewById(R.id.fabKanji);
        mFABAdd = view.findViewById(R.id.fabAdd);
        mFABCreate = view.findViewById(R.id.fabCreate);
        mFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mFABCreate.isShown()){
                    mFABAdd.show();
                    mFABCreate.show();  //required
                    float deg = mFABtn.getRotation() + 45F;
                    mFABtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = false;
                }
                else{
                    mFABAdd.hide();
                    mFABCreate.hide();
                    float deg = mFABtn.getRotation() + 45F;
                    mFABtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = true;
                }
            }
        });
        mFABCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                final View dialogView = layoutInflater.inflate(R.layout.dialog_create_list, null);
                final TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
                final EditText edtSetName = dialogView.findViewById(R.id.edtSetName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String setName = edtSetName.getText().toString().trim();
                        if(!setName.isEmpty()){
                            Intent intent = new Intent(getContext(), CreateVocabActivity.class);
                            intent.putExtra(Constants.CREATE, Constants.CREATE_KANJI);
                            intent.putExtra(Constants.NAME, setName);
                            intent.putExtra(Constants.USER_ID, mUserID);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getContext(), "Cannot create a new set.\nSet name field is required", Toast.LENGTH_SHORT).show();
                        }
                        mFABAdd.hide();
                        mFABCreate.hide();
                        mFABtn.setRotation(mFABtn.getRotation()+45F);
                        isStable = true;

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        mFABAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    Intent intent = new Intent(getContext(), TopicKanjiActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecycler(View view) {
        rvRecentlyList = (RecyclerView) view.findViewById(R.id.recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rvRecentlyList.setLayoutManager(layoutManager);

        mItemsAdapter = new CardItemsAdapter(FRAGMENT_TAG, getContext(), this);
        mItemsAdapter.setSetList(this.mKanjiSetList);
        rvRecentlyList.setItemAnimator(new DefaultItemAnimator());
        rvRecentlyList.setAdapter(mItemsAdapter);
        mItemsAdapter.setOnBoomMenuItemClick(new CardItemsAdapter.OnBoomMenuItemClicked() {
            @Override
            public void OnMenuItemClicked(int classIndex, DataTypeEnum dataTypeEnum, Set set) {
                switch (classIndex) {
                    case 0:
                        Intent intent = new Intent(getContext(), LearnActivity.class);
                        intent.putExtra(Constants.SET_BY_USER, set);
                        intent.putExtra(Constants.DATA_TYPE, dataTypeEnum);
                        intent.putExtra(Constants.USER_ID, mUserID);
                        startActivity(intent);
                        break;
                    case 1:
                        new CountItemTask(set).execute();
                        if(mKanjiList.size() >= 5){
                            Intent intentTest= new Intent(getContext(), TestActivity.class);
                            intentTest.putExtra(Constants.SET_BY_USER, set);
                            intentTest.putExtra(Constants.DATA_TYPE, FRAGMENT_TAG);
                            startActivity(intentTest);
                        }

                        break;
                    case 2:

                        break;
                    case 3:
                        Intent editIntent = new Intent(getContext(), EditVocabActivity.class);
                        editIntent.putExtra(Constants.SET_BY_USER, set);
                        editIntent.putExtra(Constants.DATA_TYPE, FRAGMENT_TAG);
                        editIntent.putExtra(Constants.USER_ID, mUserID);
                        startActivity(editIntent);
                        break;
                }
            }
        });
        rvRecentlyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isScrollDown == false){
                    mFABtn.show();
                    if(isStable == false && !mFABCreate.isShown()){
                        mFABtn.setRotation(mFABtn.getRotation()+45F);
                        isStable = true;
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if ((dy > 0 ||dy<0) && mFABtn.isShown()) {
                    if(mFABAdd.isShown()|| mFABCreate.isShown()){
                        mFABAdd.hide();
                        mFABCreate.hide();

                    }
                    mFABtn.hide();
                }
                if(dy > 0){
                    isScrollDown = true;
                }else{
                    isScrollDown = false;
                }
            }
        });
    }


    @Override
    public void removeData(String id, int position) {
        mKanjiSetRef.child(id).removeValue();
        mKanjiSetList.remove(position);
        mSetByUser.child(id).removeValue();
        Map myMap = mLocalData.readAllData();
        Map kanjiMap = mLocalData.readData(Constants.KANJI_SET_NODE);
        Map setByUserMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
        kanjiMap.remove(id);
        setByUserMap.remove(id);
        myMap.put(Constants.KANJI_SET_NODE, kanjiMap);
        myMap.put(Constants.SET_BY_USER_NODE, setByUserMap);
        String str = new Gson().toJson(myMap);
        mLocalData.writeToFile(Constants.DATA_FILE, str, getContext());
        mItemsAdapter.notifyDataSetChanged();
    }


    @Override
    public void loadData() {
        loadLocalData();
    }

    //Load kanji set
    public class LoadKanjiDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mKanjiSetRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mKanjiSetList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        mKanjiSetList.add(ds.getValue(Set.class));
                        Log.d(TAG, ds.getKey()+"/"+String.valueOf(ds.getValue()));
                    }
                    publishProgress();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mItemsAdapter.notifyDataSetChanged();
        }
    }

    //Load moji list
    public class CountItemTask extends AsyncTask<Void, Void, Void>{
        Set mSet = new Set();
        public CountItemTask(Set set){
            this.mSet = set;
        }
        @Override
        protected Void doInBackground(Void... voids) {

            mKanjiList.clear();
            Map map = mLocalData.readData(Constants.SET_BY_USER_NODE);
            mKanjiList = MapHelper.convertToKanji(map, mSet.getId());
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if(mKanjiList.size() >= 5){
                Intent intentTest = new Intent(getContext(), TestActivity.class);
                intentTest.putExtra(Constants.SET_BY_USER, mKanjiList);
                intentTest.putExtra(Constants.DATA_TYPE, FRAGMENT_TAG);
                startActivity(intentTest);
            }
            else{
                Toast.makeText(getContext(), "Cannot create test.\nLess than 5 items in the set.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    private void getKanjiSet() {
//        mKanjiSetRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                showData(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void showData(DataSnapshot dataSnapshot) {
//        mKanjiSetList.clear();
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            mKanjiSetList.add(ds.getValue(Set.class));
//            Log.d(TAG, ds.getKey()+"/"+String.valueOf(ds.getValue()));
//        }
//        mItemsAdapter.notifyDataSetChanged();
//    }
}
