package com.jishin.ankiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jishin.ankiji.R;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class CardItemsAdapter extends RecyclerView.Adapter<CardItemsAdapter.ItemViewHolder> {

    private String FRAGMENT_TAG;

    public CardItemsAdapter(String FRAGMENT_TAG) {
        this.FRAGMENT_TAG = FRAGMENT_TAG;
    }

    @Override
    public CardItemsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_recycler_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardItemsAdapter.ItemViewHolder holder, int position) {

        if (FRAGMENT_TAG == "RECENTLY") {
            holder.tvTitle.setText("MARUGOTO");
            holder.tvItemCount.setText("10 items");
        }

        if (FRAGMENT_TAG == "MOJI") {
            holder.tvTitle.setText("MOJI");
            holder.tvItemCount.setText("20 items");

        }

        if (FRAGMENT_TAG == "KANJI") {
            holder.tvTitle.setText("KANJI");
            holder.tvItemCount.setText("3 items");
        }

    }

    @Override
    public int getItemCount() {
        if (FRAGMENT_TAG.equalsIgnoreCase("RECENTLY")) {
            return 10;
        }
        if (FRAGMENT_TAG.equalsIgnoreCase("MOJI")) {
            return 20;
        }
        return 3;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvItemCount;
        public ImageButton btnDeleteItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvItemCount = itemView.findViewById(R.id.tv_item_count);
            btnDeleteItem = itemView.findViewById(R.id.btn_delete_item);
        }

        public void bindViewHolder() {

        }
    }
}
