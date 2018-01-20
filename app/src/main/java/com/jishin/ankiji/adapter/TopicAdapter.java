package com.jishin.ankiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.explores.KanjiExploresActivity;
import com.jishin.ankiji.explores.MojiExploresActivity;

import java.util.ArrayList;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicHolder> {

    public static final String TAG = TopicAdapter.class.getSimpleName();
    public Context context;


    ArrayList<String> Topic = new ArrayList<String>();


    private String FRAGMENT_TAG;

    public TopicAdapter(String FRAGMENT_TAG) {
        this.FRAGMENT_TAG = FRAGMENT_TAG;
    }

    @Override
    public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.topic_recycler_item, parent, false);
        return new TopicHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TopicHolder holder, final int position) {
        if (FRAGMENT_TAG == "KANJI") {
            holder.tvTitle.setText(Topic.get(position));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + Topic.get(position));
                    Intent intent = new Intent(holder.context, KanjiExploresActivity.class);
                    intent.putExtra("Kanji_Key", Topic.get(position));
                    holder.context.startActivity(intent);
                }
            });

        } else if (FRAGMENT_TAG == "MOJI") {
            int topicNum = position+1;
            holder.tvTitle.setText("Topic "+ String.valueOf(topicNum));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + Topic.get(position));
                    Intent intent = new Intent(holder.context, MojiExploresActivity.class);
                    intent.putExtra("Moji_Key", Topic.get(position));
                    holder.context.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Topic.size();
    }

    public ArrayList<String> setTopic(ArrayList<String> topic) {
        Topic = topic;
        return Topic;
    }

    public class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private CardView cardView;
        public TextView tvItemCount;
        public ImageButton btnDeleteItem;
        private final Context context;

        public TopicHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvItemCount = itemView.findViewById(R.id.tv_item_count);
            btnDeleteItem = itemView.findViewById(R.id.btn_delete_item);
            cardView = itemView.findViewById(R.id.card_view);
            context = itemView.getContext();

        }

        public void bindViewHolder() {

        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick(in): " + getAdapterPosition());
        }
    }


}
