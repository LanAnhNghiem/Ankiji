package com.jishin.ankiji.adapter;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Kanji;

import java.util.ArrayList;

/**
 * Created by lana on 17/01/2018.
 */

public class KanjiItemAdapter extends RecyclerView.Adapter<KanjiItemAdapter.KanjiItemHolder> {
    public ArrayList<Kanji> mList = new ArrayList<>();
    private Context mContext;
    private boolean isClick = false;

    public KanjiItemAdapter(ArrayList<Kanji> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }
    public KanjiItemAdapter(Context context){
        this.mContext = context;
    }
    public KanjiItemAdapter(){}

    @Override
    public KanjiItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kanji, parent, false);
        KanjiItemHolder holder = new KanjiItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(KanjiItemHolder holder, int position) {
        holder.edtKanji.setText(mList.get(position).getKanji());
        holder.edtWord.setText(mList.get(position).getTuvung());
        holder.edtMeaning.setText(mList.get(position).getAmhan());
        addTextListener(holder, position);
        holder.edtKanji.requestFocus();
    }
    public void addTextListener(KanjiItemHolder holder, final int position){

        holder.edtKanji.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position < mList.size()){
                    mList.get(position).setKanji(s.toString());
                    setOnTextListener(mList);
                }
            }
        });
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
                    mList.get(position).setTuvung(s.toString());
                    setOnTextListener(mList);
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
                    mList.get(position).setAmhan(s.toString());
                    setOnTextListener(mList);
                }
            }
        });
    }
    public void setOnTextListener(ArrayList<Kanji> list){
        //onTextListener.onTextListener(list);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class KanjiItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AbsListView.OnScrollListener{
        TextInputEditText edtKanji, edtMeaning, edtWord;

        public KanjiItemHolder(View itemView) {
            super(itemView);

            edtKanji = itemView.findViewById(R.id.edt_kanji);
            edtMeaning = itemView.findViewById(R.id.edt_meaning);
            edtWord = itemView.findViewById(R.id.edt_word);
            edtKanji.setOnClickListener(this);
            edtMeaning.setOnClickListener(this);
            edtWord.setOnClickListener(this);
            //addTextListener(this,getAdapterPosition());
        }

        @Override
        public void onClick(View v) {
            isClick = true;
            Toast.makeText(mContext,"click nè", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == SCROLL_STATE_IDLE && isClick){

            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(isClick){



            }
        }

    }

}
