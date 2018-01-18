package com.jishin.ankiji.model;

/**
 * Created by trungnguyeen on 1/17/18.
 */

public class Item {

    private String mFontText;
    private String mBackText;


    public Item(String mFontText, String mBackText) {
        this.mFontText = mFontText;
        this.mBackText = mBackText;
    }

    public String getmFontText() {
        return mFontText;
    }

    public void setmFontText(String mFontText) {
        this.mFontText = mFontText;
    }

    public String getmBackText() {
        return mBackText;
    }

    public void setmBackText(String mBackText) {
        this.mBackText = mBackText;
    }


}
