package com.jishin.ankiji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardItemsAdapter;
import com.jishin.ankiji.learn.LearnActivity;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.utilities.Constants;

/**
 * Created by trungnguyeen on 12/27/17.
 */
public class RecentlyFragment extends Fragment {

    private final static String TAG = RecentlyFragment.class.getSimpleName();
    private RecyclerView rvRecentlyList;
    private CardItemsAdapter mItemsAdapter;
    public String FRAGMENT_TAG = "RECENTLY";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recently, container, false);

        initRecycler(view);

        return view;
    }

    private void initRecycler(View view) {
        rvRecentlyList = (RecyclerView) view.findViewById(R.id.lv_recently);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rvRecentlyList.setLayoutManager(layoutManager);
        mItemsAdapter = new CardItemsAdapter(FRAGMENT_TAG);
        mItemsAdapter.setOnBoomMenuItemClick(new CardItemsAdapter.OnBoomMenuItemClicked() {
            @Override
            public void OnMenuItemClicked(int classIndex, DataTypeEnum dataTypeEnum) {
                switch (classIndex) {
                    case 0:{
                        Intent intent = new Intent(getContext(), LearnActivity.class);
                        intent.putExtra(Constants.DATA_TYPE, dataTypeEnum);
                        startActivity(intent);
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
        rvRecentlyList.setItemAnimator(new DefaultItemAnimator());
        rvRecentlyList.setAdapter(mItemsAdapter);


    }
}
