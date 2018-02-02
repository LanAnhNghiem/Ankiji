package com.jishin.ankiji.create_data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Kanji;

import java.util.ArrayList;

/**
 * Created by lana on 02/02/2018.
 */

public class KanjiCardAdapter extends RecyclerView.Adapter<KanjiCardAdapter.KanjiCardHolder> {
    private ArrayList<Kanji> mList = new ArrayList<>();
    private Context mContext;
    public KanjiCardAdapter(ArrayList<Kanji>list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public KanjiCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.moji_item, parent,false);
        KanjiCardHolder holder = new KanjiCardHolder(inflatedView);
        return holder;
    }

    @Override
    public void onBindViewHolder(KanjiCardHolder holder, int position) {
        holder.tvTuVung.setText(mList.get(position).getTuvung());
        holder.tvAmHan.setText(mList.get(position).getAmhan());
        holder.tvKanji.setText(mList.get(position).getKanji());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class KanjiCardHolder extends RecyclerView.ViewHolder {
        TextView tvTuVung, tvAmHan, tvKanji;
        public KanjiCardHolder(View itemView) {
            super(itemView);
            tvTuVung = itemView.findViewById(R.id.txt_tuVung);
            tvAmHan = itemView.findViewById(R.id.txt_amHan);
            tvKanji = itemView.findViewById(R.id.txt_Kanji);
        }
    }
}
