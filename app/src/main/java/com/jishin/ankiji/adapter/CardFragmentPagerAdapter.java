package com.jishin.ankiji.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jishin.ankiji.fragment.CardKanjiFragment;
import com.jishin.ankiji.fragment.CardMojiFragment;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungnguyeen on 1/14/18.
 */

public class CardFragmentPagerAdapter extends FragmentViewPagerAdapter {
    private List<?> mFragments = new ArrayList<>();
    private ArrayList<?> contentList;
    private DataTypeEnum dataTypeEnum;
    public CardFragmentPagerAdapter(FragmentManager fm, ArrayList<?> contentList, DataTypeEnum dataTypeEnum) {
        super(fm);
        this.contentList = contentList;
        this.dataTypeEnum = dataTypeEnum;

        createCardList();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragments.get(position);
    }

    public void createCardList(){
        if(dataTypeEnum == DataTypeEnum.Moji){
            ArrayList<CardMojiFragment> fragments = new ArrayList<>();
            for(int i = 0; i< this.contentList.size(); i++){
                CardMojiFragment cardFrag = new CardMojiFragment();
                cardFrag.setItem((Moji) this.contentList.get(i));
                fragments.add(cardFrag);
            }
            this.mFragments = fragments;
        }
        else{
            ArrayList<CardKanjiFragment> fragments = new ArrayList<>();
            for(int i = 0; i< this.contentList.size(); i++){
                CardKanjiFragment cardFrag = new CardKanjiFragment();
                cardFrag.setItem((Kanji) this.contentList.get(i));
                fragments.add(cardFrag);
            }
            this.mFragments = fragments;
        }

    }
}
