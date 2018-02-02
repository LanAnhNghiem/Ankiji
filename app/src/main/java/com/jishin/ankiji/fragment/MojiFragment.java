package com.jishin.ankiji.fragment;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.MojiSetAdapter;
import com.jishin.ankiji.chart.ChartActivity;
import com.jishin.ankiji.edit.EditVocabActivity;
import com.jishin.ankiji.explores.TopicMojiActivity;
import com.jishin.ankiji.feature_test.TestActivity;
import com.jishin.ankiji.interfaces.RemoveDataCommunicator;
import com.jishin.ankiji.learn.LearnActivity;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.MojiSet;
import com.jishin.ankiji.create_data.CreateVocabActivity;
import com.jishin.ankiji.utilities.AppDatabase;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class MojiFragment extends Fragment implements RemoveDataCommunicator{

    private static final String TAG = MojiFragment.class.getSimpleName();
    private RecyclerView rvRecentlyList;
    private MojiSetAdapter mItemsAdapter;
    private List<MojiSet> mMojiSetList = new ArrayList<>();
    private ArrayList<Moji> mMojiList = new ArrayList<>();
    private FloatingActionButton mFABtn, mFABCreate, mFABAdd;
    public String FRAGMENT_TAG = "MOJI";
    private boolean isStable = true;
    private DatabaseReference mMojiSetRef;
    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mSetByUser;
    private String mUserID = "";
    private boolean isScrollDown = false;
    private FirebaseUser user;
    public String getmUserID() {
        return mUserID;
    }
    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }
    private AppDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppData();
        initParam();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moji, container, false);
        addControl(view);
        new LoadMojiSetTask().execute();
        return view;
    }
    private void initAppData(){
        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, Constants.DATABASE_NAME).allowMainThreadQueries().build();
    }
    private void initParam() {
        if(!mData.getUserID().isEmpty()){
            mMojiSetRef = mData.getDatabase()
                    .child(Constants.MOJI_SET_NODE)
                    .child(mData.getUserID());
            mSetByUser = mData.createDatabase(Constants.SET_BY_USER_NODE).child(mData.getUserID());
            mUserID = mData.getUserID();
        }
        else{
            mMojiSetRef = mData.getDatabase()
                    .child(Constants.MOJI_SET_NODE)
                    .child(getmUserID());
            mSetByUser = mData.createDatabase(Constants.SET_BY_USER_NODE).child(getmUserID());
            mUserID = getmUserID();
        }
    }

    private void addControl(View view){
        user = FirebaseAuth.getInstance().getCurrentUser();
        rvRecentlyList = (RecyclerView) view.findViewById(R.id.recycle_view);
        mFABtn = view.findViewById(R.id.fabMoji);
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
                final View dialogView = layoutInflater.inflate(R.layout.dialog_create_set, null);
                final TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
                final EditText edtSetName = dialogView.findViewById(R.id.edtSetName);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String setName = edtSetName.getText().toString().trim();
                        if(!setName.isEmpty()){
                            Intent intent = new Intent(getContext(), CreateVocabActivity.class);
                            intent.putExtra(Constants.CREATE, Constants.CREATE_MOJI);
                            intent.putExtra(Constants.NAME, setName);
                            intent.putExtra(Constants.USER_ID, mUserID);
                            startActivity(intent);
                        }
                        else{
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
                    Intent intent = new Intent(getContext(), TopicMojiActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rvRecentlyList.setLayoutManager(layoutManager);
        mItemsAdapter = new MojiSetAdapter(mMojiSetList, getContext(), this);
        rvRecentlyList.setItemAnimator(new DefaultItemAnimator());
        rvRecentlyList.setAdapter(mItemsAdapter);
        mItemsAdapter.setOnBoomMenuItemClick(new MojiSetAdapter.OnBoomMenuItemClicked() {
            @Override
            public void OnMenuItemClicked(int classIndex) {
                String setID = mMojiSetList.get(classIndex).getId();
                switch (classIndex) {
                    case 0:
                        Intent intent = new Intent(getContext(), LearnActivity.class);
                        intent.putExtra(Constants.INDEX, classIndex);
                        intent.putExtra(Constants.DATA_TYPE, DataTypeEnum.Moji);
                        intent.putExtra(Constants.USER_ID, mUserID);
                        startActivity(intent);
                        break;
                    case 1:
                        new CountItemTask(classIndex).execute();

                        break;
                    case 2:
                        new LoadDataForChart(mData.getUserID(), setID, classIndex).execute();

                        break;
                    case 3:
                        Intent editIntent = new Intent(getContext(), EditVocabActivity.class);
                        editIntent.putExtra(Constants.SET_BY_USER, setID);
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
    public void removeData(final String id, int position) {
        mMojiSetRef.child(id).removeValue();
        mMojiSetList.remove(position);
        mSetByUser.child(id).removeValue();
        mItemsAdapter.notifyDataSetChanged();
    }

    //Load moji set
    public class LoadMojiSetTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mMojiSetList = db.mojiSetDao().loadMojiSet();
            publishProgress();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            initRecycler();
        }
    }
    //Load moji list
    class CountItemTask extends AsyncTask<Void, Void, Void>{
        private int index ;
        public CountItemTask(int index){
            this.index = index;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mMojiList = mMojiSetList.get(index).getList();
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if(mMojiList.size() >= 5){
                Intent intentTest = new Intent(getContext(), TestActivity.class);
                intentTest.putExtra(Constants.USER_LIST_NODE, mMojiList);
                intentTest.putExtra(Constants.DATA_TYPE, FRAGMENT_TAG);
                if (user != null) {
                    intentTest.putExtra(Constants.USER_ID, user.getUid());
                    intentTest.putExtra(Constants.KANJI_SET_NODE, mMojiSetList.get(index).getId());
                }
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


    public class LoadDataForChart extends AsyncTask<Void, Void, Void>{

        String userId;
        String setId;
        DatabaseReference chartRef;
        String correctAnswer;
        String testTimes;
        int index;
        public LoadDataForChart(String userId, String setId, int index){
            this.userId = userId;
            this.setId = setId;
            this.index = index;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataRef();
            Log.d("Chart_Ref", chartRef.toString());
            chartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    correctAnswer = (String) dataSnapshot.child(Constants.CORRECT_ANSWER).getValue();
                    testTimes = (String) dataSnapshot.child(Constants.TEST_TIMES).getValue();
                    onProgressUpdate();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mMojiList.clear();
            mMojiList = mMojiSetList.get(index).getList();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (testTimes != null || testTimes == "0"){
                Log.d(TAG, "onDataChange: Correct Answer: " + correctAnswer);
                Log.d(TAG, "onDataChange: TestTimes: " + testTimes);
                Log.d(TAG, "onDataChange: SetID: " + this.setId);
                Log.d(TAG, "onDataChange: UserID: " + this.userId);
                Log.d(TAG, "onDataChange: ListSize: " + mMojiList.size());
                Intent chartIntent = new Intent(getContext(), ChartActivity.class);
                chartIntent.putExtra(Constants.USER_ID, this.userId);
                chartIntent.putExtra(Constants.SET_BY_USER,this.setId);
                chartIntent.putExtra("SIZE", mMojiList.size());
                chartIntent.putExtra(Constants.CORRECT_ANSWER, correctAnswer);
                chartIntent.putExtra(Constants.TEST_TIMES, testTimes);
                startActivity(chartIntent);
            }
            else {
                Toast.makeText(getContext(), "Test times: 0", Toast.LENGTH_SHORT).show();
            }
        }

        public void dataRef(){
            if(!mData.getUserID().isEmpty()){
                chartRef = mData.getDatabase()
                        .child(Constants.CHART)
                        .child(mData.getUserID())
                        .child(this.setId);
            }
        }
    }
}