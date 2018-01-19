package com.jishin.ankiji;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jishin.ankiji.adapter.FragmentViewPagerAdapter;
import com.jishin.ankiji.model.Kanji;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungnguyeen on 1/14/18.
 */
public class CardFragmentPagerAdapter extends FragmentViewPagerAdapter {

    private List<CardFragment> mFragments = new ArrayList<>();;
    private ArrayList<Kanji> mKanjiList;
    public CardFragmentPagerAdapter(FragmentManager fm, ArrayList<Kanji> kanjiList) {
        super(fm);
        this.mKanjiList = kanjiList;

        for(int i = 0; i< this.mKanjiList.size(); i++){
            CardFragment cardFrag = new CardFragment();
            cardFrag.setItem(this.mKanjiList.get(i));
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
}
