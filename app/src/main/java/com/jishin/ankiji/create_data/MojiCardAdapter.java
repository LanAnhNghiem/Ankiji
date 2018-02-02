package com.jishin.ankiji.create_data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Moji;

import java.util.ArrayList;

/**
 * Created by lana on 02/02/2018.
 */

public class MojiCardAdapter extends RecyclerView.Adapter<MojiCardAdapter.MojiCardHolder> {
    private ArrayList<Moji> mList = new ArrayList<>();
    private Context mContext;
    public MojiCardAdapter(ArrayList<Moji>list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public MojiCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.moji_item, parent,false);
        MojiCardHolder holder = new MojiCardHolder(inflatedView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MojiCardHolder holder, int position) {
        holder.tvTuVung.setText(mList.get(position).getTuTiengNhat());
        holder.tvAmHan.setText(mList.get(position).getAmHan());
        holder.tvNghia.setText(mList.get(position).getNghiaTiengViet());
        holder.tvHira.setText(mList.get(position).getCachDocHira());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MojiCardHolder extends RecyclerView.ViewHolder {
        TextView tvTuVung, tvAmHan, tvHira, tvNghia;
        public MojiCardHolder(View itemView) {
            super(itemView);
            tvTuVung = itemView.findViewById(R.id.txt_moji_tu);
            tvAmHan = itemView.findViewById(R.id.txt_moji_amHan);
            tvHira = itemView.findViewById(R.id.txt_moji_Hira);
            tvNghia = itemView.findViewById(R.id.txt_moji_nghiaTV);
        }
    }
}
