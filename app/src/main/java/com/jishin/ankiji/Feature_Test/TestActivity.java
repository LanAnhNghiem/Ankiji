package com.jishin.ankiji.Feature_Test;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.QuestionAnswer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by huuduc on 17/01/2018.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    public static boolean check;
    public static final String BOOK = "Soumatome";
    public static final String REFRENCE = "moji";

//    public static final int NUMBER_OF_QUESTION = 50;

    private static int number_of_right_answer = 0;

    private static ArrayList<Moji> mojiList;
    private static ArrayList<String> answerList;
    private static ArrayList<QuestionAnswer> QAList;

    private ImageButton btnBack;
    private ProgressBar progressBar;
    private TextView txtRightCount, txtQuestion, txtAnswerA, txtAnswerB, txtAnswerC, txtAnswerD;

    public FirebaseDatabase database;
    public DatabaseReference mReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_test);

        addControls();
        database = FirebaseDatabase.getInstance();
        mReference = database.getReference(REFRENCE).child(BOOK).child("Bai_1");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Moji moji = snapshot.getValue(Moji.class);

                    mojiList.add(moji);
                    answerList.add(moji.getCachDocHira());
                    QAList.add(new QuestionAnswer(moji.getTuTiengNhat(), moji.getCachDocHira()));
                }

//                progressBarTask task = new progressBarTask();
//                task.execute(NUMBER_OF_QUESTION);

                updateQuestion(mojiList, answerList);
                txtAnswerA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(TestActivity.this, "clicked", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addEvents();

    }

    private void addEvents() {

    }

    private void addControls() {
        check = false;

        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.determinateBar);
        txtRightCount = findViewById(R.id.txtRightCount);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtAnswerA = findViewById(R.id.txtAnswerA);
        txtAnswerB = findViewById(R.id.txtAnswerB);
        txtAnswerC = findViewById(R.id.txtAnswerC);
        txtAnswerD = findViewById(R.id.txtAnswerD);


        mojiList = new ArrayList<>();
        answerList = new ArrayList<>();
        QAList = new ArrayList<>();



    }

    public void updateQuestion (ArrayList<Moji> listMoji, ArrayList<String> listAnswer) {

        int index_moji = new Random().nextInt(listMoji.size());

        int index_one;
        int index_two;
        int index_three;

        String answerRemove_1 = "";
        String answerRemove_2 = "";

        txtQuestion.setText(listMoji.get(index_moji).getTuTiengNhat());

        int index_one_location = new Random().nextInt(4);
        switch (index_one_location){
            case 0:
                txtAnswerA.setText(listMoji.get(index_moji).getCachDocHira());
                listMoji.remove(listMoji.get(index_moji));
                listAnswer.remove(listMoji.get(index_moji).getCachDocHira());

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
                
                break;
            case 1:
                txtAnswerB.setText(listMoji.get(index_moji).getCachDocHira());
                listMoji.remove(listMoji.get(index_moji));
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

                break;
            case 2:
                txtAnswerC.setText(listMoji.get(index_moji).getCachDocHira());
                listMoji.remove(listMoji.get(index_moji));
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


                break;
            case 3:
                txtAnswerD.setText(listMoji.get(index_moji).getCachDocHira());
                listMoji.remove(listMoji.get(index_moji));
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

                break;
        }

        // RE ADD answer To ListAnswer
        listAnswer.add(answerRemove_1);
        listAnswer.add(answerRemove_2);

    }

    public void updateNumberOfRightAnswer () {
        txtRightCount.setText(number_of_right_answer + " / " + mojiList.size());
    }

    public void showDialog(boolean check) {
        Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (check){
            settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_right
                    , null));
        }else{
            settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_wrong
                    , null));
        }
        settingsDialog.show();
    }

    @Override
    public void onClick(View view) {
        String question = txtQuestion.getText().toString();
        String answer = "";

        switch (view.getId()){
            case R.id.txtAnswerA:
                Toast.makeText(TestActivity.this, "TXTA_CLIKED", Toast.LENGTH_LONG).show();
                txtAnswerA.setBackgroundColor(Color.HSVToColor(new float[] {0, 0, 83}));
                answer = txtAnswerA.getText().toString();
                for (QuestionAnswer item : QAList){
                    if (item.getQuestion().equalsIgnoreCase(question)){
                        if (item.getAnswer().equalsIgnoreCase(answer)){
                            check = true;
                            number_of_right_answer++;
                            showDialog(check);
                            updateQuestion(mojiList, answerList);
                            updateNumberOfRightAnswer();
                        }
                    }
                }

                break;
            case R.id.txtAnswerB:
                txtAnswerB.setBackgroundColor(Color.HSVToColor(new float[] {0, 0, 83}));
                answer = txtAnswerA.getText().toString();
                for (QuestionAnswer item : QAList){
                    if (item.getQuestion().equalsIgnoreCase(question)){
                        if (item.getAnswer().equalsIgnoreCase(answer)){
                            check = true;
                            number_of_right_answer++;
                            showDialog(check);
                            updateQuestion(mojiList, answerList);
                            updateNumberOfRightAnswer();
                        }
                    }
                }
                break;
            case R.id.txtAnswerC:
                txtAnswerC.setBackgroundColor(Color.HSVToColor(new float[] {0, 0, 83}));
                answer = txtAnswerA.getText().toString();
                for (QuestionAnswer item : QAList){
                    if (item.getQuestion().equalsIgnoreCase(question)){
                        if (item.getAnswer().equalsIgnoreCase(answer)){
                            check = true;
                            number_of_right_answer++;
                            showDialog(check);
                            updateQuestion(mojiList, answerList);
                            updateNumberOfRightAnswer();
                        }
                    }
                }
                break;
            case R.id.txtAnswerD:
                txtAnswerD.setBackgroundColor(Color.HSVToColor(new float[] {0, 0, 83}));
                answer = txtAnswerA.getText().toString();
                for (QuestionAnswer item : QAList){
                    if (item.getQuestion().equalsIgnoreCase(question)){
                        if (item.getAnswer().equalsIgnoreCase(answer)){
                            check = true;
                            number_of_right_answer++;
                            showDialog(check);
                            updateQuestion(mojiList, answerList);
                            updateNumberOfRightAnswer();
                        }
                    }
                }
                break;

            case R.id.btnBack:
                // TODO : Back to other Activity
                break;
        }
    }

    class progressBarTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0);
            txtRightCount.setText("Correct: 0/" + mojiList.size());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setProgress(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int percent = values[0];
            progressBar.setProgress(percent);

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int question = integers[0];

            for (int i = 0; i < question; i++){
                int percent = i * 100 / question;
                publishProgress(percent);
            }
            return null;
        }
    }
}
