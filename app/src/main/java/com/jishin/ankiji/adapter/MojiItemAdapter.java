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
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.userlist.OnTextListener;

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
        addTextListener(holder, position);
        holder.edtWord.requestFocus();
    }
    public void addTextListener(MojiItemHolder holder, final int position){

        holder.edtWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position < mList.size()){
                    mList.get(position).setTuTiengNhat(s.toString());

                }

            }
        });
        holder.edtHira.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position < mList.size()){
                    mList.get(position).setCachDocHira(s.toString());
                }
            }
        });
        holder.edtMeaning.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position < mList.size()){
                    mList.get(position).setNghiaTiengViet(s.toString());

                }
            }
        });
        holder.edtOnyomi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 if(position < mList.size()){
                     mList.get(position).setAmHan(s.toString());
                 }
            }
        });
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

        }
    }

}
