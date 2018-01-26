package com.jishin.ankiji.adapter;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Moji;

import java.util.ArrayList;

/**
 * Created by lana on 17/01/2018.
 */

public class MojiItemAdapter extends RecyclerView.Adapter<MojiItemAdapter.MojiItemHolder> {
    public ArrayList<Moji> mList = new ArrayList<>();
    private Context mContext;

    public MojiItemAdapter(ArrayList<Moji> list, Context context) {
        this.mList = list;
        this.mContext = context;

    }
    public MojiItemAdapter(Context context){
        this.mContext = context;
    }
    public MojiItemAdapter(){}

    @Override
    public MojiItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moji, parent, false);
        MojiItemHolder holder = new MojiItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MojiItemHolder holder, int position) {
        holder.edtHira.setText(mList.get(position).getCachDocHira());
        holder.edtWord.setText(mList.get(position).getTuTiengNhat());
        holder.edtMeaning.setText(mList.get(position).getNghiaTiengViet());
        holder.edtOnyomi.setText(mList.get(position).getAmHan());
        holder.edtWord.requestFocus();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MojiItemHolder extends RecyclerView.ViewHolder {
        TextInputEditText edtHira,edtOnyomi, edtMeaning, edtWord;

        public MojiItemHolder(View itemView) {
            super(itemView);

            edtHira = itemView.findViewById(R.id.edt_hira);
            edtMeaning = itemView.findViewById(R.id.edt_meaning);
            edtWord = itemView.findViewById(R.id.edt_word);
            edtOnyomi = itemView.findViewById(R.id.edt_onyomi);
            addTextListener();
        }
        public void addTextListener(){

            edtWord.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(getAdapterPosition() < mList.size() && !s.toString().isEmpty()){
                        mList.get(getAdapterPosition()).setTuTiengNhat(s.toString());
                    }
                }
            });
            edtHira.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(getAdapterPosition() < mList.size() && !s.toString().isEmpty()){
                        mList.get(getAdapterPosition()).setCachDocHira(s.toString());
                    }
                }
            });
            edtMeaning.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(getAdapterPosition() < mList.size() && !s.toString().isEmpty()){
                        mList.get(getAdapterPosition()).setNghiaTiengViet(s.toString());
                    }
                }
            });
            edtOnyomi.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(getAdapterPosition() < mList.size() && !s.toString().isEmpty()){
                        mList.get(getAdapterPosition()).setAmHan(s.toString());
                    }
                }
            });
        }
    }

}
