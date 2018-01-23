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

import java.util.ArrayList;

/**
 * Created by lana on 17/01/2018.
 */

public class KanjiItemAdapter extends RecyclerView.Adapter<KanjiItemAdapter.KanjiItemHolder> {
    public static final String TAG = KanjiItemAdapter.class.getSimpleName();
    public ArrayList<Kanji> mList = new ArrayList<>();
    private Context mContext;

    public KanjiItemAdapter(ArrayList<Kanji> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }
    public KanjiItemAdapter(Context context){
        this.mContext = context;
    }

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
        holder.edtKanji.requestFocus();
  }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class KanjiItemHolder extends RecyclerView.ViewHolder {
        TextInputEditText edtKanji, edtMeaning, edtWord;

        public KanjiItemHolder(View itemView) {
            super(itemView);

            edtKanji = itemView.findViewById(R.id.edt_kanji);
            edtMeaning = itemView.findViewById(R.id.edt_meaning);
            edtWord = itemView.findViewById(R.id.edt_word);
            addTextListener();
        }

//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if(event.getAction() == MotionEvent.ACTION_SCROLL){
//                isScroll = true;
//            }
//            if(event.getAction() == MotionEvent.ACTION_DOWN &&
//                    event.getAction() != MotionEvent.ACTION_SCROLL){
//                realPosition = getAdapterPosition();
//                if(oldPos != getAdapterPosition()){
//                    //Toast.makeText(mContext,String.valueOf("Position: "+realPosition), Toast.LENGTH_SHORT).show();
//                    oldPos = realPosition;
//                    addTextListener(realPosition);
//                }
//            }
//            return false;
//        }
        public void addTextListener(){
            edtKanji.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().isEmpty()){
                        if(getAdapterPosition() < mList.size() && !s.toString().isEmpty()){
                            //Toast.makeText(mContext, edtKanji.getText().toString(), Toast.LENGTH_SHORT).show();
                            mList.get(getAdapterPosition()).setKanji(s.toString());
                        }
                    }
                }
            });
            edtWord.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().isEmpty()){
                        if(getAdapterPosition() < mList.size() && !s.toString().isEmpty()){
                            //Toast.makeText(mContext, edtKanji.getText().toString(), Toast.LENGTH_SHORT).show();
                            mList.get(getAdapterPosition()).setTuvung(s.toString());
                        }
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
                    if(!s.toString().isEmpty()){
                        if(getAdapterPosition() < mList.size() && !s.toString().isEmpty()){
                            //Toast.makeText(mContext, edtKanji.getText().toString(), Toast.LENGTH_SHORT).show();
                            mList.get(getAdapterPosition()).setAmhan(s.toString());
                        }
                    }
                }
            });

        }
    }

}
