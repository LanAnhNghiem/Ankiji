package com.jishin.ankiji.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jishin.ankiji.features.MojiFragment;
import com.jishin.ankiji.view.fragment.KanjiTopicFragment;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class DataFragmentAdapter extends FragmentPagerAdapter{

    public final static int FRAGMENT_COUNT = 2;

    public DataFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MojiFragment();
            case 1:
                return new KanjiTopicFragment();
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
                return "Moji";
            case 1:
                return "Kanji";
            default:
                return "";
        }
    }
}
