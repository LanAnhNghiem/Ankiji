package com.jishin.ankiji.userlist;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.KanjiItemAdapter;
import com.jishin.ankiji.adapter.MojiItemAdapter;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.utilities.Constants;

import java.util.ArrayList;

public class CreateVocabActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private RecyclerView rvVocab;
    private ArrayList<Kanji> mKanjiList = new ArrayList<>();
    private ArrayList<Moji> mMojiList = new ArrayList<>();
    private KanjiItemAdapter kanjiAdapter;
    private MojiItemAdapter mojiAdapter;
    private LinearLayoutManager layoutManager;
    private TextView btnDone;
    private ImageView btnAdd;
    private TextView txtWord;
    private boolean isKanji = true;
    private String mSetName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vocab);
        Intent intent = getIntent();
        if(intent.hasExtra("create")){
            if(intent.getStringExtra("create").equals(Constants.CREATE_MOJI)){
                isKanji = false;
            }
            mSetName = intent.getStringExtra("name");

        }
        initControl();
        setupRecyclerView();
        setEvents();
    }
    private void initControl(){
        //create data sample
        if(isKanji){
            mKanjiList.add(new Kanji("","","",""));
        }else{
            mMojiList.add(new Moji("","","",""));
        }

        //controller
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.create_vocab_menu);
        toolbar.setTitle(mSetName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtWord = findViewById(R.id.txt_vocab);
        btnDone = findViewById(R.id.btn_done);
        btnAdd = findViewById(R.id.btn_add);

    }
    private void setEvents(){
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateVocabActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isKanji){
                    mKanjiList.add(new Kanji("","","",""));
                    kanjiAdapter.notifyItemInserted(mKanjiList.size());
                    rvVocab.scrollToPosition(mKanjiList.size()-1);
                    txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
                }
                else{
                    mMojiList.add(new Moji("","","",""));
                    mojiAdapter.notifyItemChanged(mMojiList.size());
                    rvVocab.scrollToPosition(mMojiList.size()-1);
                    txtWord.setText("Từ vựng ("+mMojiList.size()+")");
                }
            }
        });

        //set swipe action
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                if(isKanji){
                    mKanjiList.add(position+1,new Kanji("","","",""));
                    kanjiAdapter.notifyItemInserted(position+1);
                    rvVocab.scrollToPosition(position+1);
                    txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
                }
                else{
                    mMojiList.add(position+1,new Moji("","","",""));
                    mojiAdapter.notifyItemInserted(position+1);
                    rvVocab.scrollToPosition(position+1);
                    txtWord.setText("Từ vựng ("+mMojiList.size()+")");
                }

            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
                if(isKanji){
                    mKanjiList.remove(position);
                    kanjiAdapter.notifyItemRemoved(position);
                    rvVocab.scrollToPosition(position);
                    txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
                }
                else{
                    mMojiList.remove(position);
                    mojiAdapter.notifyItemRemoved(position);
                    rvVocab.scrollToPosition(position);
                    txtWord.setText("Từ vựng ("+mMojiList.size()+")");
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(rvVocab);
        rvVocab.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                swipeController.onDraw(c);
            }
        });
    }
    private void setupRecyclerView(){
        rvVocab = findViewById(R.id.rvVocab);
        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVocab.setLayoutManager(layoutManager);
        if(isKanji){
            kanjiAdapter = new KanjiItemAdapter(mKanjiList, getBaseContext());
            rvVocab.setAdapter(kanjiAdapter);
        }else{
            mojiAdapter = new MojiItemAdapter(mMojiList, getBaseContext());
            rvVocab.setAdapter(mojiAdapter);
        }

    }


//    public void noUse(){
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHol der viewHolder, RecyclerView.ViewHolder target) {
//                Toast.makeText(CreateVocabActivity.this, "on Move", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                Toast.makeText(CreateVocabActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
//                //Remove swiped item from list and notify the RecyclerView
//                int position = viewHolder.getAdapterPosition();
//                mKanjiList.remove(position);
//                kanjiAdapter.notifyDataSetChanged();
//
//            }
//        };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(rvVocab);
//    }
}
