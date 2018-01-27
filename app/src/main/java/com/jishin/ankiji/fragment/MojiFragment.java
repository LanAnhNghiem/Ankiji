package com.jishin.ankiji.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.CHART.ChartActivity;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardItemsAdapter;
import com.jishin.ankiji.explores.TopicMojiActivity;
import com.jishin.ankiji.feature_Test.TestActivity;
import com.jishin.ankiji.interfaces.RemoveDataCommunicator;
import com.jishin.ankiji.learn.LearnActivity;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.userlist.CreateVocabActivity;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class MojiFragment extends Fragment implements RemoveDataCommunicator{

    private static final String TAG = MojiFragment.class.getSimpleName();
    private RecyclerView rvRecentlyList;
    private CardItemsAdapter mItemsAdapter;
    private ArrayList<Set> mMojiSetList = new ArrayList<>();
    private ArrayList<Moji> mMojiList = new ArrayList<>();
    private FloatingActionButton mFABtn, mFABCreate, mFABAdd;
    public String FRAGMENT_TAG = "MOJI";
    private boolean isStable = true;
    private DatabaseReference mMojiSetRef;
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

    private FirebaseUser user;
    private long correctAnswer = 0;
    private long testTimes = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        new LoadMojiDataTask().execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moji, container, false);
        addControl(view);
        initRecycler(view);
        return view;
    }

    private void initParam() {
        if(!mData.getUserID().isEmpty()){
            mMojiSetRef = mData.getDatabase()
                    .child(Constants.MOJI_SET_NODE)
                    .child(mData.getUserID());
            mSetByUser = mData.createDatabase(Constants.SET_BY_USER_NODE).child(mData.getUserID());
        }
        else{
            mMojiSetRef = mData.getDatabase()
                    .child(Constants.MOJI_SET_NODE)
                    .child(getmUserID());
            mSetByUser = mData.createDatabase(Constants.SET_BY_USER_NODE).child(getmUserID());
        }
    }

    private void addControl(View view){
        user = FirebaseAuth.getInstance().getCurrentUser();
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
                final View dialogView = layoutInflater.inflate(R.layout.dialog_create_list, null);
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
                            intent.putExtra("create", Constants.CREATE_MOJI);
                            intent.putExtra("name", setName);
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
                Intent intent = new Intent(getContext(), TopicMojiActivity.class);
                startActivity(intent);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecycler(View view) {
        rvRecentlyList = (RecyclerView) view.findViewById(R.id.recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rvRecentlyList.setLayoutManager(layoutManager);

        mItemsAdapter = new CardItemsAdapter(FRAGMENT_TAG, getContext(), this);
        mItemsAdapter.setSetList(this.mMojiSetList);
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
                        startActivity(intent);
                        break;
                    case 1:
                        new CountItemTask(set).execute();
                        break;
                    case 2:
                        new LoadNodeChart(set).execute();
                        break;
                    case 3:

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

    public class LoadMojiDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mMojiSetRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mMojiSetList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        mMojiSetList.add(ds.getValue(Set.class));
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
    public class CountItemTask extends AsyncTask<Void, Void, Void>{
        Set mSet = new Set();
        public CountItemTask(Set set){
            this.mSet = set;
            Log.d(TAG, this.mSet.toString());
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mSetByUser.child(mSet.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mMojiList.clear();
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        mMojiList.add(data.getValue(Moji.class));
                        Log.d(TAG, data.getKey()+" "+data.getValue());
                    }
                    onProgressUpdate();
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
            if(mMojiList.size() >= 5){
                Intent intentTest = new Intent(getContext(), TestActivity.class);
                intentTest.putExtra(Constants.SET_BY_USER, mMojiList);
                intentTest.putExtra(Constants.DATA_TYPE, FRAGMENT_TAG);
                if (user != null) {
                    intentTest.putExtra(Constants.USER_ID, user.getUid());
                    intentTest.putExtra(Constants.KANJI_SET_NODE, mSet.getId());
                }
                startActivity(intentTest);
            }
            else{
                Toast.makeText(getContext(), "Cannot create test.\nLess than 5 items in the set.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class LoadNodeChart extends AsyncTask<Void, Void, Void> {
        Set mSet = new Set();
        DatabaseReference chartRef = FirebaseDatabase.getInstance().getReference().child(Constants.CHART)
                .child(user.getUid()).child(mSet.getId());

        public LoadNodeChart(Set set){
            this.mSet = set;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            Log.d("USER_ID", user.getUid());
            Log.d("SET_ID_MojiFrag", mSet.getId());
            chartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(Constants.CORRECT_ANSWER).getValue() == null){
                        correctAnswer = 0;
                        testTimes = 0;
                    }else{
                        correctAnswer = dataSnapshot.child(Constants.CORRECT_ANSWER).getValue(Long.class);
                        testTimes = dataSnapshot.child(Constants.TEST_TIMES).getValue(Long.class);
                    }
                    onProgressUpdate();
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
            Intent intentChart = new Intent(getContext(), ChartActivity.class);
            if (user != null) {
                intentChart.putExtra(Constants.USER_ID, user.getUid());
                Log.d("Set_ID", mSet.getId());
                intentChart.putExtra(Constants.KANJI_SET_NODE, mSet.getId());
                intentChart.putExtra("SIZE", mMojiList.size());
                intentChart.putExtra(Constants.CORRECT_ANSWER, correctAnswer);
                intentChart.putExtra(Constants.TEST_TIMES, testTimes);
            }
            startActivity(intentChart);
        }
    }
}
