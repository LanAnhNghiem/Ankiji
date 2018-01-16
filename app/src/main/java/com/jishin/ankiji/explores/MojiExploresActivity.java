package com.jishin.ankiji.explores;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jishin.ankiji.R;

public class MojiExploresActivity extends AppCompatActivity {
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moji_explores);
        addControl();
    }

    private void addControl(){
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }
}
