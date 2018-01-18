package com.jishin.ankiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jishin.ankiji.R;

import java.util.ArrayList;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ItemViewHolder> {

    ArrayList<String> Topic = new ArrayList<String>();

    private String FRAGMENT_TAG;

    public TopicAdapter(String FRAGMENT_TAG) {
        this.FRAGMENT_TAG = FRAGMENT_TAG;
    }

    @Override
    public TopicAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_recycler_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopicAdapter.ItemViewHolder holder, int position) {
        holder.tvTitle.setText(Topic.get(position));
        holder.tvItemCount.setText("");
    }

    @Override
    public int getItemCount() {
        return Topic.size();
    }

    public ArrayList<String> setTopic(ArrayList<String> topic) {
        Topic = topic;
        return Topic;
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
