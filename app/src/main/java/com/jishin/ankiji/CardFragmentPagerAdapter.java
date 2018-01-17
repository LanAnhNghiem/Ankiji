package com.jishin.ankiji;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jishin.ankiji.adapter.FragmentViewPagerAdapter;
import com.jishin.ankiji.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungnguyeen on 1/14/18.
 */
public class CardFragmentPagerAdapter extends FragmentViewPagerAdapter {

    private List<CardFragment> mFragments = new ArrayList<>();;
    private ArrayList<Item> mItemList;
    public CardFragmentPagerAdapter(FragmentManager fm, ArrayList<Item> itemList) {
        super(fm);
        this.mItemList = itemList;

        for(int i = 0; i< this.mItemList.size(); i++){
            CardFragment cardFrag = new CardFragment();
            cardFrag.setItem(this.mItemList.get(i));
            cardFrag.setINDEX(i + 1);
            addCardFragment(cardFrag);
        }
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public void addCardFragment(CardFragment fragment) {
        mFragments.add(fragment);
    }

    public void swapData() {
        for (CardFragment cardFrag: this.mFragments) {
            cardFrag.swapContentCardView();
        }
    }
}
