package com.jishin.ankiji.Chart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jishin.ankiji.R;
import com.jishin.ankiji.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private PieChart chartPie;
    private Button btnReset;
    private String userUid;
    private String setID;
    private int listSize;
    private DatabaseReference chartRef;
    private String correctAnswer = "0";
    private String testTimes = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        new LoadNodeCharTask().execute();

        // ----------- binding view --------------
        chartPie = findViewById(R.id.test_chart);
        btnReset = findViewById(R.id.btnRetry);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constants.USER_ID)){
                userUid = intent.getExtras().getString(Constants.USER_ID);
                setID = intent.getExtras().getString(Constants.SET_BY_USER);
                listSize = intent.getExtras().getInt("SIZE");
                correctAnswer = intent.getExtras().getString(Constants.CORRECT_ANSWER);
                testTimes = intent.getExtras().getString(Constants.TEST_TIMES);
                Log.d("Chart_UserID", userUid);
                Log.d("Chart_SetID", setID);
                Log.d("SIZE", String.valueOf(listSize));
            }
        }

        // ------------ data collection -------------
        List<PieEntry> pieEntries = new ArrayList<>();
        Log.d("Outcorrect", String.valueOf(correctAnswer));
        Log.d("Outtest", String.valueOf(testTimes));

        pieEntries.add(new PieEntry(Integer.parseInt(correctAnswer) / Integer.parseInt(testTimes), "Từ đã thuộc\n"));
        pieEntries.add(new PieEntry(listSize - Integer.parseInt(correctAnswer), "Từ chưa thuộc\n"));
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        // ------------ styling of data collection -----------
        // style of the value of entries
        // color of the pies

        final int[] MY_COLORS = {Color.rgb(0,150,0), Color.rgb(0,50,150), Color.rgb(150,100,0),
                Color.rgb(150,150,50), Color.rgb(146,208,80), Color.rgb(0,176,80), Color.rgb(79,129,189)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        dataSet.setValueTextSize(20);
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);

        // ---------------- chart set data --------------
        PieData pieData = new PieData(dataSet);
        chartPie.setData(pieData);

        // --------------- chart styling ----------------
        // description
        // style of the label of entries
        // center text

//        Description desc = new Description();
//        desc.setText("Chỗ này là để chú giải");
//        desc.setTextSize(16);
//        chartPie.setDescription(desc);


        chartPie.setCenterText("Trung bình số câu đúng qua các lần Test");
        chartPie.setCenterTextSize(24);

        chartPie.setEntryLabelTextSize(16);

        // refresh the chart
        chartPie.invalidate();

//        btnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resetNodeChart();
//                startActivity(new Intent(ChartActivity.this, FeatureActivity.class));
//                finish();
//            }
//        });

    }

    private void resetNodeChart() {
        FirebaseDatabase.getInstance()
                .getReference(Constants.CHART)
                .child(userUid).child(setID).child(Constants.CORRECT_ANSWER).setValue("0");
        FirebaseDatabase.getInstance().getReference(Constants.CHART)
                .child(userUid).child(setID).child(Constants.TEST_TIMES).setValue("0");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    class LoadNodeCharTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            getDataNodeChart();
//            return null;
//        }
//    }
//
//    private void getDataNodeChart() {
//        chartRef = FirebaseDatabase.getInstance().getReference(Constants.CHART).child(userUid).child(setID);
//        chartRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                correctAnswer = dataSnapshot.child(Constants.CORRECT_ANSWER).getValue(Long.class);
//                testTimes = dataSnapshot.child(Constants.TEST_TIMES).getValue(Long.class);
//                Log.d("read_data_correct", String.valueOf(correctAnswer));
//                Log.d("read_data_test", String.valueOf(testTimes));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//    }
}
