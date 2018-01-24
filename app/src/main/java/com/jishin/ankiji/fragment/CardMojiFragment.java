package com.jishin.ankiji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Moji;
import com.wajahatkarim3.easyflipview.EasyFlipView;

/**
 * Created by trungnguyeen on 1/14/18.
 */

public class CardMojiFragment extends Fragment {
    private Moji mMoji;
    private CardView mFontCardView;
    private CardView mBackCardView;
    private EasyFlipView mEasyFlipView;

    private TextView tvMoji;
    private TextView tvAmHan;
    private TextView tvHiragana;
    private TextView tvNghiaTV;
    private float mCardElevationValue;

    private final static String TAG = CardMojiFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_moji_item, container, false);

        initControls(view);
        setEvents();
        return view;
    }

    public void setItem(Moji mMoji) {
        this.mMoji = mMoji;
    }


    public void initControls(View view) {
        mEasyFlipView = view.findViewById(R.id.flip_view);
        mFontCardView = view.findViewById(R.id.font_card_view);
        mBackCardView = view.findViewById(R.id.back_card_view);

        mCardElevationValue = mFontCardView.getCardElevation();

        tvMoji = view.findViewById(R.id.tv_moji);
        tvAmHan = view.findViewById(R.id.tv_am_han);
        tvHiragana = view.findViewById(R.id.tv_hiragana);
        tvNghiaTV = view.findViewById(R.id.tv_nghia);

        tvMoji.setText(this.mMoji.getTuTiengNhat());
        tvAmHan.setText(this.mMoji.getAmHan());
        tvHiragana.setText(this.mMoji.getCachDocHira());
        tvNghiaTV.setText(this.mMoji.getNghiaTiengViet());

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
}
