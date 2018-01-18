package com.jishin.ankiji.view.fragment;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.TopicAdapter;

import java.util.ArrayList;

/**
 * Created by trungnguyeen on 12/27/17.
 */

@SuppressLint("ValidFragment")
public class KanjiTopicFragment extends Fragment {

    public static final String TAG = KanjiTopicFragment.class.getSimpleName();

    private RecyclerView rvKanjiTopicList;
    private TopicAdapter mTopicAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mKanjiRef;
    ArrayList<String> topicList = new ArrayList();
    public String FRAGMENT_TAG = "KANJI";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kanji_explores, container, false);
        Log.d(TAG, "onCreate: test");
        //Declare database references
        mDatabase = FirebaseDatabase.getInstance();
        mKanjiRef = mDatabase.getReference().child("data_kanji");
        //addControl(view);
        initRecycler(view);
        Log.d(TAG, "onCreate: test2");
        return view;
    }

    private void initRecycler(View view) {
        rvKanjiTopicList = (RecyclerView) view.findViewById(R.id.kanji_topic_recycler);

        getKanjiTopic(view);

    }

    private void getKanjiTopic(View view) {

        mKanjiRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showTopic(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showTopic(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            topicList.add(ds.getKey());
            TopicAdapter topicAdapter = new TopicAdapter("Kanji");
            topicAdapter.setTopic(topicList);
            Log.d(TAG, "getKanjiTopic: " + topicList);
            mTopicAdapter = new TopicAdapter(FRAGMENT_TAG);
            mTopicAdapter.setTopic(topicList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            rvKanjiTopicList.setLayoutManager(layoutManager);
            rvKanjiTopicList.setItemAnimator(new DefaultItemAnimator());

            rvKanjiTopicList.setAdapter(mTopicAdapter);
        }
    }
}
