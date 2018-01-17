package com.jishin.ankiji.userlist;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.KanjiItemAdapter;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;

import java.util.ArrayList;

public class CreateVocabActivity extends AppCompatActivity {
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
        getControl();
    }
    private void getControl(){
        //create data sample
        mKanjiList.add(new Kanji("","","",""));

        //controller
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.create_vocab_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvVocab = findViewById(R.id.rvVocab);
        layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVocab.setLayoutManager(layoutManager);
        kanjiAdapter = new KanjiItemAdapter(mKanjiList, getBaseContext());
        rvVocab.setAdapter(kanjiAdapter);
        txtWord = findViewById(R.id.txt_vocab);
        btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateVocabActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKanjiList.add(new Kanji("","","",""));
                kanjiAdapter.notifyDataSetChanged();
                txtWord.setText("Từ vựng ("+mKanjiList.size()+")");
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.create_vocab_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.btn_done:
//                Toast.makeText(this, "Click done", Toast.LENGTH_SHORT).show();
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//        //getMenuInflater().inflate(R.menu.create_vocab_menu, item);
//
//    }
}
