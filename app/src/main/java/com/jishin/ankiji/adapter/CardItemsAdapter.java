package com.jishin.ankiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.animation.BuilderManager;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Set;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class CardItemsAdapter extends RecyclerView.Adapter<CardItemsAdapter.ItemViewHolder> {

    private final static String TAG = CardFragmentPagerAdapter.class.getSimpleName();
    private ArrayList<Set> mSetList = new ArrayList<>();
    private String FRAGMENT_TAG;
    private OnBoomMenuItemClicked mListener;


    public CardItemsAdapter(String FRAGMENT_TAG) {
        this.FRAGMENT_TAG = FRAGMENT_TAG;
    }

    public void setOnBoomMenuItemClick(OnBoomMenuItemClicked mListener) {
        this.mListener = mListener;
    }

    public void setSetList(ArrayList<Set> mSetList) {
        this.mSetList = mSetList;
    }

    @Override
    public CardItemsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_recycler_item,
                parent,
                false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardItemsAdapter.ItemViewHolder holder, final int position) {

        if (FRAGMENT_TAG.equals("RECENTLY")) {
            holder.tvTitle.setText("MARUGOTO");
            holder.tvItemCount.setText("10 items");
            holder.dataType = DataTypeEnum.Kanji;
        }

        if (FRAGMENT_TAG.equals("MOJI")) {
            if (this.mSetList.size() != 0){
                holder.set = this.mSetList.get(position);
                holder.tvTitle.setText(holder.set.getName());
                holder.tvItemCount.setText(holder.set.getDatetime());
                holder.dataType = DataTypeEnum.Moji;
            }
        }

        if (FRAGMENT_TAG.equals("KANJI")) {
            if (this.mSetList.size() != 0){
                holder.set = this.mSetList.get(position);
                holder.tvTitle.setText(holder.set.getName());
                holder.tvItemCount.setText(holder.set.getDatetime());
                holder.dataType = DataTypeEnum.Kanji;
            }
        }

        holder.bmb.clearBuilders();
        int stringIndex = 0;
        for (int i = 0; i < holder.bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            switch (i){
                case 0:
                    stringIndex = R.string.text_outside_button_buider_learn;
                    break;
                case 1:
                    stringIndex = R.string.text_outside_button_buider_test;
                    break;
                case 2:
                    stringIndex = R.string.text_outside_button_buider_chart;
                    break;
                case 3:
                    stringIndex = R.string.text_outside_button_buider_edit;
                    break;
            }
            addBuilder(holder, stringIndex);
        }
    }

    void addBuilder(final ItemViewHolder viewHolder, int stringIndex) {
        viewHolder.bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(BuilderManager.getImageResource())
                .normalTextRes(stringIndex)
                .textSize(14)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        mListener.OnMenuItemClicked(index, viewHolder.dataType, viewHolder.set);
                    }
                }));
    }


    @Override
    public int getItemCount() {
        if (FRAGMENT_TAG.equalsIgnoreCase("RECENTLY")) {
            return 10;
        }
        return this.mSetList.size();

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        DataTypeEnum dataType;
        TextView tvTitle;
        TextView tvItemCount;
        ImageButton btnDeleteItem;
        BoomMenuButton bmb;
        Set set;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvItemCount = itemView.findViewById(R.id.tv_item_count);
            btnDeleteItem = itemView.findViewById(R.id.btn_delete_item);
            bmb = itemView.findViewById(R.id.bmb);
        }
    }

    public interface OnBoomMenuItemClicked{
        void OnMenuItemClicked(int classIndex, DataTypeEnum dataTypeEnum, Set set);
    }

}
