package com.jishin.ankiji.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jishin.ankiji.fragment.KanjiFragment;
import com.jishin.ankiji.fragment.MojiFragment;
import com.jishin.ankiji.fragment.RecentlyFragment;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class FragmentAdapter extends FragmentPagerAdapter{

    public final static int FRAGMENT_COUNT = 3;
    private String mUserID = "";
    private Context mContext;

    public FragmentAdapter(FragmentManager fm, String userID) {
        super(fm);
        this.mUserID = userID;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RecentlyFragment recently = new RecentlyFragment();
                recently.setmUserID(mUserID);
                return recently;
            case 1:
                MojiFragment moji = new MojiFragment();
                moji.setmUserID(mUserID);
                return moji;
            case 2:
                KanjiFragment kanji = new KanjiFragment();
                kanji.setmUserID(mUserID);
                return kanji;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Recently";
            case 1:
                return "Moji";
            case 2:
                return "Kanji";
            default:
                return "";
        }
    }
}
