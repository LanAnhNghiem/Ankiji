package com.jishin.ankiji.Feature_Test;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.features.FeatureActivity;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.QuestionAnswer;
import com.jishin.ankiji.utilities.Constants;

import java.sql.Array;
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
    private TextView txtNumberQuestion;
    private ConstraintLayout layout;

    private TextView txtNotification;
    private Button btnMain;
    private Button btnRetry;
    private static boolean isKanji = false;
    Dialog settingsDialog = null;
    //Intent refresh;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moji_test);

        Intent intent = getIntent();

        if(intent.hasExtra(Constants.SET_BY_USER)){
            String fragmentTag = intent.getStringExtra(Constants.KANJI);
            if(fragmentTag.equals("KANJI")){
                isKanji = true;
                //oldKanjiList = (ArrayList<Kanji>) intent.getSerializableExtra(Constants.SET_BY_USER);
                kanjiList = (ArrayList<Kanji>) intent.getSerializableExtra(Constants.SET_BY_USER);
                //oldKanjiList = kanjiList;
            }else{
                isKanji = false;
                //oldMojiList = (ArrayList<Moji>) intent.getSerializableExtra(Constants.SET_BY_USER);
                mojiList = (ArrayList<Moji>) intent.getSerializableExtra(Constants.SET_BY_USER);
                //oldMojiList = mojiList;
            }
        }

        addControls();
        initData(isKanji);
        //database = FirebaseDatabase.getInstance();
        //mReference = database.getReference(REFERENCE).child(BOOK).child("Bai_1");
        //new LoadDataTask().execute();
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

