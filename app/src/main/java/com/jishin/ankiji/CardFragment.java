package com.jishin.ankiji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jishin.ankiji.model.Item;
import com.jishin.ankiji.view.activity.LearnActivity;
import com.wajahatkarim3.easyflipview.EasyFlipView;

/**
 * Created by trungnguyeen on 1/14/18.
 */
public class CardFragment extends Fragment{

    private Item mItem;
    private CardView mFontCardView;
    private CardView mBackCardView;
    private EasyFlipView mEasyFlipView;
    private TextView mFontViewText;
    private TextView mBackViewText;
    private float mCardElevationValue;

    private final static String TAG = CardFragment.class.getSimpleName();
    private int INDEX;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_item, container, false);

        initControls(view);
        setEvents();
        Log.i(TAG, "onCreateView: " + INDEX);
        return view;
    }

    public void setItem(Item mItem) {
        this.mItem = mItem;
    }

    public void setINDEX(int INDEX) {
        this.INDEX = INDEX;
    }

    public void initControls(View view) {
        mEasyFlipView = view.findViewById(R.id.flip_view);
        mFontCardView = view.findViewById(R.id.font_card_view);
        mBackCardView = view.findViewById(R.id.back_card_view);
        mCardElevationValue = mFontCardView.getCardElevation();
        mFontViewText = view.findViewById(R.id.tv_font);
        mBackViewText = view.findViewById(R.id.tv_back);

        if (LearnActivity.isFont()){
            mFontViewText.setText(this.mItem.getmFontText());
            mBackViewText.setText(this.mItem.getmBackText());
        }
        else{
            mFontViewText.setText(this.mItem.getmBackText());
            mBackViewText.setText(this.mItem.getmFontText());
        }


    }


    public void setEvents() {

        mBackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animRemoveElevationAndFlip();
            }
        });

        mFontCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animRemoveElevationAndFlip();
            }
        });

        mEasyFlipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                mBackCardView.setCardElevation(mCardElevationValue);
                mFontCardView.setCardElevation(mCardElevationValue);
            }
        });
    }

    public void animRemoveElevationAndFlip() {
        mFontCardView.setCardElevation(0f);
        mBackCardView.setCardElevation(0f);
        mEasyFlipView.flipTheView();
    }

    public void swapContentCardView() {
        if (LearnActivity.isFont()) {
            this.mFontViewText.setText(this.mItem.getmBackText());
            this.mBackViewText.setText(this.mItem.getmFontText());
        } else {
            this.mBackViewText.setText(this.mItem.getmBackText());
            this.mFontViewText.setText(this.mItem.getmFontText());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: " + INDEX);
    }
}
