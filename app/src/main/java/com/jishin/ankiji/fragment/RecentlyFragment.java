package com.jishin.ankiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardItemsAdapter;
import com.jishin.ankiji.interfaces.RemoveDataCommunicator;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by trungnguyeen on 12/27/17.
 */
public class RecentlyFragment extends Fragment implements RemoveDataCommunicator {

    private final static String TAG = RecentlyFragment.class.getSimpleName();
    private RecyclerView rvRecentlyList;
    private CardItemsAdapter mItemsAdapter;
    public String FRAGMENT_TAG = "RECENTLY";
    private String mUserID = "";
//    private TopicAdapter topicAdapter;
    String[] currentDay;
    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mDateRef = mData.getDatabase().child("DateSet");
    String userID = "";
    private ArrayList<Set> topicList = new ArrayList<>();

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recently, container, false);
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
        currentDay = String.valueOf(Calendar.getInstance().getTime()).split("\\s");
        Log.d(TAG, "onCreateView: currentDay: " + currentDay[0]);
        userID = mData.getUserID();
        if (userID.isEmpty()) {
            userID = getmUserID();
        }
        initRecycler(view);

        return view;
    }

    private void initRecycler(View view) {
        rvRecentlyList = (RecyclerView) view.findViewById(R.id.lv_recently);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rvRecentlyList.setLayoutManager(layoutManager);
        mItemsAdapter = new CardItemsAdapter(FRAGMENT_TAG, getContext(), this);
        mItemsAdapter.setOnBoomMenuItemClick(new CardItemsAdapter.OnBoomMenuItemClicked() {
            @Override
            public void OnMenuItemClicked(int classIndex, DataTypeEnum dataTypeEnum, Set set) {
                switch (classIndex) {
                    case 0: {
//                        Intent intent = new Intent(getContext(), LearnActivity.class);
//                        intent.putExtra(Constants.DATA_TYPE, dataTypeEnum);
//                        intent.putExtra(Constants.SET_BY_USER, set);
//                        startActivity(intent);
                        break;
                    }

                    case 1:
                        //TODO startActivity Test
                        break;
                    case 2:
                        //TODO Start Activity Chart
                        break;
                    case 3:
                        //TODO Start Activity Edit Item
                        break;
                }
            }
        });
        getData();
//        topicAdapter = new TopicAdapter("KANJI");
        mItemsAdapter = new CardItemsAdapter(FRAGMENT_TAG, getContext(), this);
        mItemsAdapter.setSetList(this.topicList);
        rvRecentlyList.setItemAnimator(new DefaultItemAnimator());
        rvRecentlyList.setAdapter(mItemsAdapter);
    }

    @Override
    public void removeData(String id, int position) {

    }

    @Override
    public void onPause() {
        super.onPause();
//        getData();
////        topicAdapter = new TopicAdapter("KANJI");
////        topicAdapter.setTopic(topicList);
//        rvRecentlyList.setItemAnimator(new DefaultItemAnimator());
//        rvRecentlyList.setAdapter(mItemsAdapter);
//        //rvRecentlyList.setAdapter(topicAdapter);
    }

    public void showData(DataSnapshot dataSnapshot) {
        topicList.clear();
        if (dataSnapshot != null) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                String[] date = ds.getValue(Set.class).getDatetime().split("\\s");
                Log.d(TAG, "showData: currentDay: "+date[0]);
                if (date[0].equals(currentDay[0]) && date[1].equals(currentDay[1]) && date[2].equals(currentDay[2])) {
                    topicList.add(ds.getValue(Set.class));
                    Log.d(TAG, "showData: topicList "+topicList);
                }
            }
        }


         mItemsAdapter.notifyDataSetChanged();
    }

    private void getData() {
        if (mDateRef != null) {
            mDateRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: dsValue: " + dataSnapshot.getValue(Set.class));
                    showData(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
