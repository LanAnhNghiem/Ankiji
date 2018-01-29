package com.jishin.ankiji.feature_test;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.QuestionAnswer;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by huuduc on 17/01/2018.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    public boolean check;
    public static final String BOOK = "Soumatome";
    public static final String REFERENCE = "moji";

    public static int NUMBER_OF_QUESTION = 0;

    private int number_of_right_answer = 0;
    private int index_question = 0;
    private ArrayList<Kanji> kanjiList = new ArrayList<>();
    private ArrayList<Moji> mojiList = new ArrayList<>() ;
    private ArrayList<String> answerList = new ArrayList<>();
    private ArrayList<QuestionAnswer> QAList = new ArrayList<>();
    private ArrayList<Kanji> oldKanjiList = new ArrayList<>();
    private ArrayList<Moji> oldMojiList = new ArrayList<>();

    private Toolbar toolbar;
    //private ProgressBar progressBar;
    private TextView txtRightCount, txtQuestion, txtAnswerA, txtAnswerB, txtAnswerC, txtAnswerD;
    private TextView txtCorrect;
    private TextView txtNumberQuestion;
    private ConstraintLayout layout;
    private Set set;
    private TextView txtNotification;
    private Button btnMain;
    private Button btnRetry;
    private static boolean isKanji = false;
    Dialog settingsDialog = null;
    private ConstraintLayout layout_test;

    private static final String TAG = TestActivity.class.getSimpleName();

    private String userID;
    private String setID;
    private DatabaseReference chartRef;
    private DatabaseService mData = DatabaseService.getInstance();

    private String correctAnswer = "0";
    private String testTimes = "0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();

        if(intent.hasExtra(Constants.SET_BY_USER)){
            String fragmentTag = intent.getStringExtra(Constants.DATA_TYPE);
            if(fragmentTag.equals("KANJI")){
                isKanji = true;
                kanjiList = (ArrayList<Kanji>) intent.getSerializableExtra(Constants.SET_BY_USER);

            }else{
                isKanji = false;
                mojiList = (ArrayList<Moji>) intent.getSerializableExtra(Constants.SET_BY_USER);
            }
            userID = intent.getExtras().getString(Constants.USER_ID);
            setID = intent.getExtras().getString(Constants.KANJI_SET_NODE);

        }
        addControls();
        initData(isKanji);
        addEvents();
    }

    private void addEvents() {
        txtAnswerA.setOnClickListener(this);
        txtAnswerB.setOnClickListener(this);
        txtAnswerC.setOnClickListener(this);
        txtAnswerD.setOnClickListener(this);

        btnMain.setOnClickListener(this);
        btnRetry.setOnClickListener(this);
    }


    private void addControls() {
        check = false;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //progressBar = findViewById(R.id.determinateBar);
        txtRightCount = findViewById(R.id.txtRightCount);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtAnswerA = findViewById(R.id.txtAnswerA);
        txtAnswerB = findViewById(R.id.txtAnswerB);
        txtAnswerC = findViewById(R.id.txtAnswerC);
        txtAnswerD = findViewById(R.id.txtAnswerD);
        txtNumberQuestion = findViewById(R.id.txtNumberOfQuestion);
        txtCorrect = (TextView) findViewById(R.id.txt_correct);
        layout_test = (ConstraintLayout) findViewById(R.id.layout_test_activity);
//        kanjiList = new ArrayList<>();
//        answerList = new ArrayList<>();
//        QAList = new ArrayList<>();

        layout = findViewById(R.id.layoutTest);

        btnMain = findViewById(R.id.btnMain);
        btnRetry = findViewById(R.id.btnRetry);
        txtNotification = findViewById(R.id.txtNotification);

        //progressBar.setProgress(0);

        btnMain.setVisibility(View.GONE);
        btnRetry.setVisibility(View.GONE);
        txtNotification.setVisibility(View.GONE);

        chartRef = FirebaseDatabase.getInstance().getReference(Constants.CHART).child(userID).child(setID);
        Log.d("chartRef", chartRef.toString());
        new LoadNodeCharTask().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void updateQuestionMoji (ArrayList<Moji> listMoji, ArrayList<String> listAnswer) {


        ArrayList<String> listCurrentAnswer = new ArrayList<>(listAnswer);


        index_question++;
        //progressBar.setProgress(index_question * 100 / NUMBER_OF_QUESTION);
        txtNumberQuestion.setText("QUESTION: " + String.valueOf(index_question) + "/"
                + String.valueOf(NUMBER_OF_QUESTION));
        txtRightCount.setText(String.valueOf(number_of_right_answer));        //Log.d("MOJI_SIZE", String.valueOf(mojiList.size()));
        Log.d("ANSWER_SIZE", String.valueOf(listCurrentAnswer.size()));
        int index_moji = 0;
        index_moji = new Random().nextInt(listMoji.size());

        Log.d("INDEX_MOJI", String.valueOf(index_moji));
        int index_one;
        int index_two;
        int index_three;

        String answer_mojiRemove = "";
        String answerRemove_1 = "";
        String answerRemove_2 = "";

        txtQuestion.setText(listMoji.get(index_moji).getTuTiengNhat());


        int index_one_location = new Random().nextInt(4);
        switch (index_one_location){
            case 0:
                // set dap an vao cau A
                txtAnswerA.setText(listMoji.get(index_moji).getCachDocHira());
                // đánh dấu câu trả lời đúng của câu hỏi
                answer_mojiRemove = listMoji.get(index_moji).getCachDocHira();
                // remove khỏi list answer
                listCurrentAnswer.remove(answer_mojiRemove);


                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerB.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);
                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);

                txtAnswerC.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);
                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 1:
                txtAnswerB.setText(listMoji.get(index_moji).getCachDocHira());
                answer_mojiRemove = listMoji.get(index_moji).getCachDocHira();
                listCurrentAnswer.remove(answer_mojiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerC.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
            case 2:
                txtAnswerC.setText(listMoji.get(index_moji).getCachDocHira());
                answer_mojiRemove = listMoji.get(index_moji).getCachDocHira();
                listCurrentAnswer.remove(answer_mojiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 3:
                txtAnswerD.setText(listMoji.get(index_moji).getCachDocHira());
                answer_mojiRemove = listMoji.get(index_moji).getCachDocHira();
                listCurrentAnswer.remove(answer_mojiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerC.setText(listCurrentAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
        }

        listMoji.remove(listMoji.get(index_moji));
    }

    public void updateQuestionKanji (ArrayList<Kanji> listKanji, ArrayList<String> listAnswer) {


        ArrayList<String> listCurrentAnswer = new ArrayList<>(listAnswer);


        index_question++;
        //progressBar.setProgress(index_question * 100 / NUMBER_OF_QUESTION);
        String numberQuestion = "Question: " + String.valueOf(index_question) + "/"
                + String.valueOf(NUMBER_OF_QUESTION);
        String rightCount = String.valueOf(number_of_right_answer);
        txtNumberQuestion.setText(numberQuestion);
        txtRightCount.setText(rightCount);
        //Log.d("MOJI_SIZE", String.valueOf(mojiList.size()));
        Log.d("ANSWER_SIZE", String.valueOf(listCurrentAnswer.size()));
        int index_kanji = 0;
        index_kanji = new Random().nextInt(listKanji.size());

        Log.d("INDEX_MOJI", String.valueOf(index_kanji));
        int index_one;
        int index_two;
        int index_three;

        String answer_kanjiRemove = "";
        String answerRemove_1 = "";
        String answerRemove_2 = "";

        txtQuestion.setText(listKanji.get(index_kanji).getKanji());


        int index_one_location = new Random().nextInt(4);
        switch (index_one_location){
            case 0:
                // set dap an vao cau A
                txtAnswerA.setText(listKanji.get(index_kanji).getAmhan());
                // đánh dấu câu trả lời đúng của câu hỏi
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                // remove khỏi list answer
                listCurrentAnswer.remove(answer_kanjiRemove);


                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerB.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);
                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);

                txtAnswerC.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);
                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 1:
                txtAnswerB.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listCurrentAnswer.remove(answer_kanjiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerC.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
            case 2:
                txtAnswerC.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listCurrentAnswer.remove(answer_kanjiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 3:
                txtAnswerD.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listCurrentAnswer.remove(answer_kanjiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerC.setText(listCurrentAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
        }

        listKanji.remove(listKanji.get(index_kanji));
    }

    public void updateNumberOfRightAnswer () {
        txtRightCount.setText(String.valueOf(number_of_right_answer));
    }

//    public void showDialog(boolean check) {
//        settingsDialog = new Dialog(this);
//        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if (check){
//            settingsDialog.setContentView(getLayoutInflater()
//                    .inflate(R.layout.image_right, null));
//        }else{
//            settingsDialog.setContentView(getLayoutInflater()
//                    .inflate(R.layout.image_wrong, null));
//        }
//        settingsDialog.show();

//        settingsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                updateQuestion(mojiList, answerList);
//            }
//        });

//    }

    public void checkTrueOrFalseAnswer(boolean check, final TextView answer_view) {
        if (check == true) {
            new CountDownTimer(1100, 1000) {
                public void onTick(long millisUntilFinished) {
                    Log.d("TAG", "onTick: " + "seconds remaining: " + millisUntilFinished / 1000);

                    answer_view.setBackgroundColor(Color.parseColor("#4CAF50"));
                    answer_view.setTextColor(Color.WHITE);
                    txtAnswerA.setEnabled(false);
                    txtAnswerB.setEnabled(false);
                    txtAnswerC.setEnabled(false);
                    txtAnswerD.setEnabled(false);

                    //next question if touch on screen
                    layout_test.setEnabled(true);
                    layout_test.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: clicked layout_test");
                            onFinish();
                            cancel();
                        }
                    });
                }
                public void onFinish() {
                    Log.d(TAG, "onFinish: countdowntimer");
                    layout_test.setEnabled(false);
                    finishCheckAnswer(answer_view);
                }
            }.start();
        }
        else {
            new CountDownTimer(1100, 1000) {
                public void onTick(long millisUntilFinished) {
                    Log.d(TAG, "onTick");
                    Log.d("TAG", "onTick: " + "seconds remaining: " + millisUntilFinished / 1000);
                    answer_view.setBackgroundColor(Color.parseColor("#C62828"));
                    answer_view.setTextColor(Color.WHITE);
                    txtAnswerA.setEnabled(false);
                    txtAnswerB.setEnabled(false);
                    txtAnswerC.setEnabled(false);
                    txtAnswerD.setEnabled(false);

                    //next question if touch on screen
                    layout_test.setEnabled(true);
                    layout_test.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: clicked layout_test");
                            onFinish();
                            cancel();
                        }
                    });
                }

                public void onFinish() {
                    Log.d(TAG, "onFinish: countdowntimer");
                    layout_test.setEnabled(false);
                    finishCheckAnswer(answer_view);
                }
            }.start();
        }
    }

    public void finishCheckAnswer(TextView answer_view) {
        Log.d(TAG, "finishCheckAnswer: isKanji" + isKanji);
        answer_view.setTextColor(Color.parseColor("#AB5E4F"));
        answer_view.setBackgroundResource(R.drawable.border);
        txtAnswerA.setEnabled(true);
        txtAnswerB.setEnabled(true);
        txtAnswerC.setEnabled(true);
        txtAnswerD.setEnabled(true);

        if (isKanji) {
            if (kanjiList.isEmpty()) {
                Log.d("KANJIlist_EMPTY", "KANJIlist_EMPTY");
                updateNumberOfRightAnswer();
                showResult();
            } else {
                updateQuestionKanji(kanjiList, answerList);
            }
        } else {
            if (mojiList.isEmpty()) {
                Log.d("MOJIlist_EMPTY", "MOJIlist_EMPTY");
                updateNumberOfRightAnswer();
                showResult();
            } else {
                updateQuestionMoji(mojiList, answerList);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (settingsDialog != null) {
            settingsDialog.dismiss();
        }
    }

    public void showResult () {
        if (settingsDialog != null) {
            settingsDialog.dismiss();
        }
        txtQuestion.setVisibility(View.GONE);
        txtAnswerA.setVisibility(View.GONE);
        txtAnswerB.setVisibility(View.GONE);
        txtAnswerC.setVisibility(View.GONE);
        txtAnswerD.setVisibility(View.GONE);

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) txtNumberQuestion.getLayoutParams();
        marginLayoutParams.setMargins(0, 250, 0, 0);
        txtRightCount.setTextSize(100f);

        txtNotification.setVisibility(View.VISIBLE);
        btnMain.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        String question = txtQuestion.getText().toString();
        String answer = "";

        switch (view.getId()) {
            case R.id.txtAnswerA:
                answer = txtAnswerA.getText().toString();
                for (QuestionAnswer item : QAList) {
                    if (item.getQuestion().equalsIgnoreCase(question)) {
                        if (item.getAnswer().equalsIgnoreCase(answer)) {
                            check = true;
                            number_of_right_answer++;
                        } else {
                            check = false;
                        }
                    }
                }
                //showDialog(check);
                checkTrueOrFalseAnswer(check, txtAnswerA);

                break;
            case R.id.txtAnswerB:

                answer = txtAnswerB.getText().toString();
                for (QuestionAnswer item : QAList) {
                    if (item.getQuestion().equalsIgnoreCase(question)) {
                        if (item.getAnswer().equalsIgnoreCase(answer)) {
                            check = true;
                            number_of_right_answer++;
                        } else {
                            check = false;
                        }
                    }
                }
//                showDialog(check);
                checkTrueOrFalseAnswer(check, txtAnswerB);
                break;
            case R.id.txtAnswerC:
                answer = txtAnswerC.getText().toString();
                for (QuestionAnswer item : QAList) {
                    if (item.getQuestion().equalsIgnoreCase(question)) {
                        if (item.getAnswer().equalsIgnoreCase(answer)) {
                            check = true;
                            number_of_right_answer++;
                        } else {
                            check = false;
                        }
                    }
                }
//                showDialog(check);
                checkTrueOrFalseAnswer(check, txtAnswerC);
                break;
            case R.id.txtAnswerD:
                answer = txtAnswerD.getText().toString();
                for (QuestionAnswer item : QAList) {
                    if (item.getQuestion().equalsIgnoreCase(question)) {
                        if (item.getAnswer().equalsIgnoreCase(answer)) {
                            check = true;
                            number_of_right_answer++;
                        } else {
                            check = false;
                        }
                    }
                }
                //showDialog(check);
                checkTrueOrFalseAnswer(check, txtAnswerD);
                break;

            case R.id.btnMain:
                setDataOfNodeChart();
                this.finish();
                break;

            case R.id.btnRetry:
                setDataOfNodeChart();
                Intent refresh = new Intent(this, TestActivity.class);
                if (isKanji) {
                    Log.d("test", String.valueOf(kanjiList.size()));

                    refresh.putExtra(Constants.SET_BY_USER, oldKanjiList);
                    refresh.putExtra(Constants.DATA_TYPE, "KANJI");

                } else {

                    refresh.putExtra(Constants.SET_BY_USER, oldMojiList);
                    refresh.putExtra(Constants.DATA_TYPE, "MOJI");
                }
                refresh.putExtra(Constants.USER_ID, mData.getUserID());
                refresh.putExtra(Constants.KANJI_SET_NODE, setID);
                startActivity(refresh);
                this.finish();
                break;
        }
    }

    private void initData(boolean isKanji){
        if(isKanji){
            for(int i = 0; i< kanjiList.size(); i++){
                oldKanjiList.add(kanjiList.get(i));
                answerList.add(kanjiList.get(i).getAmhan());
                QAList.add(new QuestionAnswer(kanjiList.get(i).getKanji(),
                        kanjiList.get(i).getAmhan()));
            }
            NUMBER_OF_QUESTION = kanjiList.size();
            updateQuestionKanji(kanjiList, answerList);
        }
        else{
            for(int i = 0; i< mojiList.size(); i++){
                oldMojiList.add(mojiList.get(i));
                answerList.add(mojiList.get(i).getCachDocHira());
                QAList.add(new QuestionAnswer(mojiList.get(i).getTuTiengNhat(),
                        mojiList.get(i).getCachDocHira()));
            }
            NUMBER_OF_QUESTION = mojiList.size();
            updateQuestionMoji(mojiList, answerList);
        }
    }

    private void setDataOfNodeChart() {
        Log.d("correctAnswer", correctAnswer);
        Log.d("testTimes", testTimes);

        int temp = Integer.parseInt(correctAnswer) + number_of_right_answer;
        FirebaseDatabase.getInstance().getReference(Constants.CHART)
                .child(userID).child(setID).child(Constants.CORRECT_ANSWER).setValue(String.valueOf(temp));

        temp = Integer.parseInt(testTimes) + 1;
        FirebaseDatabase.getInstance().getReference(Constants.CHART)
                .child(userID).child(setID).child(Constants.TEST_TIMES).setValue(String.valueOf(temp));
    }

    class LoadNodeCharTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(final Void... voids) {
            if (chartRef == null){
                Log.d("NULL_DAY_NE", "NULL_DAY_NE");
            }
            chartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(Constants.CORRECT_ANSWER).getValue() == null){
                        Log.d("Khong co gi", "Khong co correct");
                        correctAnswer = "0";
                        testTimes = "0";
                    }else{
                        correctAnswer = dataSnapshot.child(Constants.CORRECT_ANSWER).getValue(String.class);
                        testTimes = dataSnapshot.child(Constants.TEST_TIMES).getValue(String.class);
                        Log.d("COrrect", correctAnswer);
                        Log.d("TestTimesf ", testTimes);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
            publishProgress();
            return null;
        }

    }
    public class LoadDataForChart extends AsyncTask<Void, Void, Void>{

        String userId;
        String setId;
        DatabaseReference chartRef;
        String correctAnswer;
        String testTimes;
        public LoadDataForChart(String userId, String setId){
            this.userId = userId;
            this.setId = setId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataRef();
            chartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    correctAnswer = (String) dataSnapshot.child(Constants.CORRECT_ANSWER).getValue();
                    testTimes = (String) dataSnapshot.child(Constants.TEST_TIMES).getValue();
                    onProgressUpdate();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        public void dataRef(){
            if(!mData.getUserID().isEmpty()){
                chartRef = mData.getDatabase()
                        .child(Constants.CHART)
                        .child(mData.getUserID())
                        .child(this.setId);
            }
        }
    }
//    class LoadDataTask extends AsyncTask<Void, Void, Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        Moji moji = snapshot.getValue(Moji.class);
//                        mojiList.add(moji);
//                        answerList.add(moji.getCachDocHira());
//                        QAList.add(new QuestionAnswer(moji.getTuTiengNhat(), moji.getCachDocHira()));
//                    }
//                    NUMBER_OF_QUESTION = mojiList.size();
//
//                    updateQuestionMoji(mojiList, answerList);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {}
//            });
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//    }

//    class progressBarTask extends AsyncTask<Integer, Integer, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setProgress(0);
//            txtRightCount.setText("Correct: 0/" + NUMBER_OF_QUESTION);
//            Log.d("PROGRESSBAR", String.valueOf(progressBar.getProgress()));
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            progressBar.setProgress(100);
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            int percent = values[0];
//            progressBar.setProgress(percent);
//            Log.d("PROGRESSBAR", String.valueOf(progressBar.getProgress()));
//        }
//
//        @Override
//        protected Void doInBackground(Integer... integers) {
//            int question = integers[0];
//
//            for (int i = 0; i < question; i++){
//                int percent = i * 100 / question;
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                publishProgress(percent);
//            }
//            return null;
//        }
//    }
}