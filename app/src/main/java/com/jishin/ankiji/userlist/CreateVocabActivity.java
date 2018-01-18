package com.jishin.ankiji.userlist;

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
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;

import java.util.ArrayList;

public class CreateVocabActivity extends AppCompatActivity implements OnTextListener{
    private Toolbar toolbar;
    private RecyclerView rvVocab;
    private ArrayList<Kanji> mKanjiList = new ArrayList<>();
    private ArrayList<Moji> mMojiList = new ArrayList<>();
    private KanjiItemAdapter kanjiAdapter;
    private LinearLayoutManager layoutManager;
    private TextView btnDone;
    private ImageView btnAdd;
    private TextView txtWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vocab);
        initControl();
        setupRecyclerView();
        setEvents();
    }
    private void initControl(){
        //create data sample
        mKanjiList.add(new Kanji("","","",""));
        //controller
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.create_vocab_menu);
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
                mKanjiList.add(new Kanji("","","",""));
                kanjiAdapter.notifyItemInserted(mKanjiList.size());
                rvVocab.scrollToPosition(mKanjiList.size()-1);
                txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
            }
        });

        //set swipe action
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                mKanjiList.add(position+1,new Kanji("","","",""));
                kanjiAdapter.notifyItemInserted(position+1);
                rvVocab.scrollToPosition(position+1);
                txtWord.setText("Từ vựng ("+mKanjiList.size()+")");

            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
                mKanjiList.remove(position);
                kanjiAdapter.notifyItemRemoved(position);
                rvVocab.scrollToPosition(position);
                txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
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
        kanjiAdapter = new KanjiItemAdapter(mKanjiList, getBaseContext(), this);
        rvVocab.setAdapter(kanjiAdapter);
    }

    @Override
    public void onTextListener(ArrayList<Kanji> list) {
        //mKanjiList = list;
    }

//    public void noUse(){
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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
