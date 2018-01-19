package com.jishin.ankiji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jishin.ankiji.model.Kanji;
import com.wajahatkarim3.easyflipview.EasyFlipView;

/**
 * Created by trungnguyeen on 1/14/18.
 */
public class CardFragment extends Fragment{

    private Kanji mKanji;
    private CardView mFontCardView;
    private CardView mBackCardView;
    private EasyFlipView mEasyFlipView;

    private TextView tvFrontCard;
    private TextView tvTopBackCard;
    private TextView tvBottomBackCard;
    private float mCardElevationValue;

    private final static String TAG = CardFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_item, container, false);

        initControls(view);
        setEvents();
        return view;
    }

    public void setItem(Kanji mKanji) {
        this.mKanji = mKanji;
    }

    public void initControls(View view) {
        mEasyFlipView = view.findViewById(R.id.flip_view);
        mFontCardView = view.findViewById(R.id.font_card_view);
        mBackCardView = view.findViewById(R.id.back_card_view);

        mCardElevationValue = mFontCardView.getCardElevation();

        tvFrontCard = view.findViewById(R.id.tv_kanji);
        tvTopBackCard = view.findViewById(R.id.tv_am_han);
        tvBottomBackCard = view.findViewById(R.id.tv_tu_vung);

        tvFrontCard.setText(this.mKanji.getKanji());
        tvTopBackCard.setText(this.mKanji.getAmhan());
        tvBottomBackCard.setText(this.mKanji.getTuvung());

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
