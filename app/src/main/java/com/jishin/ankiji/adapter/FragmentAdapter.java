package com.jishin.ankiji.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jishin.ankiji.features.KanjiFragment;
import com.jishin.ankiji.features.MojiFragment;
import com.jishin.ankiji.view.fragment.RecentlyFragment;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class FragmentAdapter extends FragmentPagerAdapter{

    public final static int FRAGMENT_COUNT = 3;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RecentlyFragment();
            case 1:
                return new MojiFragment();
            case 2:
                return new KanjiFragment();
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
