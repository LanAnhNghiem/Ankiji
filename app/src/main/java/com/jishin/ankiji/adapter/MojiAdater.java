package com.jishin.ankiji.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Moji;

import java.util.ArrayList;

/**
 * Created by SPlayer on 18/01/2018.
 */

public class MojiAdater extends RecyclerView.Adapter<MojiAdater.RecyclerViewHolder>{
    ArrayList<Moji> mMojiList = new ArrayList<Moji>();

    @Override
    public MojiAdater.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.moji_item,parent,false);
        return new MojiAdater.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.txtAmHan.setText(mMojiList.get(position).getAmHan());
        holder.txtNghiaTV.setText(mMojiList.get(position).getNghiaTiengViet());
        holder.txtHira.setText(mMojiList.get(position).getCachDocHira());
        holder.txtTuTN.setText(mMojiList.get(position).getTuTiengNhat());
    }

    @Override
    public int getItemCount() {
        return mMojiList.size();
    }

    public void setmMojiList(ArrayList<Moji> list) {
        this.mMojiList = list;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtAmHan;
        TextView txtHira;
        TextView txtNghiaTV;
        TextView txtTuTN;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtAmHan = itemView.findViewById(R.id.txt_moji_amHan);
            txtHira = itemView.findViewById(R.id.txt__moji_Hira);
            txtNghiaTV = itemView.findViewById(R.id.txt_moji_nghiaTV);
            txtTuTN = itemView.findViewById(R.id.txt_moji_tuTN);
        }
    }
}
