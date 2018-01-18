package com.jishin.ankiji.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Kanji;

import java.util.ArrayList;

public class KanjiAdapter extends RecyclerView.Adapter<KanjiAdapter.RecyclerViewHolder> {

    ArrayList<Kanji> mKanjiList = new ArrayList<Kanji>();

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.kanji_item,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.txtAmHan.setText(mKanjiList.get(position).getAmhan());
        holder.txtKanji.setText(mKanjiList.get(position).getKanji());
        holder.txtTuVung.setText(mKanjiList.get(position).getTuvung());
    }

    @Override
    public int getItemCount() {
        return mKanjiList.size();
    }

    public void setmKanjiList(ArrayList<Kanji> list) {
        this.mKanjiList = list;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtAmHan;
        TextView txtTuVung;
        TextView txtKanji;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtAmHan = itemView.findViewById(R.id.txt_amHan);
            txtTuVung = itemView.findViewById(R.id.txt_tuVung);
            txtKanji = itemView.findViewById(R.id.txt_Kanji);
        }
    }
}