//        kanjiList = new ArrayList<>();
//        answerList = new ArrayList<>();
//        QAList = new ArrayList<>();

        layout = findViewById(R.id.layoutTest);
        
        btnMain = findViewById(R.id.btnMain);
        btnRetry = findViewById(R.id.btnRetry);
        txtNotification = findViewById(R.id.txtNotification);

        //progressBar.setProgress(0);

        btnMain.setVisibility(View.INVISIBLE);
        btnRetry.setVisibility(View.INVISIBLE);
        txtNotification.setVisibility(View.INVISIBLE);
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

    public void updateQuestionMoji (ArrayList<Moji> listMoji, ArrayList<String> listAnswer) {
        index_question++;
        //progressBar.setProgress(index_question * 100 / NUMBER_OF_QUESTION);
        txtNumberQuestion.setText("Question: " + String.valueOf(index_question) + "/"
                + String.valueOf(NUMBER_OF_QUESTION));
        txtRightCount.setText("Correct: " + String.valueOf(number_of_right_answer) + " / "
                + String.valueOf(NUMBER_OF_QUESTION));        //Log.d("MOJI_SIZE", String.valueOf(mojiList.size()));
        Log.d("ANSWER_SIZE", String.valueOf(listAnswer.size()));
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
                listAnswer.remove(answer_mojiRemove);


                index_one = new Random().nextInt(listAnswer.size());
                answerRemove_1 = listAnswer.get(index_one);
                txtAnswerB.setText(answerRemove_1);
                listAnswer.remove(answerRemove_1);
                index_two = new Random().nextInt(listAnswer.size());
                answerRemove_2 = listAnswer.get(index_two);

                txtAnswerC.setText(answerRemove_2);
                listAnswer.remove(answerRemove_2);
                index_three = new Random().nextInt(listAnswer.size());
                txtAnswerD.setText(listAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 1:
                txtAnswerB.setText(listMoji.get(index_moji).getCachDocHira());
                answer_mojiRemove = listMoji.get(index_moji).getCachDocHira();
                listAnswer.remove(answer_mojiRemove);
                listAnswer.remove(listMoji.get(index_moji).getCachDocHira());

                index_one = new Random().nextInt(listAnswer.size());
                answerRemove_1 = listAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listAnswer.size());
                answerRemove_2 = listAnswer.get(index_two);
                txtAnswerC.setText(answerRemove_2);
                listAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listAnswer.size());
                txtAnswerD.setText(listAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
            case 2:
                txtAnswerC.setText(listMoji.get(index_moji).getCachDocHira());
                answer_mojiRemove = listMoji.get(index_moji).getCachDocHira();
                listAnswer.remove(answer_mojiRemove);
                listAnswer.remove(listMoji.get(index_moji).getCachDocHira());

                index_one = new Random().nextInt(listAnswer.size());
                answerRemove_1 = listAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listAnswer.size());
                answerRemove_2 = listAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listAnswer.size());
                txtAnswerD.setText(listAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 3:
                txtAnswerD.setText(listMoji.get(index_moji).getCachDocHira());
                answer_mojiRemove = listMoji.get(index_moji).getCachDocHira();
                listAnswer.remove(answer_mojiRemove);
                listAnswer.remove(listMoji.get(index_moji).getCachDocHira());

                index_one = new Random().nextInt(listAnswer.size());
                answerRemove_1 = listAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listAnswer.size());
                answerRemove_2 = listAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listAnswer.size());
                txtAnswerC.setText(listAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
        }

        listMoji.remove(listMoji.get(index_moji));
        // RE ADD answer To ListAnswer
        listAnswer.add(answer_mojiRemove);
        listAnswer.add(answerRemove_1);
        listAnswer.add(answerRemove_2);


    }
    public void updateQuestionKanji (ArrayList<Kanji> listKanji, ArrayList<String> listAnswer) {
        index_question++;
        //progressBar.setProgress(index_question * 100 / NUMBER_OF_QUESTION);
        String numberQuestion = "Question: " + String.valueOf(index_question) + "/"
                + String.valueOf(NUMBER_OF_QUESTION);
        String rightCount = "Correct: " + String.valueOf(number_of_right_answer) + " / "
                + String.valueOf(NUMBER_OF_QUESTION);
        txtNumberQuestion.setText(numberQuestion);
        txtRightCount.setText(rightCount);
        //Log.d("MOJI_SIZE", String.valueOf(mojiList.size()));
        Log.d("ANSWER_SIZE", String.valueOf(listAnswer.size()));
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
                listAnswer.remove(answer_kanjiRemove);


                index_one = new Random().nextInt(listAnswer.size());
                answerRemove_1 = listAnswer.get(index_one);
                txtAnswerB.setText(answerRemove_1);
                listAnswer.remove(answerRemove_1);
                index_two = new Random().nextInt(listAnswer.size());
                answerRemove_2 = listAnswer.get(index_two);

                txtAnswerC.setText(answerRemove_2);
                listAnswer.remove(answerRemove_2);
                index_three = new Random().nextInt(listAnswer.size());
                txtAnswerD.setText(listAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 1:
                txtAnswerB.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listAnswer.remove(answer_kanjiRemove);
                listAnswer.remove(listKanji.get(index_kanji).getAmhan());

                index_one = new Random().nextInt(listAnswer.size());
                answerRemove_1 = listAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listAnswer.size());
                answerRemove_2 = listAnswer.get(index_two);
                txtAnswerC.setText(answerRemove_2);
                listAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listAnswer.size());
                txtAnswerD.setText(listAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
            case 2:
                txtAnswerC.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listAnswer.remove(answer_kanjiRemove);
                listAnswer.remove(listKanji.get(index_kanji).getAmhan());

                index_one = new Random().nextInt(listAnswer.size());
                answerRemove_1 = listAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listAnswer.size());
                answerRemove_2 = listAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listAnswer.size());
                txtAnswerD.setText(listAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 3:
                txtAnswerD.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listAnswer.remove(answer_kanjiRemove);
                listAnswer.remove(listKanji.get(index_kanji).getAmhan());

                index_one = new Random().nextInt(listAnswer.size());
                answerRemove_1 = listAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listAnswer.size());
                answerRemove_2 = listAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listAnswer.size());
                txtAnswerC.setText(listAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
        }

        listKanji.remove(listKanji.get(index_kanji));
        // RE ADD answer To ListAnswer
        listAnswer.add(answer_kanjiRemove);
        listAnswer.add(answerRemove_1);
        listAnswer.add(answerRemove_2);


    }
    public void updateNumberOfRightAnswer () {
        txtRightCount.setText("Correct: " + number_of_right_answer + " / " + NUMBER_OF_QUESTION);
    }

    public void showDialog(boolean check) {
        settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (check){
            settingsDialog.setContentView(getLayoutInflater()
                    .inflate(R.layout.image_right, null));
        }else{
            settingsDialog.setContentView(getLayoutInflater()
                    .inflate(R.layout.image_wrong, null));
        }
        settingsDialog.show();

//        settingsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                updateQuestion(mojiList, answerList);
//            }
//        });

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
        layout.removeView(txtAnswerA);
        layout.removeView(txtAnswerB);
        layout.removeView(txtAnswerC);
        layout.removeView(txtAnswerD);
        //layout.removeView(progressBar);
        layout.removeView(txtRightCount);
        layout.removeView(txtNumberQuestion);

        txtQuestion.setTextSize(30f);

        txtQuestion.setText("RESULT: " + number_of_right_answer +"/" + NUMBER_OF_QUESTION+ " CORRECT");

        txtNotification.setVisibility(View.VISIBLE);
        btnMain.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        String question = txtQuestion.getText().toString();
        String answer = "";

        switch (view.getId()){
            case R.id.txtAnswerA:
                answer = txtAnswerA.getText().toString();
                for (QuestionAnswer item : QAList){
                    if (item.getQuestion().equalsIgnoreCase(question)){
                        if (item.getAnswer().equalsIgnoreCase(answer)){
                            check = true;
                            number_of_right_answer++;
                        }else{
                            check = false;
                        }
                    }
                }
                showDialog(check);
                if(isKanji){
                    if (kanjiList.isEmpty()){
                        Log.d("KANJIlist_EMPTY","KANJIlist_EMPTY");
                        updateNumberOfRightAnswer();
                        showResult();
                    }else{
                        updateQuestionKanji(kanjiList, answerList);
                    }
                }else{
                    if (mojiList.isEmpty()){
                        Log.d("MOJIlist_EMPTY","MOJIlist_EMPTY");
                        updateNumberOfRightAnswer();
                        showResult();
                    }else{
                        updateQuestionMoji(mojiList, answerList);
                    }
                }

                break;
            case R.id.txtAnswerB:

                answer = txtAnswerB.getText().toString();
                for (QuestionAnswer item : QAList){
                    if (item.getQuestion().equalsIgnoreCase(question)){
                        if (item.getAnswer().equalsIgnoreCase(answer)){
                            check = true;
                            number_of_right_answer++;
                        }else{
                            check = false;
                        }
                    }
                }
                showDialog(check);
                if(isKanji){
                    if (kanjiList.isEmpty()){
                        Log.d("KANJIlist_EMPTY","KANJIlist_EMPTY");
                        updateNumberOfRightAnswer();
                        showResult();
                    }else{
                        updateQuestionKanji(kanjiList, answerList);
                    }
                }else{
                    if (mojiList.isEmpty()){
                        Log.d("MOJIlist_EMPTY","MOJIlist_EMPTY");
                        updateNumberOfRightAnswer();
                        showResult();
                    }else{
                        updateQuestionMoji(mojiList, answerList);
                    }
                }

                break;
            case R.id.txtAnswerC:
                answer = txtAnswerC.getText().toString();
                for (QuestionAnswer item : QAList){
                    if (item.getQuestion().equalsIgnoreCase(question)){
                        if (item.getAnswer().equalsIgnoreCase(answer)){
                            check = true;
                            number_of_right_answer++;
                        }else{
                            check = false;
                        }
                    }
                }
                showDialog(check);
                if(isKanji){
                    if (kanjiList.isEmpty()){
                        Log.d("KANJIlist_EMPTY","KANJIlist_EMPTY");
                        updateNumberOfRightAnswer();
                        showResult();
                    }else{
                        updateQuestionKanji(kanjiList, answerList);
                    }
                }else{
                    if (mojiList.isEmpty()){
                        Log.d("MOJIlist_EMPTY","MOJIlist_EMPTY");
                        updateNumberOfRightAnswer();
                        showResult();
                    }else{
                        updateQuestionMoji(mojiList, answerList);
                    }
                }
                break;
            case R.id.txtAnswerD:
                answer = txtAnswerD.getText().toString();
                for (QuestionAnswer item : QAList){
                    if (item.getQuestion().equalsIgnoreCase(question)){
                        if (item.getAnswer().equalsIgnoreCase(answer)){
                            check = true;
                            number_of_right_answer++;
                        }else{
                            check = false;
                        }
                    }
                }
                showDialog(check);
                if(isKanji){
                    if (kanjiList.isEmpty()){
                        Log.d("KANJIlist_EMPTY","KANJIlist_EMPTY");
                        updateNumberOfRightAnswer();
                        showResult();
                    }else{
                        updateQuestionKanji(kanjiList, answerList);
                    }
                }else{
                    if (mojiList.isEmpty()){
                        Log.d("MOJIlist_EMPTY","MOJIlist_EMPTY");
                        updateNumberOfRightAnswer();
                        showResult();
                    }else{
                        updateQuestionMoji(mojiList, answerList);
                    }
                }
                break;

            case R.id.btnMain:
                startActivity(new Intent(TestActivity.this, FeatureActivity.class));
                break;

            case R.id.btnRetry:
//                startActivity(getIntent());
//                finish();
                Intent refresh = new Intent(this, TestActivity.class);
                if(isKanji){
                    Log.d("test", String.valueOf(kanjiList.size()));

                    refresh.putExtra(Constants.SET_BY_USER, oldKanjiList);
                    refresh.putExtra(Constants.KANJI, "KANJI");
                }
                else{

                    refresh.putExtra(Constants.SET_BY_USER, oldMojiList);
                    refresh.putExtra(Constants.MOJI, "MOJI");
                }
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
